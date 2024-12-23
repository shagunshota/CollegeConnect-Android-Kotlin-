package com.example.collegeconnect.Admin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.collegeconnect.R


class add_notification : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_add_notification, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            add_notification().apply {
                arguments = Bundle().apply {

                }
            }
    }
}