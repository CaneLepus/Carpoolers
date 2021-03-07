package com.example.carpoolers

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * Composed by Shmonn<3
 */
class ProfilePage : AppCompatActivity() {
    private lateinit var profilePic: ImageView
    private lateinit var first: TextView
    private lateinit var last: TextView
    private lateinit var bio: TextView
    private lateinit var phone: TextView
    private lateinit var displayName: TextView
    private lateinit var passWordField: TextView
    private lateinit var emailInputWindow: TextView
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var biography: String
    private lateinit var addressString: String
    private lateinit var checkBox: CheckBox
    private val pickImage = 100;
    private lateinit var phoneNumber: String
    private lateinit var updateButton: Button
    private var lat by Delegates.notNull<Double>()
    private var long by Delegates.notNull<Double>()
    private val geocoder = Geocoder(this, Locale.getDefault())
    private lateinit var email: String
    private lateinit var address: TextView
    private lateinit var ratings: String // not implemented
    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val users = db.collection("users")
    private val query = users.document(auth.currentUser.uid)

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
        passWordField = findViewById(R.id.passInput)
        updateButton = findViewById(R.id.updateButton)
        updateButton.setOnClickListener {
            update()
        }
        address = findViewById(R.id.addressInput)
        checkBox = findViewById(R.id.checkBox)

        checkPermission()
        initProfile()
    }

    private fun initProfile() {
        query.get().addOnSuccessListener { document ->
            first.text = document.get("first") as String
            last.text = document.get("last") as String
            bio.text = document.get("biography") as String
            emailInputWindow.text = auth.currentUser.email as String
            phone.text = document.get("phone") as String
            lat = document.getDouble("latitude")!!
            long = document.getDouble("longitude")!!
            address.text = geocoder.getFromLocation(lat, long, 1)[0].getAddressLine(0)
            profilePic.setImageURI(auth.currentUser.photoUrl)
            displayName.text = auth.currentUser.displayName
            passWordField.hint = "Update password"

            firstName = first.text.toString()
            lastName = last.text.toString()
            biography = bio.text.toString()
            email = emailInputWindow.text.toString()
            phoneNumber = phone.text.toString()
            addressString = address.text.toString()

        }.addOnFailureListener {
            Toast.makeText(
                this, "Some or all parts of profile could not be initialized",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun update() {
        if (first.text.toString() != firstName || last.text.toString() != lastName
            || phone.text.toString() != phoneNumber || bio.text.toString() != biography
            || emailInputWindow.text.toString() != email
            || address.text.toString() != addressString || passWordField.text.isNullOrEmpty()
        ) {

            val changeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(first.text.toString() + " " + last.text.toString())
            auth.currentUser.updateProfile(changeRequest.build())

            val addressInfo = geocoder.getFromLocationName(address.text.toString(), 1)
            lat = addressInfo[0].latitude
            long = addressInfo[0].longitude

            val userInfo = User(first.text.toString(), last.text.toString()
                , phone.text.toString()
                , lat, long, bio.text.toString(), ArrayList()
                , "token")
            query.set(userInfo.storeFormat())

            auth.currentUser.updateEmail(email)
            if (!passWordField.text.isNullOrEmpty()){
                auth.currentUser.updatePassword(passWordField.text.toString())
            }

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show()
        }

        initProfile()
    }
}