package com.example.collegeconnect.Models

data class Users(
    val username: String = "",
    val email: String = "",
    val rollno : String =" ",
    val password : String =" ",
    val branch: String =" "
) {
    constructor() : this("", "","","","")
}
