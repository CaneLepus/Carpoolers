package com.example.carpoolers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    val db = Firebase.firestore
    lateinit var first: EditText
    lateinit var second: EditText
    lateinit var phone: EditText
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var repeat: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var button = findViewById<Button>(R.id.registerButton)
        first = findViewById(R.id.firstNameEditText)
        second = findViewById(R.id.lastNameEditText)
        phone = findViewById(R.id.editTextPhone)
        email = findViewById(R.id.editTextTextEmailAddress)
        password = findViewById(R.id.editTextTextPassword)
        repeat = findViewById(R.id.editTextTextPasswordRepeat)
        phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        button.setOnClickListener {
            click()
        }
    }
    private fun click(){
        if (isEnteredCorrectly()) {
            val user = User(
                first.text.toString(), second.text.toString(), phone.text.toString(),
                email.text.toString(), password.text.toString()
            )
            db.collection("users")
                .add(user.storeFormat())
                .addOnSuccessListener { documentReference ->
                    Log.d("Success: ", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("Error: ", "Error adding document", e)
                }
        }

    }
    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    fun CharSequence?.isValidPhoneNumber() = !isNullOrEmpty() && this.matches(Regex("^[+]?[0-9]{8,20}$"))
    fun isEnteredCorrectly(): Boolean{
        if (first.text.toString() != "" && second.text.toString() != ""){
            if(email.text.isValidEmail()){
                if (phone.text.isValidPhoneNumber()){
                    if (password.text.toString() != "" && password.text.toString() == repeat.text.toString()){
                        return true
                    }else{
                        val toast = Toast.makeText(this, "Enter the same password in both password fields.", Toast.LENGTH_SHORT)
                        toast.show()
                    }

                }else{
                    val toast = Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT)
                    toast.show()
                }

            }else{
                val toast = Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT)
                toast.show()
            }
        }else{
            val toast = Toast.makeText(this, "Enter a valid name", Toast.LENGTH_SHORT)
            toast.show()
        }
        return false
    }
}