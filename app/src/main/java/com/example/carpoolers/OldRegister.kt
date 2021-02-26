package com.example.carpoolers

import android.os.Bundle
import android.os.PersistableBundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OldRegister : AppCompatActivity() {
//    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        val button = findViewById<Button>(R.id.registerButton)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        print("In register activity")
        button.setOnClickListener {
            click()
        }
    }

    private fun click(){
        val first = findViewById<EditText>(R.id.firstNameEditText)
        val second = findViewById<EditText>(R.id.lastNameEditText)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val repeat = findViewById<EditText>(R.id.editTextTextPasswordRepeat)
        if (first.text.toString() != "" && second.text.toString() != ""){
            if(phone.text.toString() != ""){
                if (isEmailValid(email.text.toString())){
                    if (password.text.toString() != "" && password.text == repeat.text){
                        val user = User(first.text.toString(), second.text.toString(), phone.text.toString(),
                        email.text.toString(), password.text.toString())
//                        db.collection("users")
//                            .add(user.storeFormat())
//                            .addOnSuccessListener { documentReference ->
//                                Log.d("Success: ", "DocumentSnapshot added with ID: ${documentReference.id}")
//                            }
//                            .addOnFailureListener{e ->
//                                Log.w("Error: ", "Error adding document", e)
//                            }
                    }else{
                        val toast = Toast.makeText(this, "Enter the same password in both password fields.", Toast.LENGTH_SHORT)
                        toast.show()
                    }

                }else{
                    val toast = Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT)
                    toast.show()
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
    private fun isEmailValid(email: CharSequence) : Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}