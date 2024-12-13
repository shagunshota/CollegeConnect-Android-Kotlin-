package com.example.collegeconnect.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivitySelectroleBinding
import java.util.Locale

class Selectrole : AppCompatActivity() {

    private lateinit var binding: ActivitySelectroleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_selectrole)

        // Handle clicks for Admin, Teacher, and Student buttons
        binding.admin.setOnClickListener {
            navigateToLoginActivity("Admin")
        }
        binding.teacher.setOnClickListener {
            navigateToLoginActivity("Teacher")
        }
        binding.student.setOnClickListener {
            navigateToLoginActivity("Student")
        }


        binding.changelang.setOnClickListener {
            showLanguageSelectionDialog()
        }

        // Handle insets for immersive UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun navigateToLoginActivity(role: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("role", role)
            apply()
        }

        val intent = Intent(this, Login_Activity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
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

        // Recreate activity to apply the new language
        recreate()
    }
}