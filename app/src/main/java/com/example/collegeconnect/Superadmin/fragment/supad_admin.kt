package com.example.collegeconnect.Superadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Adapter.Admin_Adapter
import com.example.collegeconnect.Adapter.Student_Adapter
import com.example.collegeconnect.Admin.Admin_register
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.databinding.FragmentSupadAdminBinding
import com.example.collegeconnect.databinding.FragmentSupadHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class supad_admin : Fragment() {
    private lateinit var binding :FragmentSupadAdminBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Admin_Adapter
    private val adminList = mutableListOf<Admin_register.User>()
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val  binding : FragmentSupadAdminBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_supad_admin,container,false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
         val email = binding.itemuser.emailTextView.text.toString()


        userAdapter = Admin_Adapter(adminList)
        recyclerView.adapter = userAdapter


        fetchAdminData()


        return  binding.root

    }


    private fun fetchAdminData() {
        FirebaseDatabase.getInstance().getReference("Admin")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    adminList.clear() // Clear the list before adding new data

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Admin_register.User::class.java)
                        if (user != null) {
                            adminList.add(user) // Add user data to the list
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
            supad_admin().apply {
                arguments = Bundle().apply {

                }
            }
    }
}





//
//class SupadAdminFragment : Fragment() {
//
//    private lateinit var binding: FragmentSupadAdminBinding
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adminAdapter: Admin_Adapter
//    private val adminList = mutableListOf<Admin_register.User>()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Perform any additional setup if needed
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_supad_admin, container, false)
//
//        recyclerView = binding.recyclerView
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        adminAdapter = Admin_Adapter(adminList) { user ->
//            deleteUserFromDatabase(user)
//        }
//        recyclerView.adapter = adminAdapter
//
//        fetchAdminData()
//
//        return binding.root
//    }
//
//    private fun deleteUserFromDatabase(user: Admin_register.User) {
//        FirebaseDatabase.getInstance().getReference("Admin")
//            .child(user.userId) // Assuming userId is the key for this user
//            .removeValue()
//            .addOnSuccessListener {
//                Toast.makeText(requireContext(), "User deleted successfully", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(requireContext(), "Error deleting user: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun fetchAdminData() {
//        FirebaseDatabase.getInstance().getReference("Admin")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    adminList.clear() // Clear the list before adding new data
//
//                    for (userSnapshot in snapshot.children) {
//                        val user = userSnapshot.getValue(Admin_register.User::class.java)
//                        if (user != null) {
//                            user.userId = userSnapshot.key ?: "" // Set userId from Firebase key
//                            adminList.add(user)
//                        }
//                    }
//
//                    adminAdapter.notifyDataSetChanged()
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//
//    companion object {
//        fun newInstance(param1: String, param2: String) =
//            SupadAdminFragment().apply {
//                arguments = Bundle().apply {
//                    // Pass any parameters if needed
//                }
//            }
//    }
//}
