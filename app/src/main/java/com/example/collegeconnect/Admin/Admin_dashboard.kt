package com.example.collegeconnect.Admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment

import com.example.collegeconnect.Admin.fragment.admin_home
import com.example.collegeconnect.Admin.fragment.admin_student
import com.example.collegeconnect.Admin.fragment.admin_teacher

import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityAdminDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Admin_dashboard : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityAdminDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_dashboard)

        changeStatusBarColor()
        auth = FirebaseAuth.getInstance()



        val homefragment = admin_home()
        val teacherfragment = admin_teacher()
        val studentfragment = admin_student()

        if (savedInstanceState == null){
            setcurrentfragment(homefragment)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.admin_home -> setcurrentfragment(homefragment)
                R.id.admin_student -> setcurrentfragment(studentfragment)
                R.id.admin_teacher -> setcurrentfragment(teacherfragment)
            }
            true
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    data class User(
        val username: String = "",
        val email: String = ""
    ) {
        constructor() : this("", "")
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }

    private fun setcurrentfragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply(){
            replace(R.id.flFragment,fragment)
            commit()
        }
}