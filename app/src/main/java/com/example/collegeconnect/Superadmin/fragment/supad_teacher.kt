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
import com.example.collegeconnect.Adapter.Teacher_Adapter
import com.example.collegeconnect.Models.Teacher
import com.example.collegeconnect.R
import com.example.collegeconnect.Teacher.Teacher_registration
import com.example.collegeconnect.databinding.FragmentSupadTeacherBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class supad_teacher : Fragment() {
    private lateinit var binding: FragmentSupadTeacherBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Teacher_Adapter
    private val teacherList = mutableListOf<Teacher>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding : FragmentSupadTeacherBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_supad_teacher,container,false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        fetchTeacherData()

        return  binding.root
    }

    private fun fetchTeacherData() {
        FirebaseDatabase.getInstance().getReference("Teacher")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    teacherList.clear() // Clear the list before adding new data

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

        fun newInstance() =
            supad_teacher().apply {
                arguments = Bundle().apply {

                }
            }
    }
}