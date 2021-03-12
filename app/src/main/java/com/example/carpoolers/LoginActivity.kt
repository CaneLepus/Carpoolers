package com.example.carpoolers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var email: EditText
    private lateinit var password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        email = findViewById(R.id.editTextEmailLogin)
        password = findViewById(R.id.editTextTextPasswordLogin)
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            if (email.text.toString() == "" || password.text.toString() == ""){
                Toast.makeText(
                    this, "Please fill out both fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "createUserWithEmail:success")
                            val intent = Intent(this, RegisterActivity::class.java)
                            startActivity(intent)

                        } else {
                            Log.w("TAG", "createUserWithEmail:failure ${task.exception?.message}")
                            Toast.makeText(
                                baseContext, task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
            }
            val loginButton = findViewById<Button>(R.id.loginButton)
            loginButton.setOnClickListener {
                if (email.text.toString() == "" || password.text.toString() == ""){
                    Toast.makeText(
                        this, "Please fill out both fields!",
                        Toast.LENGTH_SHORT
                    ).show()
                }else {
                    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val collection = db.collection("users")
                                val document = collection.document(auth.currentUser.uid)
                                    .get()
                                    .addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()){
                                            val intent = Intent(this, MainMenuActivity::class.java)
                                            startActivity(intent)
                                        }else{
                                            val intent = Intent(this, RegisterActivity::class.java)
                                            startActivity(intent)
                                        }
                                    }
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success")
                                //finish()

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, task.exception?.message,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                }
            }
        }

    }

