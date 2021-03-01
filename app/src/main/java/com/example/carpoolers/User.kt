package com.example.carpoolers

data class User(val first: String, val second: String, val phone: String, val email: String, val password: String, val rating: Double) {
    fun storeFormat(): HashMap<Any, Any>{
        return hashMapOf(
            "first" to first,
            "last" to second,
            "phone" to phone,
            "email" to email,
            "password" to password,
            "rating" to rating
        )
    }
}