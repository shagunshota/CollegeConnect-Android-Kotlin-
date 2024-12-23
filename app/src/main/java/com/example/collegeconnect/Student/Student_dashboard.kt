package com.example.collegeconnect.Student

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
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
import com.example.collegeconnect.Student.fragment.student_attendance
import com.example.collegeconnect.Student.fragment.student_home
import com.example.collegeconnect.Student.fragment.student_marks
import com.example.collegeconnect.Student.fragment.student_notes
import com.example.collegeconnect.databinding.ActivityStudentDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Student_dashboard : AppCompatActivity() {

    private lateinit var binding : ActivityStudentDashboardBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_student_dashboard)
        auth = FirebaseAuth.getInstance()
        changeStatusBarColor()
        val homefragment = student_home()
        val attendanceFragment = student_attendance()
        val marksFragment = student_marks()
        val notesFragment = student_notes()

        if (savedInstanceState == null) {
            setCurrentFragment(homefragment)
        }



        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homefragment)
                R.id.marks -> setCurrentFragment(marksFragment)
                R.id.attendance -> setCurrentFragment(attendanceFragment)
                R.id.notes -> setCurrentFragment(notesFragment)
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }

    private fun setCurrentFragment(fragment: Fragment) =
//
        supportFragmentManager.beginTransaction().replace(R.id.flFragment,fragment).commit()

}