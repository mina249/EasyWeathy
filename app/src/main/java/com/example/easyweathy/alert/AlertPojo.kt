package com.example.easyweathy.alert

import androidx.room.Entity
import androidx.room.PrimaryKey


class AlertPojo(
    var startDate:String,
    var endDate:String,
    var AlertType:String="Alert",
    @PrimaryKey
    var id:Int

) {

}