package com.example.collegeconnect.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.R

class SliderAdapter(private val context: Context, private val imageList: List<Int>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {


    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.sliderImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.slider_layout, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}
