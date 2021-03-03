package com.example.carpoolers

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carpoolers.SwipeFunction.SwipeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    lateinit var first: EditText
    lateinit var second: EditText
    lateinit var phone: EditText
    lateinit var address: EditText
    lateinit var bio: EditText
    private val pickImage = 100;
    var lat by Delegates.notNull<Double>()
    var long by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        var button = findViewById<Button>(R.id.registerButton)
        first = findViewById(R.id.firstNameEditText)
        second = findViewById(R.id.lastNameEditText)
        phone = findViewById(R.id.editTextPhone)
        address = findViewById(R.id.editTextPostalAddress)
        bio = findViewById(R.id.editTextBiography)
        phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        button.setOnClickListener {
            click()
        }
    }

    private fun click(){
        isEnteredCorrectly()
    }
    fun CharSequence?.isValidPhoneNumber() = !isNullOrEmpty() && this.matches(Regex("^[+]?[0-9]{8,20}$"))
    fun isEnteredCorrectly(){
        if (first.text.toString() != "" && second.text.toString() != ""){
                if (phone.text.isValidPhoneNumber()){
                    if (getCoordinates()) {
                        var user = auth.currentUser
                        val collection = db.collection("users")
                        val userInfo = User(first.text.toString(), second.text.toString(), phone.text.toString(), lat, long, bio.text.toString(), ArrayList()
                        )
                        Log.d("TAG", "User id: ${user.uid}")
                        collection.document(user.uid).set(userInfo.storeFormat())
                                .addOnSuccessListener {
                                    Log.d("TAG", "DocumentSnapshot successfully written!")
                                    val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                                    startActivityForResult(gallery, pickImage)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Error writing document",
                                            Toast.LENGTH_SHORT).show()
                                    Log.w("TAG", "Error writing document")
                                }
                    }

                }else{
                    val toast = Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT)
                    toast.show()
                }
        }else{
            val toast = Toast.makeText(this, "Enter a valid name", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage){
            val imageUri = data?.data
            if (imageUri != null) {
                updateInfo(imageUri)
            }
        }
    }
    fun getCoordinates(): Boolean{
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addressInfo = geocoder.getFromLocationName(address.text.toString(), 1)
            lat = addressInfo.get(0).latitude
            long = addressInfo.get(0).longitude
            return true
        }catch (e: Exception){
            Toast.makeText(this, "Enter a valid address",
                    Toast.LENGTH_SHORT).show()
            return false
        }
    }
    fun updateInfo(imageUri: Uri){
        val changeRequest = UserProfileChangeRequest.Builder()
                .setPhotoUri(imageUri)
                .setDisplayName(first.text.toString() + " " + second.text.toString())
        auth.currentUser.updateProfile(changeRequest.build())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Log.d("TAG", "User picture added")
                        val intent = Intent(this, SwipeActivity::class.java)
                        startActivity(intent)
                    }

                }
    }

}