package com.example.collegeconnect.Models

data class Student (
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
val image: String,
val uniqueId : String
) {
    constructor() : this("","", "", "", "", "", "", "", "", "", "", "")

}
