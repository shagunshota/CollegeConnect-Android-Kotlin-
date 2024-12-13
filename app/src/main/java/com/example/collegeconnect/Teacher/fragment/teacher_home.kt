package com.example.collegeconnect.Teacher.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.Teacher.teacher_profile
import com.example.collegeconnect.Teacher.teacher_settings
import com.example.collegeconnect.Teacher.teacher_upload
import com.example.collegeconnect.databinding.FragmentTeacherHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class teacher_home : Fragment() {
    private var _binding: FragmentTeacherHomeBinding?=null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_home, container, false)
        countStudent()

        auth = FirebaseAuth.getInstance()
        countNotifications()
        countEvent()

        binding.toolbar.setOnClickListener() {
            showDropdownMenu(it)
        }
        binding.stuadd.setOnClickListener {
            val intent = Intent(requireContext(),Student_Registration::class.java)
            startActivity(intent)
            true
        }
        binding.attenadd.setOnClickListener {
            attendancedialog()

        }
        binding.marksadd.setOnClickListener {
            marksdialog()
        }



        binding.lifecycleOwner = viewLifecycleOwner

        val userId = auth.currentUser?.uid
        if(userId!=null){
            val database = FirebaseDatabase.getInstance().getReference("Teacher").child(userId)
            database.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.getValue(Users::class.java)
                    if(user!=null){
                        binding.welcomeTextView.text="Welcome,${user.username}!"
                        binding.subject.text="${user.uniqueId}"
                    }
                    else{
                        Toast.makeText(requireContext(), "User not found in database", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),"Failed to load data",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(requireContext(),"User not logged in",Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(),Login_Activity::class.java)
            startActivity(intent)

        }


        return binding.root

    }


    private fun marksdialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.marks_input, null)
        val subject =dialogView.findViewById<EditText>(R.id.subtext)
        val rollno = dialogView.findViewById<EditText>(R.id.rolltext)
        val obtainmarks = dialogView.findViewById<EditText>(R.id.obtaimarks)
        val totalmarks = dialogView.findViewById<EditText>(R.id.totalmarks)
        val add = dialogView.findViewById<Button>(R.id.add)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        add.setOnClickListener {
            val subject = subject.text.toString()
            val rollno = rollno.text.toString()
            val obtainmarks = obtainmarks.text.toString()
            val totalmarks = totalmarks.text.toString()
            if ((subject.isNotEmpty())  && (rollno.isNotEmpty()) &&(obtainmarks.isNotEmpty()) &&(totalmarks.isNotEmpty())) {
                addmarks(subject,totalmarks,rollno,obtainmarks)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

    }

    private fun addmarks(subject: String, totalmarks: String, rollno: String, obtainmarks: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Marks")


        val key = attendanceRef.push().key


        val attendanceData = mapOf(
            "subject" to subject,
            "rollNumber" to rollno,
            "obtainedmarks" to obtainmarks,
            "totalmarks" to totalmarks
        )

        if (key != null) {
            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Attendance added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Error generating key", Toast.LENGTH_SHORT).show()
        }

    }


    private fun attendancedialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.attendence_input, null)
        val subject =dialogView.findViewById<EditText>(R.id.subtext)
        val rollno = dialogView.findViewById<EditText>(R.id.rolltext)
        val attendlec = dialogView.findViewById<EditText>(R.id.attendtext)
        val totallec = dialogView.findViewById<EditText>(R.id.totallec)
        val add = dialogView.findViewById<Button>(R.id.add)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        add.setOnClickListener {
            val subject = subject.text.toString()
            val rollno = rollno.text.toString()
            val attenlec = attendlec.text.toString()
            val totallec = totallec.text.toString()
            if ((subject.isNotEmpty())  && (rollno.isNotEmpty()) &&(attenlec.isNotEmpty()) &&(totallec.isNotEmpty())) {
                addattendence(subject,attenlec,rollno,totallec)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

    }

    private fun addattendence(subject: String, attenlec: String, rollno: String, totallec: String) {

            val database = FirebaseDatabase.getInstance()
            val attendanceRef = database.getReference("Attendance")


            val key = attendanceRef.push().key


            val attendanceData = mapOf(
                "subject" to subject,
                "rollNumber" to rollno,
                "attendedLectures" to attenlec,
                "totalLectures" to totallec
            )

            if (key != null) {
                attendanceRef.child(key).setValue(attendanceData)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Attendance added successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Error generating key", Toast.LENGTH_SHORT).show()
            }
        }




    private fun countNotifications() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Notification")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.nonoti.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun countEvent() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Event")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.noeve.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }



    private fun countStudent() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Student")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.nostu.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    data class Users(
        val username: String = "",
        val email: String = "",
        val subject : String =" ",
        val uniqueId : String
    ) {
        constructor() : this("", "","","")
    }
    private fun showDropdownMenu(anchor: android.view.View) {

        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.options, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {

                R.id.profile -> {
                    val intent = Intent(requireContext(),teacher_profile::class.java)
                    startActivity(intent)

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


    companion object {

        fun newInstance(param1: String, param2: String) =
            teacher_home().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
