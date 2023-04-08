package com.example.easyweathy.alert.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.easyweathy.OnAlertDeleteListener
import com.example.easyweathy.R
import com.example.easyweathy.databinding.AlertRvBinding
import com.example.easyweathy.favourite.view.FavouriteAdapter
import com.example.easyweathy.utilities.Utility


class AlertAdapter(var alertList:List<AlertPojo>,var listener: OnAlertDeleteListener,var context: Context):RecyclerView.Adapter<AlertAdapter.AlertHolder>() {

    lateinit var binding:AlertRvBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding =AlertRvBinding.inflate(inflater, parent, false)
        return AlertHolder(binding)

    }

    override fun getItemCount(): Int {
       return alertList.size
    }

    override fun onBindViewHolder(holder: AlertHolder, position: Int) {
        var alert = alertList[position]
        holder.binding.alertRvStartDate.text = alert.startDate
        holder.binding.alertRvEndDate.text = alert.endDate
        if(alert.AlertType == "Alert"){
            holder.binding.imgAlertRv.setImageResource(R.drawable.snow)
        }else{
            holder.binding.imgAlertRv.setImageResource(R.drawable.sun)
        }
        holder.binding.alertDelete.setOnClickListener(){
            dialogDeleteConfirmation(position,holder)
        }
        if(alert.AlertType == "Alert"){
            holder.binding.imgAlertRv.setImageResource(R.drawable.alarm)
        }else{
            holder.binding.imgAlertRv.setImageResource(R.drawable.chat)
        }
      holder. binding.alertRvCountry.text = Utility.getAdressName(alert.lat,alert.longitude,context)
        holder.binding.alertTypeTv.text = alert.alertDescription


    }
    inner class AlertHolder(val binding:AlertRvBinding):ViewHolder(binding.root)

    @SuppressLint("SuspiciousIndentation")
    fun dialogDeleteConfirmation(position:Int, holder: AlertHolder) {
        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delete_from_favourite)
        val window: Window? = dialog.getWindow()
        window?.setLayout(
            Constraints.LayoutParams.MATCH_PARENT,
            Constraints.LayoutParams.WRAP_CONTENT

        )
        window?.setBackgroundDrawableResource(R.color.transparent);
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        dialog.findViewById<TextView>(R.id.tv_delete_confirm).setText(R.string.alert_delete)
        dialog.findViewById<Button>(R.id.btn_delete_fav).setOnClickListener {
            dialog.dismiss()
            holder.binding.alertDelete.playAnimation()
            Handler().postDelayed({
                holder.binding.alertCard.animate().translationX(-1400f).setDuration(500)
            },2500)

            android.os.Handler().postDelayed({
                listener.onAlertDeleteListener(alertList[position])
            }, 3000)


        }
        dialog.findViewById<Button>(R.id.btn_cancel_fav).setOnClickListener() {
            dialog.dismiss()
        }


    }
}