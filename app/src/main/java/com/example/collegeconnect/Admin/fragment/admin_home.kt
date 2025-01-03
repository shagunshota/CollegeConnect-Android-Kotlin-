package com.example.collegeconnect.Admin.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat.getColor
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.add_fee
import com.example.collegeconnect.Admin.admin_profile
import com.example.collegeconnect.Admin.admin_settings
import com.example.collegeconnect.Admin.admin_update
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.Superadmin.fragment.supad_home.NotificationEvent
import com.example.collegeconnect.Teacher.Teacher_registration
import com.example.collegeconnect.Teacher.fragment.teacher_home
import com.example.collegeconnect.databinding.FragmentAdminHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID
class admin_home : Fragment() {
    private lateinit var binding : FragmentAdminHomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_admin_home,container,false)

        auth = FirebaseAuth.getInstance()
        fetchAdminDetails()
        countStudent()
        countTeacher()
        checkNetworkStatus()
//        binding.addTeacher.setOnClickListener {
//           openFragment(add_teacher())
//               val fragmentManager = requireActivity().supportFragmentManager
//                   fragmentManager.beginTransaction()
//               .replace(R.id.fragment_container, add_teacher.newInstance())
//               .commit()
//
//        }
//        binding.addStudent.setOnClickListener {
//            openFragment(add_Student())
//            val fragmentManager = requireActivity().supportFragmentManager
//            fragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, add_Student.newInstance())
//                .commit()
//        }
//        binding.addNoti.setOnClickListener {
//            notificationdialog()
//
//        }
//        binding.addEvent.setOnClickListener {
//            eventdialog()
//        }
        binding.add.setOnClickListener { anchor ->
            additionalMenu(anchor)

        }



        return binding.root
    }


//
    private fun additionalMenu(anchor: View) {

        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.admin_add, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.add_fee -> {
                     openFragment(add_fees())
                    true
                }
                R.id.add_teacher -> {
                    openFragment(add_teacher())
                    true
                }
                R.id.add_student -> {
                   openFragment(add_Student())
                    true
                }
                R.id.add_noti -> {
                    openFragment(add_notification())
                    true
                }
                R.id.add_event -> {
                    openFragment(add_event())
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }


    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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

    data class Admins(
        val username: String = "",
        val email: String = "",
        val uniqueId: String = "")



    private fun notificationdialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.notification_input, null)
        val title = dialogView.findViewById<EditText>(R.id.noti_title)
        val description = dialogView.findViewById<EditText>(R.id.noti_des)
        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        submitButton.setOnClickListener {
            val title = title.text.toString()
            val description = description.text.toString()
            if ((title.isNotEmpty())  && (description.isNotEmpty())) {
                addnotification(title,description)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

    }
    private fun addnotification(title: String, description: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Notification")


        val key = attendanceRef.push().key


        val attendanceData = mapOf(
           "title" to title,
            "description" to description
        )

        if (key != null) {
            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Notification added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Error generating key", Toast.LENGTH_SHORT).show()
        }


    }

    private fun eventdialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.event_input, null)

        val title = dialogView.findViewById<EditText>(R.id.event_title)
        val venue = dialogView.findViewById<EditText>(R.id.event_venue)
        val timeing =dialogView.findViewById<EditText>(R.id.event_time)
        val description = dialogView.findViewById<EditText>(R.id.event_desc)
        val submitButton = dialogView.findViewById<Button>(R.id.submitButton)


        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()


        submitButton.setOnClickListener {
            val title = title.text.toString()
            val venue = venue.text.toString()
            val timing=timeing.text.toString()
            val description = description.text.toString()
            if ((timing.isNotEmpty()) &&(title.isNotEmpty()) && (venue.isNotEmpty()) && (description.isNotEmpty())) {

                addEvent(title,venue,timing,description)

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()

    }

    private fun addEvent(title: String, venue: String, timing: String, description: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Event")


        val key = attendanceRef.push().key


        val attendanceData = mapOf(
            "title" to title,
            "description" to description,
            "venue" to venue,
            "timing" to timing
        )

        if (key != null) {
            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Event added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Error generating key", Toast.LENGTH_SHORT).show()
        }
    }



    private fun fetchAdminDetails() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance().getReference("Admin").child(userId)
            database.get().addOnCompleteListener { task ->
                if (task.isSuccessful && isAdded) { // Check isAdded to ensure the fragment is attached
                    val admin = task.result.getValue(Admins::class.java)
                    if (admin != null) {
                        binding.welcomeTextView.text = "Welcome, Admin !"
                        binding.admin.text = admin.uniqueId
                    } else {
                        if (isAdded) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.user_with_email),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            if (isAdded) {
                Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), Login_Activity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun countTeacher() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Teacher")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    val count = snapshot.childrenCount
                    binding.teachercount.text = " $count"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) { Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun countStudent() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Student")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    val count = snapshot.childrenCount
                    binding.studentcount.text = " $count"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                if (isAdded) {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    companion object {
        fun newInstance() = admin_home().apply {
            arguments = Bundle()
        }
    }
}

