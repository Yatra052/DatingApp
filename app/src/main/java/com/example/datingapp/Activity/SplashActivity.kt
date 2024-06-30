package com.example.datingapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.datingapp.Auth.LoginActivity
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({
       if(user == null)
       {
           startActivity(Intent(this, LoginActivity::class.java))
       }
            else{
           startActivity(Intent(this, MainActivity::class.java))
           finish()

       }

        }, 2000)
    }
}