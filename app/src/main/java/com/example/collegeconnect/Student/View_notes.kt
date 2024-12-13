package com.example.collegeconnect.Student

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.fragment.student_home
import com.example.collegeconnect.Student.fragment.student_notes
import com.example.collegeconnect.databinding.ActivityViewNotesBinding

class View_notes : AppCompatActivity() {
    private lateinit var binding : ActivityViewNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_notes)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_view_notes)
        changeStatusBarColor()
        binding.toolbar.setNavigationOnClickListener{
            startActivity(Intent(this, Student_dashboard::class.java))
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
}