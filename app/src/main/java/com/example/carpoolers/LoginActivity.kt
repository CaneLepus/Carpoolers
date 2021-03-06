package com.example.carpoolers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.carpoolers.SwipeFunction.SwipeActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.concurrent.thread

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
                Toast.makeText(this, "Please fill out both fields!",
                Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Please fill out both fields!",
                        Toast.LENGTH_SHORT).show()
                }else {
                    auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, ProfilePage::class.java)
                                startActivity(intent)
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success")

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
    fun initiateUser(){
        val users = db.collection("users")
        val query = users.document(auth.currentUser.uid)
        val snapshot = query.get()
        Thread.sleep(2000)

        if (snapshot.result?.exists() == true){
            val first = snapshot.result?.get("first")
            val second = snapshot.result?.get("last")
            val phone = snapshot.result?.get("phone")
            val lat = snapshot.result?.get("latitude")
            val long = snapshot.result?.get("longitude")
            val bio = snapshot.result?.get("biography")
            val ratings = snapshot.result?.get("rating")
            val fcmKey = snapshot.result?.get("fcmKey")
            Singleton.user = User(first as String, second as String, phone as String,
                    lat as Double, long as Double, bio as String, ratings as ArrayList<Double>, fcmKey as String)
            Log.d("TAG", Singleton.user.storeFormat().toString())
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    }

