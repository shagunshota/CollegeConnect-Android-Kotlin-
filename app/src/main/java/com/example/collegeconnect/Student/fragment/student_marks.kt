package com.example.collegeconnect.Student.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
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
import com.example.collegeconnect.Adapter.MarksAdapter
import com.example.collegeconnect.Models.Attendence
import com.example.collegeconnect.Models.Marks
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.student_profile
import com.example.collegeconnect.Student.student_settings
import com.example.collegeconnect.Student.student_upload
import com.example.collegeconnect.databinding.FragmentStudentMarksBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class student_marks : Fragment() {
    private lateinit var binding: FragmentStudentMarksBinding
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
        val binding: FragmentStudentMarksBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_marks, container, false)
        checkNetworkStatus()
        loadMarksData()
        auth = FirebaseAuth.getInstance()
        binding.marksToolbar.setOnClickListener {
            showDropdownMenu(it)
        }



        return binding.root
    }

    private fun loadMarksData() {
       val database = FirebaseDatabase.getInstance()
        val marksref = database.getReference("Marks")
        val marksList = mutableListOf<Marks>()
        marksref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               marksList.clear()
                for (marksSnapshot in snapshot.children) {
                    val marks = marksSnapshot.getValue(Marks::class.java)
                    if (marks != null) {
                        marksList.add(marks)
                    }
                }
                view?.let { fragmentView ->
                    val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.marks_recycle)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = MarksAdapter(marksList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
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
    companion object {

        fun newInstance(param1: String, param2: String) =
            student_marks().apply {
                arguments = Bundle().apply {

                }
            }
    }
}