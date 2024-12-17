package com.example.collegeconnect.Teacher

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.collegeconnect.Activity.Selectrole
import com.example.collegeconnect.Admin.Admin_dashboard.User
import com.example.collegeconnect.Admin.add_fee
import com.example.collegeconnect.Admin.admin_settings
import com.example.collegeconnect.Admin.fragment.admin_home
import com.example.collegeconnect.Admin.fragment.admin_student
import com.example.collegeconnect.Admin.fragment.admin_teacher
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.Teacher.fragment.teacher_home
import com.example.collegeconnect.Teacher.fragment.teacher_student
import com.example.collegeconnect.databinding.ActivityTeacherDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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



        setupToolbarMenu()

        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,teacher_home())
                .commit()
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.teacher_home -> {
                    loadFragment(teacher_home())
                    toolbar.title = getString(R.string.cc)
                    true
                }
                R.id.teacher_student-> {
                    loadFragment(teacher_student())
                    toolbar.title = getString(R.string.teacher)
                    true
                }

                else -> false
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun setupToolbarMenu() {
        val menuButton = findViewById<View>(R.id.menu)
        menuButton.setOnClickListener { anchor ->
            showDropdownMenu(anchor)
        }
    }

    private fun showDropdownMenu(anchor: View) {
        val popupMenu = PopupMenu(this, anchor)
        popupMenu.menuInflater.inflate(R.menu.admin_option, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this,teacher_profile::class.java))
                    true
                }
                R.id.update_Profile -> {
                    startActivity(Intent(this, teacher_upload::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this,teacher_settings::class.java))
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }


    }
