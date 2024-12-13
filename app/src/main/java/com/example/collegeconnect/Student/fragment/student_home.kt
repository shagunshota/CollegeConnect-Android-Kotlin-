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
import com.example.collegeconnect.Adapter.EventAdapter
import com.example.collegeconnect.Adapter.NotificationAdapter
import com.example.collegeconnect.Login.Login_Activity
import com.example.collegeconnect.Models.Event
import com.example.collegeconnect.Models.Users
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_fee
import com.example.collegeconnect.Student.student_profile
import com.example.collegeconnect.Student.student_settings
import com.example.collegeconnect.Student.student_upload
import com.example.collegeconnect.databinding.FragmentStudentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class student_home : Fragment() {
    private lateinit var binding :FragmentStudentHomeBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentStudentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_home, container, false)

        checkNetworkStatus()
        loadNotificationData()
        loadEventData()


        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not logged in. Please log in first.", Toast.LENGTH_SHORT).show()
            return binding.root
        }


        binding.fee.setOnClickListener{
            val intent = Intent(requireContext(), Student_fee::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        binding.toolbar.setOnClickListener {
            showDropdownMenu(it)
        }


        if(userId!=null){
            val database = FirebaseDatabase.getInstance().getReference("Student").child(userId)
            database.get().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val user = task.result.getValue(Users::class.java)
                    if(user!=null){
                        binding.welcomeTextView.text="Welcome,${user.username}!"
                        binding.studentid.text= user.rollno
                        binding.stubranch.text= user.branch
                    }
                    else{
                        Toast.makeText(requireContext(), "User not found in database", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(),"Failed to load data",Toast.LENGTH_SHORT).show()
                } }
        } else{
            Toast.makeText(requireContext(),"User not logged in",Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(),Login_Activity::class.java)
            startActivity(intent)
        }



        return binding.root
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


    private fun loadNotificationData() {
        val database = FirebaseDatabase.getInstance()
        val notificationRef = database.getReference("Notification")
        val notificationList = mutableListOf<com.example.collegeconnect.Models.Notification>()
        notificationRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notificationList.clear()
                for (notificationSnapshot in snapshot.children) {
                    val notification = notificationSnapshot.getValue(com.example.collegeconnect.Models.Notification::class.java)
                    if (notification != null) {
                        notificationList.add(notification)
                    }
                }
                view?.let { fragmentView ->
                    val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.rvNoti)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = NotificationAdapter(notificationList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun loadEventData() {
        val database = FirebaseDatabase.getInstance()
        val eventref = database.getReference("Event")
        val eventList = mutableListOf(Event())
        eventref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                eventList.clear()
                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(com.example.collegeconnect.Models.Event::class.java)
                    if (event!= null) {
                        eventList.add(event)
                    }
                }
                view?.let { fragmentView ->
                    val recyclerView = fragmentView.findViewById<RecyclerView>(R.id.rv_event)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = EventAdapter(eventList)
                }
            }
            override fun onCancelled(error: DatabaseError) {

                if (isAdded) {
                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }







    companion object {

        fun newInstance() =
            student_home().apply {
                arguments = Bundle().apply {

                }
            }
    }
}






