package com.example.collegeconnect.Student
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Adapter.FeeAdapter
import com.example.collegeconnect.Models.Fee
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.ActivityStudentFeeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Student_fee : AppCompatActivity() {
    private lateinit var binding: ActivityStudentFeeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_fee)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_fee)
        changeStatusBarColor()
        loadFeeData()
        binding.maintoolbar.setNavigationOnClickListener {

            startActivity(Intent(this,Student_dashboard::class.java))

        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadFeeData() {
        val database = FirebaseDatabase.getInstance()
        val feeRef = database.getReference("Fee")


        val feeList = mutableListOf<Fee>()

        feeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feeList.clear()


                for (feeSnapshot in snapshot.children) {
                    val fee = feeSnapshot.getValue(Fee::class.java)
                    if (fee != null) {
                        feeList.add(fee)
                    }
                }


                val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this@Student_fee)
                recyclerView.adapter = FeeAdapter(feeList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Student_fee, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun changeStatusBarColor() {
        val window: Window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)

    }

}
