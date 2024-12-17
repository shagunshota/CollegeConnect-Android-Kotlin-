package com.example.collegeconnect.Admin.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Adapter.Student_Adapter
import com.example.collegeconnect.Admin.add_fee
import com.example.collegeconnect.Admin.admin_profile
import com.example.collegeconnect.Admin.admin_settings
import com.example.collegeconnect.Models.Student
import com.example.collegeconnect.R
import com.example.collegeconnect.R.id.etStudentName
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.databinding.FragmentAdminStudentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class admin_student : Fragment() {
    private  var _binding : FragmentAdminStudentBinding?=null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Student_Adapter
    private val studentList = mutableListOf<Student>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_student, container, false)
        checkNetworkStatus()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        userAdapter = Student_Adapter(
            studentList,
            { uniqueId -> deleteStudent(uniqueId) },
            { student -> showUpdateDialog(student) }
        )
        recyclerView.adapter = userAdapter


        fetchUserData()
        return binding.root
    }

    private fun deleteStudent(uniqueId: String) {
        val database = FirebaseDatabase.getInstance()
        val dataRef = database.getReference("Student").child(uniqueId)

        dataRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Student deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateStudent(student: Student, updatedValues: Map<String, Any>) {
        val database = FirebaseDatabase.getInstance()
        val dataRef = database.getReference("Student").child(student.uniqueId)

        dataRef.updateChildren(updatedValues)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Student updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(student: Student) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.update_studentdata, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Update Student")
            .setPositiveButton("Update") { _, _ ->
                val updatedName = dialogView.findViewById<EditText>(R.id.etname).text.toString()
                val updatedEmail = dialogView.findViewById<EditText>(R.id.etemail).text.toString()
                val updatedNumber = dialogView.findViewById<EditText>(R.id.etnumber).text.toString()


                val updatedValues = mapOf(
                    "username" to updatedName,
                    "email" to updatedEmail,
                    "number" to updatedNumber
                )


                updateStudent(student, updatedValues)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()


        dialogView.findViewById<EditText>(R.id.etname).setText(student.username)
        dialogView.findViewById<EditText>(R.id.etemail).setText(student.email)
        dialogView.findViewById<EditText>(R.id.etnumber).setText(student.number)
    }


    private fun checkNetworkStatus(): Boolean {
        if (!isNetworkConnected(requireContext())) {
            Snackbar.make(requireView(), "No internet connection available.", Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }




    private fun fetchUserData() {
        FirebaseDatabase.getInstance().getReference("Student")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    studentList.clear()

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Student::class.java)
                        if (user != null) {
                            studentList.add(user)
                        }
                    }

                    userAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }



    companion object {

        fun newInstance(param1: String, param2: String) =
            admin_student().apply {
                arguments = Bundle().apply {

                }
            }
    }
}









