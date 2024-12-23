package com.example.collegeconnect

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Activity.Selectrole
import com.example.collegeconnect.Admin.Admin_dashboard
import com.example.collegeconnect.Student.Student_dashboard
import com.example.collegeconnect.Teacher.Teacher_dashboard
import com.example.collegeconnect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val slideanim = AnimationUtils.loadAnimation(this, R.anim.slide_right)
        binding.ss1.startAnimation(slideanim)


        val slideanim1 = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        binding.ss2.startAnimation(slideanim1)


        val slideanim2 = AnimationUtils.loadAnimation(this, R.anim.slide_top)
        binding.si1.startAnimation(slideanim2)


        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val role = sharedPreferences.getString("role", "")

            if (isLoggedIn) {
                navigatetodashboard(role)
            } else {
                startActivity(Intent(this, Selectrole::class.java))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            finish()


        }, 3000)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigatetodashboard(role: String?) {
        val intent = when (role) {
            "Admin" -> Intent(this, Admin_dashboard::class.java)
            "Teacher" -> Intent(this, Teacher_dashboard::class.java)
            "Student" -> Intent(this, Student_dashboard::class.java)
            else -> Intent(this, Selectrole::class.java)
        }
        startActivity(intent)

    }

    private fun <T> navigateTo(activity: Class<T>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }


}