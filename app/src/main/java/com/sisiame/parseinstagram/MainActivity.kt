package com.sisiame.parseinstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import com.sisiame.parseinstagram.fragment.ComposeFragment
import com.sisiame.parseinstagram.fragment.FeedFragment
import com.sisiame.parseinstagram.fragment.ProfileFragment
import java.io.File

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

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

    companion object {
        const val TAG = "MainActivity"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    }
}