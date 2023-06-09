package com.example.easyweathy.alert.view

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Alert_Table")
class AlertPojo(
    var startDate:Long,
    var endDate:Long,
    var AlertType:String="Alert",
    var lat:Double,
    var longitude:Double,
    var alertDescription:String="Rain",
    @PrimaryKey
    var id:Int,



):java.io.Serializable {

}