package com.martdev.chitchat

data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val profile_picture: String
) {
    constructor() : this("", "", "", "")
}