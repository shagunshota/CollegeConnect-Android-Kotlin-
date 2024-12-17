package com.example.collegeconnect.Teacher.fragment

import android.app.AlertDialog
import android.content.Intent
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
import com.example.collegeconnect.Models.Student
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.Teacher.teacher_profile
import com.example.collegeconnect.Teacher.teacher_settings
import com.example.collegeconnect.Teacher.teacher_upload
import com.example.collegeconnect.databinding.FragmentTeacherStudentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class teacher_student : Fragment() {
    private lateinit var binding : FragmentTeacherStudentBinding
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

        val binding: FragmentTeacherStudentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_student, container, false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        userAdapter = Student_Adapter(
            studentList,
            { uniqueId -> deleteStudent(uniqueId) },
            { student -> showUpdateDialog(student) }
        )
        recyclerView.adapter = userAdapter
        recyclerView.adapter = userAdapter

        fetchUserData()
        binding.toolbar.setOnClickListener() {
            showDropdownMenu(it)

        }

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


    private fun showUpdateDialog(student: Student) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.studentdata_update, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Update Student")
            .setPositiveButton("Update") { _, _ ->
                val updatedName = dialogView.findViewById<EditText>(R.id.etStudentName).text.toString()
                val updatedEmail = dialogView.findViewById<EditText>(R.id.etStudentEmail).text.toString()
                val updatedNumber = dialogView.findViewById<EditText>(R.id.etStudentNumber).text.toString()


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


        dialogView.findViewById<EditText>(R.id.etStudentName).setText(student.username)
        dialogView.findViewById<EditText>(R.id.etStudentEmail).setText(student.email)
        dialogView.findViewById<EditText>(R.id.etStudentNumber).setText(student.number)
    }


    private fun showDropdownMenu(anchor: android.view.View) {

        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {

                R.id.profile -> {
                    val intent = Intent(requireContext(), teacher_profile::class.java)
                    startActivity(intent)
//
                    true
                }

                R.id.settings -> {
                    val intent = Intent(requireContext(), teacher_settings::class.java)
                    startActivity(intent)
                    true

                }
                R.id.update_Profile -> {
                    val intent = Intent(requireContext(), teacher_upload::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
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
            teacher_student().apply {
                arguments = Bundle().apply {

                }
            }
    }
}

