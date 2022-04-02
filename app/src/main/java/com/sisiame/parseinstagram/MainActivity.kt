package com.sisiame.parseinstagram

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import com.sisiame.parseinstagram.fragment.ComposeFragment
import com.sisiame.parseinstagram.fragment.FeedFragment
import com.sisiame.parseinstagram.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.sign_out).setOnClickListener {
            signOut()
        }

        bottomNav = findViewById(R.id.bottom_navigation)

        bottomNav.setOnItemSelectedListener {

            item ->

            lateinit var fragmentToShow: Fragment

            when(item.itemId) {

                R.id.action_home -> {
                    fragmentToShow = FeedFragment()
                }

                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()
                }

                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }

            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, fragmentToShow)
                .commit()

            true

        }

        bottomNav.selectedItemId = R.id.action_home

    }


    private fun signOut() {

        ParseUser.logOutInBackground {
            goToLoginActivity()
        }

    }

    private fun goToLoginActivity() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {

        const val TAG = "MainActivity"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    }
}