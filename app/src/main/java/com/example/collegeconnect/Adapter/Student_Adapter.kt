package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Student
import com.example.collegeconnect.R


class Student_Adapter(
    private val studentList: MutableList<Student>,
    private val deletestudent: (String)-> Unit,
    private val updatestudent : (Student)-> Unit
) : RecyclerView.Adapter<Student_Adapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view,deletestudent,updatestudent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student)

    }

    override fun getItemCount(): Int = studentList.size




    class UserViewHolder(itemView: View, private val deletestudent: (String) -> Unit , private val updatestudent: (Student) -> Unit)  : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
        private val deltestudent : ImageView = itemView.findViewById(R.id.deleteuser)
        private val editstudent : ImageView = itemView.findViewById(R.id.edituser)

        fun bind(student: Student) {
            usernameTextView.text =student.username
            emailTextView.text = student.email
            numberTextView.text = student.number
            deltestudent.setOnClickListener { deletestudent(student.uniqueId)
            }
            editstudent.setOnClickListener {

                updatestudent(student)
            }

        }
    }
}
