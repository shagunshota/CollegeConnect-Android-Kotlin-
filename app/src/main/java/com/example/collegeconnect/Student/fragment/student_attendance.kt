package com.example.collegeconnect.Student.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Adapter.AttendenceAdapter
import com.example.collegeconnect.Adapter.FeeAdapter
import com.example.collegeconnect.Adapter.NotificationAdapter
import com.example.collegeconnect.Models.Attendence
import com.example.collegeconnect.Models.Fee
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.student_profile
import com.example.collegeconnect.Student.student_settings
import com.example.collegeconnect.Student.student_upload
import com.example.collegeconnect.databinding.FragmentStudentAttendanceBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class student_attendance : Fragment() {
    private lateinit var binding : FragmentStudentAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        val binding: FragmentStudentAttendanceBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_student_attendance,container,false)
        checkNetworkStatus()
        loadattendencedata()
        binding.attendenceToolbar.setOnClickListener {
            showDropdownMenu(it)
        }
        return binding.root
    }

    private fun loadattendencedata() {
       val database= FirebaseDatabase.getInstance()
        val attendref = database.getReference("Attendance")
        val attendenceList = mutableListOf<Attendence>()
        attendref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                attendenceList.clear()
                for (attendenceSnapshot in snapshot.children) {
                    val attendence = attendenceSnapshot.getValue(Attendence::class.java)
                    if (attendence != null) {
                        attendenceList.add(attendence)
                    }
                }
                view?.let { fragmentView ->
                    val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.attendence_recyle)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = AttendenceAdapter(attendenceList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })


    }

    private fun showDropdownMenu(anchor: android.view.View) {
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.options, popupMenu.menu)


        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {

                R.id.profile -> {
                    val intent = Intent(requireContext(), student_profile::class.java)
                    startActivity(intent)

                    true
                }

                R.id.settings -> {
                    val intent = Intent(requireContext(), student_settings::class.java)
                    startActivity(intent)
                    true

                }
                R.id.update_Profile -> {
                    val intent = Intent(requireContext(), student_upload::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        popupMenu.show()

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

    companion object {

        fun newInstance() =
            student_attendance().apply {
                arguments = Bundle().apply {

                }
            }
    }
}