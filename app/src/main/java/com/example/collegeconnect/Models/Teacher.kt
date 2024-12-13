package com.example.collegeconnect.Models

data class Teacher(
val username: String,
val email: String ,
val number: String ,
val branch : String,
val subject: String,
val gender: String,
val experience: String,
val password: String,
val uniqueId: String
){
    constructor() : this("", "", "", "", "", "", "", "", "")
}
