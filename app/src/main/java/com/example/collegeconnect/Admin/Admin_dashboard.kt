package com.example.collegeconnect.Admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.collegeconnect.Admin.fragment.addfeeFragment

import com.example.collegeconnect.Admin.fragment.admin_home
import com.example.collegeconnect.Admin.fragment.admin_student
import com.example.collegeconnect.Admin.fragment.admin_teacher

import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityAdminDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        setupToolbarMenu()
        changeStatusBarColor()
        auth = FirebaseAuth.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, admin_home())
                .commit()
        }


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_home -> {
                    loadFragment(admin_home())
                    toolbar.title = getString(R.string.cc)
                    true
                }
                R.id.admin_teacher-> {
                    loadFragment(admin_teacher())
                    toolbar.title = getString(R.string.teacher)
                    true
                }
                R.id.admin_student -> {
                    loadFragment(admin_student())
                    toolbar.title = getString(R.string.student)
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
                    startActivity(Intent(this,com.example.collegeconnect.Admin.admin_profile::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this,admin_settings::class.java))
                    true
                }
                R.id.add_fee -> {
                   startActivity(Intent(this,add_fee::class.java))
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
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
    private fun loadFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


}