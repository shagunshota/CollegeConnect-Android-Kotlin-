package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Teacher
import com.example.collegeconnect.R

class Teacher_Adapter(
    private val teacherList: MutableList<Teacher>,
    private val deleteteacher: (String, String, String) -> Unit
) : RecyclerView.Adapter<Teacher_Adapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view, deleteteacher)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val teacher = teacherList[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int = teacherList.size

    class UserViewHolder(itemView: View, private val deleteteacher: (String,String, String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val deleteTeacherImageView: ImageView = itemView.findViewById(R.id.deleteuser)

        fun bind(teacher: Teacher) {
            usernameTextView.text = teacher.username
            emailTextView.text = teacher.email
            numberTextView.text = teacher.number
            deleteTeacherImageView.setOnClickListener {
                deleteteacher(teacher.uniqueId,teacher.email,teacher.password)
            }
        }
    }

}
