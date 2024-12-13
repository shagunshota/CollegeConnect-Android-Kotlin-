package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Marks
import com.example.collegeconnect.R


class MarksAdapter(private val marksList: List<Marks>) : RecyclerView.Adapter<MarksAdapter.MarksViewHolder>() {


    inner class MarksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subject: TextView = view.findViewById(R.id.subject)
        val rollNumber: TextView = view.findViewById(R.id.rollno)
        val obtainedMarks: TextView = view.findViewById(R.id.obtaimarks)
        val totalMarks: TextView = view.findViewById(R.id.totalmarks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.marks_layout, parent, false)
        return MarksViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarksViewHolder, position: Int) {
        val marks = marksList[position]
        holder.subject.text = marks.subject
        holder.rollNumber.text = marks.rollNumber
        holder.obtainedMarks.text = marks.obtainedmarks
        holder.totalMarks.text = marks.totalmarks
    }

    override fun getItemCount(): Int = marksList.size
}
