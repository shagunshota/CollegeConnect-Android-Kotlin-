package com.example.collegeconnect.Superadmin

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.collegeconnect.R
import com.example.collegeconnect.Superadmin.fragment.supad_admin
import com.example.collegeconnect.Superadmin.fragment.supad_home
import com.example.collegeconnect.Superadmin.fragment.supad_student
import com.example.collegeconnect.Superadmin.fragment.supad_teacher
import com.example.collegeconnect.databinding.ActivitySuperadminDashboardBinding

class superadmin_dashboard : AppCompatActivity() {


    private lateinit var binding: ActivitySuperadminDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_superadmin_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_superadmin_dashboard)


        changeStatusBarColor()

        val homefragment = supad_home()
        val adminfragment = supad_admin()
        val teacherfragment = supad_teacher()
        val studentfragment = supad_student()

        if (savedInstanceState == null) {
            setcurrentstate(homefragment)
        }
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.supad_home -> setcurrentstate(homefragment)
                R.id.supad_admin -> setcurrentstate(adminfragment)
                R.id.supad_teacher -> setcurrentstate(teacherfragment)
                R.id.supad_student -> setcurrentstate(studentfragment)
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

    private fun setcurrentstate(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()

        }
    }
}