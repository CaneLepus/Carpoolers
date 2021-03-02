package com.example.carpoolers

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    lateinit var first: EditText
    lateinit var second: EditText
    lateinit var phone: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        var button = findViewById<Button>(R.id.registerButton)
        first = findViewById(R.id.firstNameEditText)
        second = findViewById(R.id.lastNameEditText)
        phone = findViewById(R.id.editTextPhone)
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
                    var user = auth.currentUser
                    val collection = db.collection("users")
                    val userInfo = User(first.text.toString(), second.text.toString(), phone.text.toString())
                    Log.d("TAG", "User id: ${user.uid}")
                    collection.document(user.uid).set(userInfo.storeFormat())
                        .addOnSuccessListener {
                            Log.d("TAG", "DocumentSnapshot successfully written!")
//                            TODO: Add intent for main screen after login
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error writing document",
                            Toast.LENGTH_SHORT).show()
                            Log.w("TAG", "Error writing document")
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

}