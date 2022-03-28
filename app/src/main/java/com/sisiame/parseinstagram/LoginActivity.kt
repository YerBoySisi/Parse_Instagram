 package com.sisiame.parseinstagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.Parse
import com.parse.ParseObject
import com.parse.ParseUser

 class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ParseObject.registerSubclass(Post::class.java)

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )

        // go straight to main activity if user is logged in
        if(ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val user = findViewById<EditText>(R.id.input_user).text.toString()
            val pass = findViewById<EditText>(R.id.input_pass).text.toString()
            logIn(user, pass)
        }

        findViewById<Button>(R.id.btn_signup).setOnClickListener {
            val user = findViewById<EditText>(R.id.input_user).text.toString()
            val pass = findViewById<EditText>(R.id.input_pass).text.toString()
            signUp(user, pass)
        }

    }

     private fun signUp(user: String, pass: String) {

         val newUser = ParseUser()

         newUser.username = user
         newUser.setPassword(pass)

         newUser.signUpInBackground { e ->
             if(e == null) {
                 Log.i(TAG, "Succesfully signed up")

                 goToMainActivity()
             } else {
                 e.printStackTrace()
                 Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show()
             }
         }

     }

     private fun logIn(user: String, pass: String) {

         ParseUser.logInInBackground(user, pass, ({u, e ->
             if(u != null) {
                 Log.i(TAG, "Succesfully logged in")

                 goToMainActivity()
             } else {
                 e.printStackTrace()
                 Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
             }
         }))

     }

     private fun goToMainActivity() {
         val intent = Intent(this@LoginActivity, MainActivity::class.java)
         startActivity(intent)
         finish()
     }

     companion object {
         const val TAG = "LoginActivity"
     }

}