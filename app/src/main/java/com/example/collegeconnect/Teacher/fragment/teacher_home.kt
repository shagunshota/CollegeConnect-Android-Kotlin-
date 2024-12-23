package com.example.collegeconnect.Teacher.fragment

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
import com.example.collegeconnect.Models.Teacher
import com.example.collegeconnect.Models.Users
import com.example.collegeconnect.R
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
    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher_home, container, false)

        auth = FirebaseAuth.getInstance()
        countStudent()

        setupListeners()

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
    data class Users(
        val username: String,
        val email: String ,
        val number: String ,
        val branch : String,
        val subject: String,
        val gender: String,
        val experience: String,
        val password: String,
        val uniqueId: String,
        val userId: String
    ) {
        constructor() : this("","","","","","","", "","","")  // No-argument constructor
    }

    private fun setupListeners() {

        binding.stuadd.setOnClickListener {
           requireActivity().supportFragmentManager.beginTransaction()
               .replace(R.id.fragment_container_teacher,addstudent_teacher())
               .commit()
        }
        binding.attenadd.setOnClickListener {
            attendancedialog()
        }
        binding.marksadd.setOnClickListener {
            marksdialog()
        }
    }

    private fun fetchUserData(userId: String) {
        val database = FirebaseDatabase.getInstance().getReference("Teacher").child(userId)
        database.get().addOnCompleteListener { task ->
            if (isAdded) {
                if (task.isSuccessful) {
                    val user = task.result.getValue(Teacher::class.java)
                    if (user != null) {
                        binding.welcomeTextView.text = "Welcome, ${user.username}!"
                        binding.subject.text = user.uniqueId
                    } else {
                        showToast("User not found in database")
                    }
                } else {
                    showToast("Failed to load data")
                }
            }
        }
    }
    private fun countStudent() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Student")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.nostu.text = count.toString() // Ensure `countTextView` is present in XML
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
//    private fun getTeacherUsername(userId: String) {
//        // Reference to the "Teacher" node in Firebase
//        val database = FirebaseDatabase.getInstance().getReference("Teacher").child(userId)
//
//        // Retrieve the data for the teacher
//        database.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // On success, get the teacher object
//                val teacher = task.result?.getValue(Teacher::class.java)
//
//                // Check if teacher data is not null
//                if (teacher != null) {
//                    // Access the username (or any other field)
//                    val username = teacher.username
//                    Toast.makeText(requireContext(), "Username: $username", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(requireContext(), "Teacher data not found", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                // If there's an error retrieving the data
//                Toast.makeText(requireContext(), "Error retrieving teacher data: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    //    data class Tea(
//        val username: String = "",
//        val email: String = "",
//        val subject : String =" ",
//        val uniqueId : String,
//        val userId: String
//    ) {
//        constructor() : this("", "","","","")
//    }
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

    private fun navigateToLogin() {
        showToast("User not logged in")
        val intent = Intent(requireContext(), Login_Activity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openFragment(fragment: Fragment) {
        if (isAdded) {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_teacher, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun marksdialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.marks_input, null)
        val subjectField = dialogView.findViewById<EditText>(R.id.subtext)
        val rollnoField = dialogView.findViewById<EditText>(R.id.rolltext)
        val obtainmarksField = dialogView.findViewById<EditText>(R.id.obtaimarks)
        val totalmarksField = dialogView.findViewById<EditText>(R.id.totalmarks)
        val addButton = dialogView.findViewById<Button>(R.id.add)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        addButton.setOnClickListener {
            val subject = subjectField.text.toString()
            val rollno = rollnoField.text.toString()
            val obtainmarks = obtainmarksField.text.toString()
            val totalmarks = totalmarksField.text.toString()

            if (subject.isNotEmpty() && rollno.isNotEmpty() && obtainmarks.isNotEmpty() && totalmarks.isNotEmpty()) {
                addmarks(subject, totalmarks, rollno, obtainmarks)
                dialog.dismiss()
            } else {
                showToast("Please enter some data")
            }
        }

        dialog.show()
    }

    private fun addmarks(subject: String, totalmarks: String, rollno: String, obtainmarks: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Marks")

        val key = attendanceRef.push().key
        if (key != null) {
            val attendanceData = mapOf(
                "subject" to subject,
                "rollNumber" to rollno,
                "obtainedmarks" to obtainmarks,
                "totalmarks" to totalmarks
            )

            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener { showToast("Marks added successfully") }
                .addOnFailureListener { e -> showToast("Failed to add marks: ${e.message}") }
        } else {
            showToast("Error generating key")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = teacher_home()
    }
}
