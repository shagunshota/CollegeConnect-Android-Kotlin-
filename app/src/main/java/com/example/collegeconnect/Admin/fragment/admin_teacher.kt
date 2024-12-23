package com.example.collegeconnect.Admin.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Adapter.Student_Adapter
import com.example.collegeconnect.Adapter.Teacher_Adapter
import com.example.collegeconnect.Admin.add_fee
import com.example.collegeconnect.Admin.admin_profile
import com.example.collegeconnect.Admin.admin_settings
import com.example.collegeconnect.Models.Student
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
    private var _binding: FragmentAdminTeacherBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: Teacher_Adapter
    private val teacherList = mutableListOf<Teacher>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_admin_teacher, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        checkNetworkStatus()



        userAdapter = Teacher_Adapter(
            teacherList,
            { uniqueId -> deleteteacher(uniqueId) },
            { teacher -> showUpdateDialog(teacher) }
        )
        recyclerView.adapter = userAdapter

        fetchTeacherData()
        return binding.root

    }

    private fun deleteteacher(uniqueId: String) {
        val database = FirebaseDatabase.getInstance()
        val dataRef = database.getReference("Teacher").child(uniqueId)

        dataRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Student deleted successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateteacher(teacher: Teacher, updatedValues: Map<String, Any>) {
        val database = FirebaseDatabase.getInstance()
        val dataRef = database.getReference("Teacher").child(teacher.uniqueId)

        dataRef.updateChildren(updatedValues)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Student updated successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
//    private fun countStudent() {
//        val database = FirebaseDatabase.getInstance()
//        val tableRef = database.getReference("Student")
//        tableRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val count = snapshot.childrenCount
//                binding.count.text = count.toString() // Ensure `countTextView` is present in XML
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }



    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(teacher: Teacher) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.update_teacherdata, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Update Student")
            .setPositiveButton("Update") { _, _ ->
                val updatedName = dialogView.findViewById<EditText>(R.id.ettname).text.toString()
                val updatedEmail = dialogView.findViewById<EditText>(R.id.ettemail).text.toString()
                val updatedNumber = dialogView.findViewById<EditText>(R.id.ettnumber).text.toString()


                val updatedValues = mapOf(
                    "username" to updatedName,
                    "email" to updatedEmail,
                    "number" to updatedNumber
                )

//                if (areFieldsValid()) {
                updateteacher(teacher, updatedValues)}
//            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()


        dialogView.findViewById<EditText>(R.id.ettname).setText(teacher.username)
        dialogView.findViewById<EditText>(R.id.ettemail).setText(teacher.email)
        dialogView.findViewById<EditText>(R.id.ettnumber).setText(teacher.number)
    }

//    private fun areFieldsValid(): Boolean {
//        var isValid = true
//        val updatedName =
//            dialogView.findViewById<EditText>(R.id.ettname).text.toString()
//        val updatedEmail =
//            dialogView.findViewById<EditText>(R.id.ettemail).text.toString()
//        val updatedNumber =
//            dialogView.findViewById<EditText>(R.id.ettnumber).text.toString()
//
//
//        val name = binding.et.text.toString().trim()
//        if (TextUtils.isEmpty(name)) {
//           updatedName.error = getString(R.string.empty_name)
//
//        } else if (!isValidname(name)) {
//            binding.tname.error = getString(R.string.valid_name)
//            isValid = false
//        } else {
//            binding.tname.error = null // Clear error if valid
//        }

//
////        for email
//        val email = binding.ettemail.text.toString()
//        if (TextUtils.isEmpty(
//                binding.ettemail.text.toString().trim()) ) {
//            binding.temail.error = getString(R.string.empty_email)
//
//        }
//        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.ettemail.text.toString().trim()).matches()){
//            binding.temail.error = valid_email
//
//            isValid = false
//        }
//        else{
//            binding.temail.error=null
//        }
//
//
////        for contact
//        if (TextUtils.isEmpty(binding.ettnumber.text.toString().trim())) {
//            binding.tnumber.error = getString(R.string.empty_number)
//            isValid = false
//        }
//        else{
//            binding.tnumber.error=null
//        }
//
//
////        for branch
//
//        if(TextUtils.isEmpty(binding.etBranch.text.toString().trim())){
//            binding.tbranch.error = getString(R.string.empty_branch)
//            isValid= false
//        }
//
//
//
//        //        for subject
//        if (binding.ettSubject.text.isNullOrEmpty()) {
//            binding.tsubject.error = getString(R.string.empty_subject)
//            isValid = false
//        }
//        else{
//            binding.tsubject.error= null
//
//        }





//    }




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