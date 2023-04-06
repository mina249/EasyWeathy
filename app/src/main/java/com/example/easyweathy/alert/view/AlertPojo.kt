package com.example.easyweathy.alert.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alert_Table")
class AlertPojo(
    var startDate:String,
    var endDate:String,
    var AlertType:String="Alert",
    @PrimaryKey
    var id:Int

) {

}