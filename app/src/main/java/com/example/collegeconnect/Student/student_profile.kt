package com.example.collegeconnect.Student

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.admin_profile.Admins
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R

import com.example.collegeconnect.databinding.ActivityStudentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class student_profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  binding : ActivityStudentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_profile)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_profile)
        auth = FirebaseAuth.getInstance()

        changeStatusBarColor()
        fetchStudentDetails()


        binding.toolbar.setNavigationOnClickListener {

            startActivity(Intent(this,Student_dashboard::class.java))

        }

        binding.clpass.setOnClickListener {

            val userId = auth.currentUser?.uid
            if(userId!=null){
                val database = FirebaseDatabase.getInstance().getReference("Student").child(userId)
                database.get().addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val user = task.result.getValue(Users::class.java)
                        if(user!=null){
                           val password = " ${user.password}"
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Student Password")
                            builder.setMessage( "$password")
                            builder.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            builder.show()

                        }
                        }
                        else{
                            Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show()
                        }

                }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchStudentDetails() {
        val userId = auth.currentUser?.uid
        if(userId!=null){
            val database = FirebaseDatabase.getInstance().getReference("Student").child(userId)
            database.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.getValue(Users::class.java)
                    if(user!=null){
                        binding.name.text="${user.username}"
                        binding.branch.text="${user.branch}"
                        binding.ebranch.text="${user.branch}"
                        binding.rollno.text="${user.rollno}"
                        binding.number.text="${user.number}"
                        binding.semester.text="${user.semester}"
                        binding.gender.text="${user.gender}"
                        binding.session.text="${user.session}"
                        binding.dob.text="${user.dob}"

                    }
                } }
        }
    }
    data class Users(
        val username: String = "",
        val email: String = "",
        val rollno : String =" ",
        val password : String =" ",
        val branch: String =" ",
        val number : String =" ",
        val semester : String =" ",
        val dob : String =" ",
        val session : String =" ",
        val gender : String =" "
    ) {
        constructor() : this("", "")  // No-argument constructor
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }
}