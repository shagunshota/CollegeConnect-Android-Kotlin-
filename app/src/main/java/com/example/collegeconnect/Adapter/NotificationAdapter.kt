package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.collegeconnect.Models.Notification
import com.example.collegeconnect.R

class NotificationAdapter (private val notificationList: List<Notification>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.titletext)
        val descriptionText: TextView = view.findViewById(R.id.descriptiontext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_layout, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.titleText.text = notification.title
        holder.descriptionText.text = notification.description
    }

    override fun getItemCount(): Int = notificationList.size
}
