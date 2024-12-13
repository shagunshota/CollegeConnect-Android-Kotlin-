package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Event
import com.example.collegeconnect.R

class EventAdapter(private val eventList: List<Event>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.eventtitle)
        val description: TextView = view.findViewById(R.id.eventdesc)
        val venue : TextView = view.findViewById(R.id.eventvenue)
        val timing : TextView = view.findViewById(R.id.eventtime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_layout, parent, false)
        return EventViewHolder(view)
    }




    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.title.text = event.title
        holder.description.text = event.description
        holder.venue.text=event.venue
        holder.timing.text= event.timing
    }

    override fun getItemCount(): Int = eventList.size
}