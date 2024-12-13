package com.example.collegeconnect.Teacher

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.collegeconnect.Activity.Selectrole
import com.example.collegeconnect.Admin.Admin_dashboard.User
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.Teacher.fragment.teacher_home
import com.example.collegeconnect.Teacher.fragment.teacher_student
import com.example.collegeconnect.databinding.ActivityTeacherDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Teacher_dashboard : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityTeacherDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_dashboard)
        auth = FirebaseAuth.getInstance()
        changeStatusBarColor()




        val homefragment = teacher_home()
        val studentfragment = teacher_student()

        if (savedInstanceState == null){
            setcurrentfragment(homefragment)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.teacher_home -> setcurrentfragment(homefragment)
                R.id.teacher_student -> setcurrentfragment(studentfragment)
            }
            true
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }

    private fun setcurrentfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    }
