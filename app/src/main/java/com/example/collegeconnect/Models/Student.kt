package com.example.collegeconnect.Models

data class Student (
    val uniqueId : String,
val username: String,
val email: String,
val number: String,
val rollno: String,
val dob: String,
val gender: String,
val session: String,
val semester: String,
val branch: String,
val password: String,
val image: String
) {
    constructor() : this("","", "", "", "", "", "", "", "", "", "", "")

}
