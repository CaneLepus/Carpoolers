package com.example.carpoolers

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile_page.*
import org.w3c.dom.Text
import java.util.*

/**
 * Composed by Shmonn<3
 */
class ProfilePage : AppCompatActivity() {
    lateinit var profilePic: ImageView
    lateinit var first: TextView
    lateinit var last: TextView
    lateinit var bio: TextView
    lateinit var phone: TextView
    lateinit var displayName: TextView
    lateinit var emailInputWindow: TextInputEditText
    lateinit var address: TextView
    lateinit var ratings: String
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        profilePic = findViewById(R.id.profilePictureView)
        first = findViewById(R.id.firstNameInput)
        last = findViewById(R.id.lastNameInput)
        bio = findViewById(R.id.bioInput)
        phone = findViewById(R.id.phoneInput)
        emailInputWindow = findViewById(R.id.emailInput)
        displayName = findViewById(R.id.nameDisplayTextField)

        initProfile()
    }

    private fun initProfile() {
        auth = Firebase.auth
        val db = Firebase.firestore
        val users = db.collection("users")
        val query = users.document(auth.currentUser.uid)

        query.get().addOnSuccessListener { document ->
            checkPermission()
            profilePic.setImageURI(auth.currentUser.photoUrl)
            first.hint = document.get("first") as String
            last.hint = document.get("last") as String
            bio.hint = document.get("biography") as String
            displayName.text = auth.currentUser.displayName
            emailInputWindow.hint = auth.currentUser.email
            phone.hint = auth.currentUser.phoneNumber

            //TODO: fixa så phone syns

        }.addOnFailureListener {
            Toast.makeText(this, "Some or all parts of profile could not be initialized",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) { //Can add more as per requirement
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                123
            )
        } else {
            // load profilepic outside this method
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            123 -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //Peform your task here if any
                    profilePic.setImageURI(auth.currentUser.photoUrl)
                } else {
                    checkPermission()
                }
                return
            }
        }
    }
}