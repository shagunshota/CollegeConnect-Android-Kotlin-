package com.example.collegeconnect.Admin


import com.google.firebase.database.*
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.appcompat.app.AlertDialog
import com.example.collegeconnect.Admin.fragment.admin_home


class admin_profile : AppCompatActivity() {
    private lateinit var binding : ActivityAdminProfileBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_profile)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_admin_profile)
        auth = FirebaseAuth.getInstance()
        changeStatusBarColor()
        fetchAdminDetails()

        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Admin_dashboard::class.java))

        }


        binding.clpass.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val adminRef = database.getReference("Admin")
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val database = FirebaseDatabase.getInstance().getReference("Admin").child(userId)
                database.get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val admin = task.result.getValue(Admins::class.java)
                        if (admin != null) {
                            val password = "${admin.password}"
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Admin Password")
                            builder.setMessage( "$password")
                            builder.setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            builder.show()

                        } else {
                            Toast.makeText(
                                this,
                                getString(R.string.user_with_email),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }

        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun fetchAdminDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("Admin").child(userId)
            database.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val admin = task.result.getValue(Admins::class.java)
                    if (admin != null) {
                        binding.name.text = "${admin.username}"

                        binding.email.text="${admin.email}"

                        binding.uniqueid.text="${admin.uniqueId}"
                     
                    }
                }
            }
        }
    }
    data class Admins(
        val username: String = "",
        val password : String=" ",
        val email: String = "",
        val uniqueId: String = "",
        val subject : String =" ",
        val branch : String =" ",
        val number: String =" "
    )

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }
}