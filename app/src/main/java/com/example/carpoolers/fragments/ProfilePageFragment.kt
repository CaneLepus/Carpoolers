package com.example.carpoolers.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.carpoolers.*
import com.example.carpoolers.FacebookActivity
import com.example.carpoolers.MainActivity
import com.example.carpoolers.R
import com.example.carpoolers.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.properties.Delegates


/**
 * This class handles the profile page where you can change information, profile picture etc.
 * modded by FELIX xD
 * @author Shmonn<3
 */

class ProfilePageFragment : Fragment() {

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
    private lateinit var deleteButton: Button
    private lateinit var checkBox: CheckBox
    private val pickImage = 100;
    private lateinit var phoneNumber: String
    private lateinit var updateButton: Button
    private var lat by Delegates.notNull<Double>()
    private var long by Delegates.notNull<Double>()
    private lateinit var geocoder: Geocoder
    private lateinit var email: String
    private lateinit var address: TextView
    private lateinit var ratings: String // not implemented
    private lateinit var shareButton: Button
    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance()
    private val users = db.collection("users")
    private val query = users.document(auth.currentUser.uid)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_profile_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var v : View? = view



        geocoder = Geocoder(context, Locale.getDefault())

        if (v != null) {
            profilePic = v.findViewById(R.id.profilePictureView)
            first = v.findViewById(R.id.firstNameInput)
            last = v.findViewById(R.id.lastNameInput)
            bio = v.findViewById(R.id.bioInput)
            phone = v.findViewById(R.id.phoneInput)
            emailInputWindow = v.findViewById(R.id.emailInput)
            displayName = v.findViewById(R.id.nameDisplayTextField)
            passWordField = v.findViewById(R.id.passInput)
            updateButton = v.findViewById(R.id.updateButton)
            updateButton.setOnClickListener {
                update()
            }
            address = v.findViewById(R.id.addressInput)
            checkBox = v.findViewById(R.id.checkBox)
            deleteButton = v.findViewById(R.id.deleteButton)
            shareButton = v.findViewById(R.id.shareButton)
        }

        deleteButton.setOnClickListener {
            deleteAction()
        }
        shareButton.setOnClickListener {
            val intent = Intent(context, FacebookActivity::class.java)
            intent.putExtra("address", address.text.toString())
            startActivity(intent)
        }

        checkPermission()
        initProfile()
        test()

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
            //profilePic.setImageURI(auth.currentUser.photoUrl)
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
                context, "Some or all parts of profile could not be initialized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkPermission() {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            != PackageManager.PERMISSION_GRANTED && context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            != PackageManager.PERMISSION_GRANTED
        ) { //Can add more as per requirement
            ActivityCompat.requestPermissions(
                context as Activity,
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
                    //profilePic.setImageURI(auth.currentUser.photoUrl)
                } else {
                    checkPermission()
                }
                return
            }
        }
    }

    private fun test(){
        // Points to the root reference
        var path : String = ""

        if (auth.currentUser != null){
            path = "images/" + auth.currentUser.uid
        }
        Toast.makeText(context, path, Toast.LENGTH_LONG).show()

        val storageReference : StorageReference = FirebaseStorage.getInstance().reference.child(path)

        try {
            var file : File = File.createTempFile("test", "jpg")
            storageReference.getFile(file)
                .addOnSuccessListener {

                    var bitmap : Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    profilePic.setImageBitmap(bitmap)

                }
        }catch (e: IOException){
            Log.i("Debug", "Profile picture failed to load.")
        }
    }



    private fun update() {
        var profile = false
        var pass = false

        if (first.text.toString() != firstName || last.text.toString() != lastName
            || phone.text.toString() != phoneNumber || bio.text.toString() != biography
            || emailInputWindow.text.toString() != email
            || address.text.toString() != addressString) {

            val changeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(first.text.toString() + " " + last.text.toString())
            auth.currentUser.updateProfile(changeRequest.build())

            val addressInfo = geocoder.getFromLocationName(address.text.toString(), 1)
            lat = addressInfo[0].latitude
            long = addressInfo[0].longitude

            val userInfo = User(
                first.text.toString(),
                last.text.toString(),
                phone.text.toString(),
                lat,
                long,
                bio.text.toString(),
                ArrayList(),
                "token",
                ""
            )
            query.set(userInfo.storeFormat())

            auth.currentUser.updateEmail(email)

            profile = true
        }

        if(!passWordField.text.isNullOrEmpty()){
            auth.currentUser.updatePassword(passWordField.text.toString())
            pass = true
        }

        if(checkBox.isChecked){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        if(profile && !pass){
            Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            Thread.sleep(3000)
            initProfile()
        } else if(profile && pass){
            Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
            Thread.sleep(3000)
            initProfile()
        } else if (!profile && pass){
            Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
            Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
            Thread.sleep(3000)
            initProfile()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage){
            val imageUri = data?.data
            if (imageUri != null) {
                //updateInfo()

                var pd = ProgressDialog(context)
                pd.setTitle("Uploading")
                pd.show()

                var filepath: Uri = data.data!!

                var imageRef =
                    FirebaseStorage.getInstance().reference.child("images/" + auth.currentUser.uid)
                imageRef.putFile(filepath)
                    .addOnSuccessListener { p0 ->
                        pd.dismiss()
                        Toast.makeText(context, "Image Uploaded!", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, MainMenuActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { p0 ->
                        pd.dismiss()
                        Toast.makeText(context, "Image NOT Uploaded!", Toast.LENGTH_LONG).show()

                    }
                    .addOnProgressListener { p0 ->

                        var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                        pd.setMessage("uploaded ${progress.toInt()}%")

                    }
            }
        }
    }

    private fun deleteAction() {
        var tempuid = auth.currentUser.uid
        AlertDialog.Builder(context)
            .setIcon(R.drawable.notifications_icon)
            .setTitle("Deleting account")
            .setMessage("Are you sure you want to delete your account? This cannot be undone")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which ->

                    auth.currentUser.delete().addOnSuccessListener {
                        Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                        val photoRef: StorageReference =
                            storage.getReference("images/$tempuid")

                        photoRef.delete().addOnSuccessListener {

                            Toast.makeText(
                                context,
                                "PROFILE IMAGE documents deleted",
                                Toast.LENGTH_SHORT
                            ).show()// File deleted successfully
                        }.addOnFailureListener { // Uh-oh, an error occurred!

                            Toast.makeText(
                                context,
                                "PROFILE PIC NOT DELETED",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        query.get().addOnSuccessListener { document ->
                            document.reference.delete().addOnSuccessListener {
                                Toast.makeText(
                                    context,
                                    "User documents deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Failed to delete user documents",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }.addOnFailureListener {
                        Toast.makeText(context, "Account deletion failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            .setNegativeButton("No", null)
            .show()
    }




}