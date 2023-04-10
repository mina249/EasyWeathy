package com.example.easyweathy.database


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.*
import com.example.myweatherapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {
    @get:Rule
    val instance = InstantTaskExecutorRule()
    private lateinit var weatherDataBase : WeatherDataBase
   lateinit var alert1:AlertPojo
    lateinit var alert:AlertPojo
    lateinit var alertList :MutableList<AlertPojo>

    fun createAlertPojoTest():AlertPojo{
        return AlertPojo("start","end","alert",31.3,35.5,"rain",5)
    }
    fun creteAlert():AlertPojo{
        return AlertPojo("date","edndDate","not",88.5,99.5,"wind",66)
    }
    @Before
    fun initialSetup(){
        alertList = mutableListOf()
        weatherDataBase=
            Room.inMemoryDatabaseBuilder(getApplicationContext(),WeatherDataBase::class.java).build()
    }
    @After
    fun closeDataBase(){
      weatherDataBase.close()
    }

    @Test
    fun addWeatherResponse_weatherResponse_getTheWeatherAdded()=runBlocking{
        //given->
        alert = createAlertPojoTest()
        weatherDataBase.getWeatherDao().insertAlarm(alert)
        //when->
        var result = weatherDataBase.getWeatherDao().getAllAlerts().getOrAwaitValue {  }
        // then ->
        assertThat(result,`is`(not(emptyList())))
    }

   @Test
    fun removeAlert_AddThenDeleteAlertAndAlert1_listISEmpty()=
        runBlocking{
        //give->
        alert = createAlertPojoTest()
         alert1 = creteAlert()
        weatherDataBase.getWeatherDao().insertAlarm(alert)
        weatherDataBase.getWeatherDao().insertAlarm(alert1)
        weatherDataBase.getWeatherDao().deleteAlert(alert)
            weatherDataBase.getWeatherDao().deleteAlert(alert1)
         //when->
         var result =  weatherDataBase.getWeatherDao().getAllAlerts().getOrAwaitValue {  }
         //then->
            assertThat(result,`is`(emptyList()))
    }

    @Test
    fun getAllAlerts_addAlert_ListOfAlert()= runBlocking{
        //give->
        alert = createAlertPojoTest()
        alert1 = creteAlert()
        alertList.add(alert)
        weatherDataBase.getWeatherDao().insertAlarm(alert)
        //when->
        var result = weatherDataBase.getWeatherDao().getAllAlerts().getOrAwaitValue {  }
        var alertdata = result[0]
        //then->
        assertThat(alertdata.id,`is`(alert.id))
    }

}