package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Models.Fee
import com.example.collegeconnect.R

class FeeAdapter(private val feeList: List<Fee>) : RecyclerView.Adapter<FeeAdapter.FeeViewHolder>() {

    inner class FeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val semText: TextView = view.findViewById(R.id.semtext)
        val amountText: TextView = view.findViewById(R.id.amounttext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fee_layout, parent, false)
        return FeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeeViewHolder, position: Int) {
        val fee = feeList[position]
        holder.semText.text = fee.sem
        holder.amountText.text = fee.amount
    }

    override fun getItemCount(): Int = feeList.size
}
