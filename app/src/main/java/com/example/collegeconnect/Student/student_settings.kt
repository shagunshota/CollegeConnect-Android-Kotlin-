package com.example.collegeconnect.Student

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Activity.Selectrole
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityStudentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class student_settings : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding : ActivityStudentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_settings)

        changeStatusBarColor()
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_student_settings)

        binding.logoutButton.setOnClickListener {
            logoutstudent()

        }
        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this,Student_dashboard::class.java))
        }
        binding.changelang.setOnClickListener {
            showLanguageSelectionDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf("English", "Hindi")
        val languageCodes = arrayOf("en", "hi")

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.change_language))
        builder.setItems(languages) { _, which ->
            setLocale(languageCodes[which])
        }
        builder.show()
    }


    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }

private fun logoutstudent() {

    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()


    val intent = Intent(this, Selectrole::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}


    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }
}