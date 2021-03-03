package com.example.carpoolers

data class User(val first: String, val second: String, val phone: String, val lat: Double, val long: Double, val bio: String, val ratings:ArrayList<Double>) {
    fun storeFormat(): HashMap<Any, Any>{

        val rating: ArrayList<Double> = ArrayList()
        rating.add(0.0)

        return hashMapOf(
            "first" to first,
            "last" to second,
            "phone" to phone,
            "rating" to 0,
            "latitude" to lat,
            "longitude" to long,
            "biography" to bio
        )
    }
}