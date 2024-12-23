package com.example.collegeconnect.Teacher.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.collegeconnect.R


class teacher_addstudent : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_addstudent, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            teacher_addstudent().apply {
                arguments = Bundle().apply {

                }
            }
    }
}