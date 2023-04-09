package com.example.easyweathy.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.easyweathy.MainDispatchersRule
import com.example.myweatherapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ConcreteRepoTest{
    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()

    lateinit var local:DataSource
    lateinit var remote:DataSource
    lateinit var weatherResponse:WeatherResponse
    lateinit var myWeatherResponse:WeatherResponse
    lateinit var repo: ConcreteRepo
    lateinit var result:WeatherResponse
    lateinit var list : MutableList<WeatherResponse>
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

        local = DataSource()
        remote = DataSource()
        repo = ConcreteRepo.getInstance(remote,local)
        list = mutableListOf()

    }

    @Test
    fun getWeatherForHome_lat_long_language_units_weatherResponse()=runBlockingTest{
        //given ->
        createTestWeatherResponse()


        //when ->
        var flowResult = repo.getWeatherForHomeScreen(31.5,32.5,"standard","en").getOrAwaitValue {  }

        //then ->
        assertThat(flowResult, `is`(weatherResponse))

    }

    @Test
    fun addWeatherToFavourite_weatherResponse_listContainTheWeatherResponse()= runBlockingTest{
        // give - >
        createTestWeatherResponse()
     repo.addWeatherToFavourite(weatherResponse)
        list.add(weatherResponse)
        //when->
        var result = repo.getFavouriteWeather().getOrAwaitValue {  }
        //then
       assertThat(result, `is`(list))

    }

    @Test
    fun deleteFromFavourite_addToWeatherResponseObjectRemoveOne_getOneWeatherResponse()=
        runBlockingTest{
        //given
        createTestWeatherResponse()
        repo.addWeatherToFavourite(weatherResponse)
        repo.addWeatherToFavourite(myWeatherResponse)
        list.add(myWeatherResponse)
         repo.deleteFavouriteWeather(weatherResponse)
        //when - >
         var result = repo.getFavouriteWeather().getOrAwaitValue {  }
         // then ->
         assertThat(result, `is`(list))
    }

    @Test
    fun etSelectedFavouriteWeatherDetails_latAndLong_weatherResponseWithTheLatAndLong()=runBlockingTest{
        //given
        createTestWeatherResponse()
        repo.addWeatherToFavourite(weatherResponse)
        repo.addWeatherToFavourite(myWeatherResponse)

        //when->
        var result = repo.getSelectedFavouriteWeatherDetails(weatherResponse.lat,weatherResponse.lon).getOrAwaitValue {  }

        //then
        assertThat(result, `is`(weatherResponse))

    }





}