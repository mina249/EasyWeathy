package com.example.easyweathy.favourite.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.easyweathy.MainDispatchersRule
import com.example.easyweathy.model.*
import com.example.myweatherapp.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    lateinit var viewModel :FavouriteViewModel
    lateinit var repo : FakeRepo
    lateinit var weatherResponse: WeatherResponse
    lateinit var myWeatherResponse: WeatherResponse
    lateinit var weatherList:MutableList<WeatherResponse>

        fun createTestWeatherResponse(){
        var weather = Weather(123L,"weatherDescription","01d")
        var temp = Temp(2.0,1.0,3.0,5.0,6.0,9.0)
        var feels = FeelsLike(2.0,3.0,4.0,5.0)
        var daily = Daily(5555666L,6666L,777777L,8888L,999L,7.0,temp,
        feels,5L,6L,5.0,6.0,6L,5.0, listOf(weather),
        6L,5.0,6.0,2.3)
          var alert =Alerts()
          var current = Current(5L,8L,5L,5.5,6.3,3L,6L,7.5,9.0,9L,6L,5.0,9L,5.5, listOf(weather))
             weatherResponse = WeatherResponse(31.3,32.5,"africa",6L,current, listOf(current), listOf(daily),"status", listOf(alert))
             myWeatherResponse = WeatherResponse(50.5,60.5,"america",8L,current, listOf(current),
                listOf(daily),"myStatus", listOf(alert))
        }


    @Before
    fun initialSetup(){
        weatherList = mutableListOf<WeatherResponse>()
        repo = FakeRepo()
        viewModel = FavouriteViewModel(repo)
        createTestWeatherResponse()
    }

    @Test
    fun addToFavourite_addWeather_listOfWeatherContainAddedWeather()= runBlockingTest{
        // given ->
       viewModel.insertFavouriteWeather(31.3,32.5,"en","standard")
        weatherList.add(weatherResponse)
        //when - >
        viewModel.getAllFavWeather()
        var result = viewModel._favouriteWeatherList.getOrAwaitValue {  }
        // then ->

       assertThat(result, `is`(weatherList))

    }

    @Test
    fun deleteWeather_deleteWeatherFromDataBase_listIsEmpty()= runBlockingTest {
        // given ->
        repo.addWeatherToFavourite(weatherResponse)
        viewModel.deleteFromFavourite(weatherResponse)

        //when ->
        viewModel.getAllFavWeather()
        var result = viewModel._favouriteWeatherList.getOrAwaitValue {  }
        // then ->
        assertThat(result, `is`(emptyList()))

    }

    @Test
    fun getAllFavourite_add2weatherResponse_get_the_2_WeatherResponse()= runBlockingTest{

        // given ->
        repo.addWeatherToFavourite(weatherResponse)
        repo.addWeatherToFavourite(myWeatherResponse)
        weatherList.add(weatherResponse)
        weatherList.add(myWeatherResponse)
         // when - >
        viewModel.getAllFavWeather()
        var result = viewModel._favouriteWeatherList.getOrAwaitValue{}
        // then - >
        assertThat(result, `is`(weatherList))


    }

}