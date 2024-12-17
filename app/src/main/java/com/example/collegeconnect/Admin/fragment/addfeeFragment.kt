package com.example.collegeconnect.Admin.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.collegeconnect.Admin.Admin_dashboard
import com.example.collegeconnect.R
import com.example.collegeconnect.databinding.FragmentAddfeeBinding
import com.google.firebase.database.FirebaseDatabase


class addfeeFragment : Fragment() {
    private lateinit var binding: FragmentAddfeeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_addfee,container,false)

        binding.etsem.setOnClickListener {
            semDropdown(it)
        }

//        binding.toolbar.setNavigationOnClickListener {
//            startActivity(Intent(this, Admin_dashboard::class.java))
//        }

        binding.add.setOnClickListener {
            val sem = binding.etsem.text.toString()
            val amount = binding.etamount.text.toString()
            if (areFieldsValid()) {
                addfee(sem,amount)
            }
        }

        return binding.root
    }
    private fun semDropdown(view: View?) {
        if (view == null) return
        val popupMenu = PopupMenu(requireContext(), view)
        val options = resources.getStringArray(R.array.select_sem)
        options.forEach { option ->
            popupMenu.menu.add(option)
        }


        popupMenu.setOnMenuItemClickListener { menuItem ->
            binding.etsem.setText(menuItem.title)
            binding.etsem.error = null
            true
        }

        popupMenu.show() // Show the menu
    }


    private fun addfee(sem: String, amount: String) {
        val database = FirebaseDatabase.getInstance()
        val attendanceRef = database.getReference("Fee")


        val key = attendanceRef.push().key


        val attendanceData = mapOf(
            "sem" to sem,
            "amount" to amount
        )

        if (key != null) {
            attendanceRef.child(key).setValue(attendanceData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Fee added successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to add attendance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Error generating key", Toast.LENGTH_SHORT).show()
        }
    }


    private fun areFieldsValid(): Boolean {

        var isValid = true

        if (binding.etsem.text.isNullOrEmpty()) {
            binding.sem.error = getString(R.string.empty_semester)
            isValid = false
        }
        else {
            binding.etsem.error = null
        }


        if (TextUtils.isEmpty(binding.etamount.text.toString().trim())) {
            binding.amount.error = getString(R.string.empty_number)
            isValid = false
        }
        else{
            binding.amount.error=null
        }
        return  isValid

    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addfeeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}