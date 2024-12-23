package com.example.collegeconnect.Teacher

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
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.student_profile.Users
import com.example.collegeconnect.Teacher.fragment.teacher_home
import com.example.collegeconnect.databinding.ActivityTeacherProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class teacher_profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityTeacherProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_profile)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_teacher_profile)
        auth = FirebaseAuth.getInstance()

        changeStatusBarColor()
        fetchTeacherData()
        binding.teachertoolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Teacher_dashboard::class.java))

        }

        binding.clpass.setOnClickListener {

            val userId = auth.currentUser?.uid
            if(userId!=null){
                val database = FirebaseDatabase.getInstance().getReference("Teacher").child(userId)
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

    private fun fetchTeacherData() {
        val userId = auth.currentUser?.uid
        if(userId!=null){
            val database = FirebaseDatabase.getInstance().getReference("Teacher").child(userId)
            database.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.getValue(Users::class.java)
                    if(user!=null){
                        binding.name.text="${user.username}"
                        binding.subject.text="${user.subject}"
                        binding.email.text="${user.email}"
                        binding.uniqueid.text="${user.uniqueId}"
                        binding.number.text="${user.number}"
                        binding.ebranch.text="${user.branch}"
                        binding.esubject.text="${user.subject}"
                        binding.gender.text="${user.gender}"
                        binding.experience.text="${user.experience}"



                    }
                    else{
                        Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this,"Failed to load data",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    data class Users(
        val username: String = "",
        val email: String = "",
        val subject : String =" ",
        val uniqueId : String =" ",
        val number: String =" ",
        val branch : String =" ",
        val gender : String =" ",
        val experience : String =" ",
        val password: String =" "
    ) {
        constructor() : this("", "","","")  // No-argument constructor
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }
}