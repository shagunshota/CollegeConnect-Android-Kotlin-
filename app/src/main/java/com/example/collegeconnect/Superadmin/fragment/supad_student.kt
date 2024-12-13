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
import com.example.collegeconnect.Adapter.Student_Adapter
import com.example.collegeconnect.Models.Student
import com.example.collegeconnect.R
import com.example.collegeconnect.Student.Student_Registration
import com.example.collegeconnect.databinding.FragmentSupadStudentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class supad_student : Fragment() {
    private lateinit var binding :FragmentSupadStudentBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Student_Adapter
    private val studentList = mutableListOf<Student>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSupadStudentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_supad_student,container,false)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


//        userAdapter = Student_Adapter(studentList) { userId ->
//            DeleteUser(userId)
//        }
        recyclerView.adapter = userAdapter


        fetchstudentData()


        return   binding.root
    }

    private fun fetchstudentData() {
        FirebaseDatabase.getInstance().getReference("Student")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    studentList.clear() // Clear the list before adding new data

                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(Student::class.java)
                        if (user != null) {
                            studentList.add(user) // Add user data to the list
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
            supad_student().apply {
                arguments = Bundle().apply {


                }
            }
    }
}