package com.example.collegeconnect.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collegeconnect.Admin.Admin_register
import com.example.collegeconnect.R
class Admin_Adapter(
    private val adminList: MutableList<Admin_register.User>
) : RecyclerView.Adapter<Admin_Adapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val admin = adminList[position]
        holder.bind(admin)
    }

    override fun getItemCount(): Int = adminList.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)


        fun bind(admin: Admin_register.User, ) {
//            usernameTextView.text = admin.username
            emailTextView.text = admin.email
//            numberTextView.text = admin.number


        }
    }
}


//class Admin_Adapter(private val adminList: MutableList<Admin_register.User>) :
//    RecyclerView.Adapter<Admin_Adapter.UserViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
//        return UserViewHolder(view)
//    }
//
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val admin = adminList[position]
//        holder.bind(admin)
//    }
//
//
//    override fun getItemCount(): Int = adminList.size
//
//
//    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
//        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
//        private val numberTextView: TextView = itemView.findViewById(R.id.numberTextView)
//
//
//        fun bind(admin: Admin_register.User) {
//            usernameTextView.text = admin.username
//            emailTextView.text = admin.email
//            numberTextView.text = admin.number
//        }
//    }
//}
