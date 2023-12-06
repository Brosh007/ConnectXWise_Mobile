package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnRegisterBusinessOwner = findViewById<Button>(R.id.btnRegisterBusinessOwner)
        val btnRegisterIntern = findViewById<Button>(R.id.btnRegisterIntern)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Navigate to Register Business Owner Activity
        btnRegisterBusinessOwner.setOnClickListener {
            val intent = Intent(this, RegisterBusinessOwnerActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Register Intern Activity
        btnRegisterIntern.setOnClickListener {
            val intent = Intent(this, RegisterInternActivity::class.java)
            startActivity(intent)
        }

        // Navigate to Login Activity
        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}