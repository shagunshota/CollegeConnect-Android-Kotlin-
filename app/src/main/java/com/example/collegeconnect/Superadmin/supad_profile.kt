package com.example.collegeconnect.Superadmin

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.admin_profile.Admins
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_dashboard
import com.example.collegeconnect.databinding.ActivitySupadProfileBinding
import com.google.firebase.database.FirebaseDatabase

class supad_profile : AppCompatActivity() {
    private lateinit var binding: ActivitySupadProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_supad_profile)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_supad_profile)
        changeStatusBarColor()

        binding.superadtoolbar.setNavigationOnClickListener {
            startActivity(Intent(this, superadmin_dashboard::class.java))
        }

        binding.clpass.setOnClickListener {

                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Super Admin Password")
                            builder.setMessage( getString(R.string.superad_Pass))
                            builder.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            builder.show()

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