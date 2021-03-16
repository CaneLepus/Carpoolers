package com.example.carpoolers

data class User(
    val first: String,
    val second: String,
    val phone: String,
    val lat: Double,
    val long: Double,
    val bio: String,
    val ratings: ArrayList<Double>,
    val fcmKey: String,
    val imgUrl: String,
    val uid: String = ""
) {
    fun storeFormat(): HashMap<Any, Any> {

        val rating: ArrayList<Double> = ArrayList()
        rating.add(0.0)

        return hashMapOf(
            "first" to first,
            "last" to second,
            "phone" to phone,
            "rating" to rating,
            "latitude" to lat,
            "longitude" to long,
            "biography" to bio,
            "fcmKey" to fcmKey,
            "imgUrl" to imgUrl
        )
    }
}