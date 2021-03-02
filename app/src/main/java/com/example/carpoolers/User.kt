package com.example.carpoolers

data class User(val first: String, val second: String, val phone: String) {
    fun storeFormat(): HashMap<Any, Any>{
        return hashMapOf(
            "first" to first,
            "last" to second,
            "phone" to phone,
            "rating" to 0
        )
    }
}