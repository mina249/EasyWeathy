package com.example.easyweathy.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.*
import com.example.myweatherapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ConcreteLocalSourceTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    lateinit var local: ConcreteLocalSource
    lateinit var alert: AlertPojo
    lateinit var myAlert: AlertPojo
    lateinit var alertList: MutableList<AlertPojo>
    fun createAlertPojoTest(): AlertPojo {
        return AlertPojo("start", "end", "alert", 31.3, 35.5, "rain", 5)
    }

    fun creteAlert(): AlertPojo {
        return AlertPojo("date", "edndDate", "not", 88.5, 99.5, "wind", 66)
    }

    @Before
    fun initialSetup() {
        local = ConcreteLocalSource.getInstance(ApplicationProvider.getApplicationContext())
        alertList = mutableListOf()
        alert = createAlertPojoTest()
        myAlert = creteAlert()
    }

    @Test
    fun addWAlert_Alert_listContainAlert() = runBlocking {
        //given->
        local.addAlert(alert)

        //when ->
        var result = local.getAllAlerts().getOrAwaitValue { }
        var data = result[0]

        //then
        assertThat(data.id, CoreMatchers.`is`(alert.id))
    }

    @Test
    fun removeAlert_AlertThenRemoveAlert_emptyList() = runBlocking {
        //given->
        local.addAlert(alert)
        local.removeAlert(alert)
        //when ->
        var result = local.getAllAlerts().getOrAwaitValue { }
        //then
        assertThat(result, CoreMatchers.`is`(emptyList()))
    }

    @Test
    fun getAllAlerts_AlertAndMyAlert_listOfAlertAndMyAlert() = runBlocking {
        //given->
        local.addAlert(alert)
        local.addAlert(myAlert)
        //when ->
        var result = local.getAllAlerts().getOrAwaitValue { }
        var data = result[0]
        var myData = result[1]
        //then
        assertThat(data.id, CoreMatchers.`is`(alert.id))
        assertThat(myData.id, CoreMatchers.`is`(myAlert.id))
    }


}