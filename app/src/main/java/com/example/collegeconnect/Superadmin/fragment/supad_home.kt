package com.example.collegeconnect.Superadmin.fragment

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.Admin_register
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.Superadmin.supad_profile
import com.example.collegeconnect.Superadmin.supad_settings
import com.example.collegeconnect.Teacher.Teacher_registration
import com.example.collegeconnect.databinding.FragmentSupadHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class supad_home: Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSupadHomeBinding
    private lateinit var title : String
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_supad_home, container, false)


        countStudent()
        countTeacher()
        countAdmin()


        binding.superadtoolbar.setOnClickListener {
            showDropdownMenu(it)
        }
        binding.addadmin.setOnClickListener{
            val intent = Intent(requireContext(),Admin_register::class.java)
            startActivity(intent)
        }
        binding.addstudent.setOnClickListener{
            val intent= Intent(requireContext(),Student_Registration::class.java)
            startActivity(intent)
        }
        binding.addteacher.setOnClickListener{
            val intent = Intent(requireContext(),Teacher_registration::class.java)
            startActivity(intent)
        }
        binding.addnotification.setOnClickListener {
            notificationdialog()

        }
        binding.addevent.setOnClickListener {
           eventdialog()
        }
        binding.addfee.setOnClickListener {
            feedialog()
        }

        return binding.root
    }


    private fun feedialog() {
        val dialogfee = LayoutInflater.from(requireContext()).inflate(R.layout.fee_input,null)
        val sem = dialogfee.findViewById<EditText>(R.id.fee_sem)
        val amou = dialogfee.findViewById<EditText>(R.id.fee_amount)

        val addbutton = dialogfee.findViewById<Button>(R.id.addbutton)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogfee)
            .create()

        addbutton.setOnClickListener {
            val semester = sem.text.toString()
            val amount = amou.text.toString()
            if ((semester.isNotEmpty())&&(amount.isNotEmpty())){
//                addfee(semester,amount)
                dialog.dismiss()
            }
            else{
                Toast.makeText(requireContext(),getString(R.string.enter_data),Toast.LENGTH_SHORT).show()
            }

        }
        dialog.show()
    }


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
//                addnotification(title,description)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()

    }
//    private fun addfee(semester: String, amount: String) {
//        val userId = auth.currentUser?.uid
//        if (userId == null) {
//            Toast.makeText(requireContext(), "User not logged in. Please log in first.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val feeId = UUID.randomUUID().toString()
//
//        val database = FirebaseDatabase.getInstance().getReference("Student Fee").child(userId).child(feeId)
//
//        val fee = Fee(semester,amount)
//
//
//        database.setValue(fee).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(requireContext(), getString(R.string.registration_sucess), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), getString(R.string.error_saving_user_data), Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    }


//    private fun addnotification(title: String, description: String) {
//        val userId = auth.currentUser?.uid
//        if (userId == null) {
//            Toast.makeText(requireContext(), "User not logged in. Please log in first.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val notificationId = UUID.randomUUID().toString()
//
//        val database = FirebaseDatabase.getInstance().getReference("Notification").child(userId).child(notificationId)
//
//        val notification = Notification(title, description)
//
//
//        database.setValue(notification).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(requireContext(), getString(R.string.registration_sucess), Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), getString(R.string.error_saving_user_data), Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//    }



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
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in. Please log in first.", Toast.LENGTH_SHORT).show()
            return
        }
        val database = FirebaseDatabase.getInstance().getReference("Events").child(userId)

        val notification = NotificationEvent(timing,venue, title, description)
        database.push().setValue(notification)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), getString(R.string.sucess_event), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.fail_event), Toast.LENGTH_SHORT).show()
                }
            }
    }


    data class NotificationEvent(
        val timing: String =" ",
        val venue: String =" ",
        val title: String = " ",
        val description: String = " "
    )




    private fun showDropdownMenu(anchor: View) {
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.superad_option, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    val intent = Intent(requireContext(), supad_profile::class.java)
                    startActivity(intent)
                    true
                }
                R.id.settings -> {
                    val intent = Intent(requireContext(), supad_settings::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun countStudent() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Student")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.studentcount.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun countTeacher() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Teacher")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.teachercount.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun countAdmin() {
        val database = FirebaseDatabase.getInstance()
        val tableRef = database.getReference("Admin")
        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount
                binding.admincount.text = count.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            supad_home().apply {
                arguments = Bundle().apply {
                    putString("PARAM1", param1)
                    putString("PARAM2", param2)
                }
            }
    }
}
