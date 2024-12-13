package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Attendence
import com.example.collegeconnect.R

class AttendenceAdapter(private val attendenceList : List<Attendence>) : RecyclerView.Adapter<AttendenceAdapter.AttendenceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendenceAdapter.AttendenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.attendence, parent, false)
        return AttendenceViewHolder(view)
    }
    inner class AttendenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subject: TextView = view.findViewById(R.id.subject)
        val rollno: TextView = view.findViewById(R.id.rollno)
        val attendedlec : TextView = view.findViewById(R.id.attend_lec)
        val totallec : TextView = view.findViewById(R.id.total_lec)
    }


    override fun onBindViewHolder(holder: AttendenceAdapter.AttendenceViewHolder, position: Int) {
        val attendence = attendenceList[position]
        holder.subject.text = attendence.subject
        holder.rollno.text = attendence.rollNumber
        holder.attendedlec.text= attendence.attendedLectures
        holder.totallec.text= attendence.totalLectures

    }

    override fun getItemCount(): Int = attendenceList.size
}