package com.example.carpoolers




import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.Sharer
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareButton
import com.facebook.share.widget.ShareDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class FacebookActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager
    lateinit var shareDialog: ShareDialog
    lateinit var loginManager: LoginManager
    val db =  Firebase.firestore
    val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook)



        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        shareDialog = ShareDialog(this)
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("TAG", "facebook login success")
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (!isLoggedIn) {
            loginManager.logIn(this, Arrays.asList("email"))
        }
        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result?> {
            override fun onSuccess(shareResult: Sharer.Result?) {
                Log.d("TAG", "facebook share success")
                finish()
            }

            override fun onCancel() {
                // App code
                finish()
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
        db.collection("users").document(auth.currentUser.uid)
            .get()
            .addOnSuccessListener { result ->
                val shareLinkContent = ShareLinkContent.Builder()
                    .setContentUrl(
                        Uri.parse(
                            "https://www.google.com/maps/search/${intent.getStringExtra("address")}"
                        )
                    )
                    .setShareHashtag(
                        ShareHashtag.Builder()
                            .setHashtag("#Carpoolers").build()
                    )
                    .setQuote("Join me at Carpoolers to meet others needing a travel mate! you can find me here.")
                    .build()
                shareDialog.show(shareLinkContent)
            }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }
}