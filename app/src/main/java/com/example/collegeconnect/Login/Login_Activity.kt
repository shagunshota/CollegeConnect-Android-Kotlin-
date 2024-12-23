package com.example.collegeconnect.Login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Activity.Role
import com.example.collegeconnect.Activity.Selectrole
import com.example.collegeconnect.Admin.Admin_dashboard
import com.example.collegeconnect.Admin.Admin_register
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.Student.Student_dashboard
import com.example.collegeconnect.Superadmin.superadmin_dashboard
import com.example.collegeconnect.Teacher.Teacher_dashboard
import com.example.collegeconnect.Teacher.Teacher_registration
import com.example.collegeconnect.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Login_Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        database = FirebaseDatabase.getInstance().reference

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        val role = intent.getStringExtra("role")
        binding.back.setOnClickListener {
            startActivity(Intent(this,Selectrole::class.java))
            finish()
        }

        setupUI()
        performloginaction()
        checkNetworkStatus()
        changeStatusBarColor()



        binding.register.setOnClickListener {

            val role = intent.getStringExtra("role")

            when (role) {
               "Admin" -> {
                   Toast.makeText(this,getString(R.string.admin_cannot_register),Toast.LENGTH_SHORT).show()

                }
               "Teacher" -> {
                    startActivity(Intent(this, Teacher_registration::class.java))
                }
                "Student" -> {
                    startActivity(Intent(this, Student_Registration::class.java))
                }

            }
        }
    }
    private fun checkNetworkStatus(): Boolean {
        if (!isNetworkConnected(this)) {

            Snackbar.make(   findViewById(android.R.id.content),
                "No internet connection available.",
                Snackbar.LENGTH_LONG
            ).show()
        }
        return isNetworkConnected(this)
    }
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }



    private fun performloginaction() {
        val role = intent.getStringExtra("role")
        binding.button.setOnClickListener {
            if (areFieldsValid()) {
                checkNetworkStatus()

                val email = binding.studentemail.text.toString()
                val password = binding.password.text.toString()
                saveUserSession(role!!)
                 loginUser(email,password)
            }
            else{
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val username =binding.studentemail.text.toString().trim()
        val password = binding.password.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val role = intent.getStringExtra("role")

                    when (role) {
                        "Admin" -> {

                                database.child("Admin",).get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val admins = task.result?.children
                                        var isValidUser = false

                                        admins?.forEach { snapshot ->
                                            val adminEmail =
                                                snapshot.child("email").value.toString()
                                            val adminPassword =
                                                snapshot.child("password").value.toString()

                                            if (adminEmail == email && adminPassword== password) {
                                                isValidUser = true
                                                return@forEach
                                            }

                                        }

                                        if (isValidUser) { Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this, Admin_dashboard::class.java))
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Invalid Credentials",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Database Error: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        "Teacher" -> {
                            database.child("Teacher").get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val teachers = task.result?.children
                                    var isValidUser = false

                                    teachers?.forEach { snapshot ->
                                        val teacherEmail = snapshot.child("email").value.toString()
                                        val teacherPassword = snapshot.child("password").value.toString()

                                        if (teacherEmail == email && teacherPassword == password) {
                                            isValidUser = true
                                            return@forEach
                                        }
                                    }

                                    if (isValidUser) {
                                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, Teacher_dashboard::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "Database Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                       "Student" -> {
                            database.child("Student").get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val students = task.result?.children
                                    var isValidUser = false

                                    students?.forEach { snapshot ->
                                        val studentEmail = snapshot.child("email").value.toString()
                                        val studentPassword = snapshot.child("password").value.toString()

                                        if (studentEmail == email && studentPassword == password) {
                                            isValidUser = true
                                            return@forEach
                                        }
                                    }

                                    if (isValidUser) {
                                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this, Student_dashboard::class.java))
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        finish()
                                    } else {
                                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(this, "Database Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }

                }
                else{
                 Toast.makeText(this,"Network Error",Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun saveUserSession(role: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("userRole", role)
            apply()
        }
    }

    private fun setupUI() {

        val noSpaceInputFilter = InputFilter { source, _, _, _, _, _ ->
            if (source.toString().contains(" ")) {

                if (binding.studentemail.hasFocus()) {
                    binding.til.error = getString(R.string.space_email)
                } else if (binding.password.hasFocus()) {
                    binding.pass.error = getString(R.string.space_pass)
                }
                ""
            } else {
                null
            }
        }
        binding.studentemail.filters = arrayOf(noSpaceInputFilter)
        binding.password.filters = arrayOf(noSpaceInputFilter)
        binding.studentemail.addTextChangedListener(createEmailTextWatcher())
        binding.password.addTextChangedListener(createPasswordTextWatcher())


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun createEmailTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.til.error = getString(R.string.valid_email)
                }
                else if (TextUtils.isEmpty(email)) {
                    binding.til.error =getString(R.string.empty_email)

                }
                else
                {
                    binding.til.error = null
                }
            }
        }
    }
    private fun createPasswordTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString().trim()

                if(TextUtils.isEmpty(password)){
                    binding.pass.error=getString(R.string.empty_pass)
                }
                else if ( !isValidPassword(password)) {
                    binding.pass.error = getString(R.string.valid_pass)
                }
                else {
                    binding.pass.error = null
                }
            }
        }
    }


    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }

    private fun areFieldsValid(): Boolean {

        val email = binding.studentemail.text.toString().trim()
        val password = binding.password.text.toString().trim()
        var isValid = true

        if (TextUtils.isEmpty(email)) {
            binding.til.error =getString(R.string.empty_email)
            isValid=false
        }
        else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.til.error = getString(R.string.valid_email)
            isValid = false
        }


        if (TextUtils.isEmpty(password)){
            binding.pass.error=getString(R.string.empty_pass)
            isValid =false
        }
        else if ( !isValidPassword(password)) {
            binding.pass.error = getString(R.string.valid_pass)
            isValid = false
        }

        return isValid
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,12}\$"
        return password.matches(passwordRegex.toRegex())
    }

}