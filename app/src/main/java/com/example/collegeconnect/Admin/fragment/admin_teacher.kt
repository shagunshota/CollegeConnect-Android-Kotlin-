package com.example.collegeconnect.Admin.fragment

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
import com.example.collegeconnect.Adapter.Teacher_Adapter
import com.example.collegeconnect.Admin.add_fee
import com.example.collegeconnect.Admin.admin_profile
import com.example.collegeconnect.Admin.admin_settings
import com.example.collegeconnect.Models.Teacher
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.FragmentAdminTeacherBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class admin_teacher : Fragment() {
   private var _binding : FragmentAdminTeacherBinding?= null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Teacher_Adapter
    private val teacherList = mutableListOf<Teacher>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_teacher, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        checkNetworkStatus()

        binding.menu.setOnClickListener {anchor ->
            if (isAdded) {
                showDropdownMenu(anchor)
            }
        }

        userAdapter = Teacher_Adapter(teacherList) { uniqueId, email, password ->
            deleteItem(uniqueId, email, password)
        }
        recyclerView.adapter = userAdapter

        fetchTeacherData()
        return binding.root

    }


    private fun deleteItem(unique: String , email : String , password: String) {


        val database = FirebaseDatabase.getInstance()
        val dataRef = database.getReference("Teacher").child(unique)

        dataRef.removeValue()
            .addOnSuccessListener {
               val Auth = FirebaseAuth.getInstance()
                val teacher = Auth.currentUser
                if(teacher!=null){
                    val credential = EmailAuthProvider.getCredential(email , password)
                        teacher.reauthenticate(credential)
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(),"teacher deleted successfully",Toast.LENGTH_SHORT).show()
                            }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }



    private fun showDropdownMenu(anchor: View) {
        if (!isAdded) return
        val popupMenu = PopupMenu(requireContext(), anchor)
        popupMenu.menuInflater.inflate(R.menu.admin_option, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(requireContext(), admin_profile::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(requireContext(), admin_settings::class.java))
                    true
                }
                R.id.add_fee -> {
                    startActivity(Intent(requireContext(), add_fee::class.java))
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

    private fun fetchTeacherData() {
        FirebaseDatabase.getInstance().getReference("Teacher")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    teacherList.clear() 

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Teacher::class.java)
                        if (user != null) {
                           teacherList.add(user)
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
            admin_teacher().apply {
                arguments = Bundle().apply {

                }
            }
    }
}