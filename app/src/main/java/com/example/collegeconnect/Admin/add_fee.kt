package com.example.collegeconnect.Admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityAddFeeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class add_fee : AppCompatActivity() {
    private lateinit var binding : ActivityAddFeeBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_fee)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_fee)
        changeStatusBarColor()
        auth = FirebaseAuth.getInstance()


        binding.etsem.setOnClickListener {
            semDropdown(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this, Admin_dashboard::class.java))
        }

        binding.add.setOnClickListener {
            val sem = binding.etsem.text.toString()
            val amount = binding.etamount.text.toString()
            if (areFieldsValid()) {
                addfee(sem,amount)
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun semDropdown(view: View?) {
        if (view == null) return
        val popupMenu = PopupMenu(this, view)
        val options = resources.getStringArray(R.array.select_sem)
        options.forEach { option ->
            popupMenu.menu.add(option)
        }


        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etsem.setText(menuItem.title)
            binding.etsem.error = null
            true
        }

        popupMenu.show() // Show the menu
    }


    private fun addfee(sem: String, amount: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Fee")


        val key = attendanceRef.push().key


        val attendanceData = mapOf(
           "sem" to sem,
            "amount" to amount
        )

        if (key != null) {
            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Fee added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error generating key", Toast.LENGTH_SHORT).show()
        }
    }


    private fun areFieldsValid(): Boolean {

        var isValid = true

        if (binding.etsem.text.isNullOrEmpty()) {
            binding.sem.error = getString(R.string.empty_semester)
            isValid = false
        }
        else {
            binding.etsem.error = null
        }


        if (TextUtils.isEmpty(binding.etamount.text.toString().trim())) {
            binding.amount.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.amount.error=null
        }
      return  isValid

    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.lightblue)

    }
}