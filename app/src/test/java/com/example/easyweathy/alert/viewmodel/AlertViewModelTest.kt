package com.example.easyweathy.alert.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.easyweathy.MainDispatchersRule
import com.example.easyweathy.alert.view.AlertPojo
import com.example.easyweathy.model.FakeRepo
import com.example.myweatherapp.getOrAwaitValue
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AlertViewModelTest{
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatchersRule = MainDispatchersRule()
    lateinit var viewModel : AlertViewModel
    lateinit var repo : FakeRepo
    lateinit var alert: AlertPojo
    lateinit var myAlert: AlertPojo
    lateinit var alertList:MutableList<AlertPojo>
    fun createAlertPojoTest():AlertPojo{
        return AlertPojo("start","end","alert",31.3,35.5,"rain",5)
    }
    fun creteAlert():AlertPojo{
        return AlertPojo("date","edndDate","not",88.5,99.5,"wind",66)
    }

    @Before
    fun initialSetup(){
        alertList = mutableListOf<AlertPojo>()
        repo = FakeRepo()
        viewModel = AlertViewModel(repo)
       alert = createAlertPojoTest()
       myAlert = creteAlert()
    }

    @Test
    fun addAlert_addAlert_returnThatAlert(){
        //given
        viewModel.addAlert(alert)
        alertList.add(alert)
        //when
         viewModel.getAlerts()
        var result = viewModel._alertList.getOrAwaitValue {  }
        //then
        assertThat(result, CoreMatchers.`is`(alertList))

    }

    @Test
    fun deleteAlert_addAlertThenDelete_emptyList(){
        //given
        viewModel.addAlert(myAlert)
        viewModel.deleteAlert(myAlert)

        //when
        viewModel.getAlerts()
        var result = viewModel._alertList.getOrAwaitValue{}
        //then
        assertThat(result, CoreMatchers.`is`(emptyList()))

    }

    @Test
    fun getAllAlerts_addAlertAndMyAlert_listOfAlertAndMyAlert(){
        //given
        viewModel.addAlert(myAlert)
        viewModel.addAlert(alert)
        alertList.add(myAlert)
        alertList.add(alert)

        //when
        viewModel.getAlerts()
        var result = viewModel._alertList.getOrAwaitValue{}
        //then
        assertThat(result, CoreMatchers.`is`(alertList))

    }


}