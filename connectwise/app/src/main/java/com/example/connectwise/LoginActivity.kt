package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let { uid ->
                        checkUserRoleAndNavigate(uid)
                    }
                } else {
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRoleAndNavigate(userId: String) {
        database.reference.child("BusinessOwners").child(userId).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    navigateToBusinessOwnerHome(userId)
                } else {
                    checkIfIntern(userId)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkIfIntern(userId: String) {
        database.reference.child("Interns").child(userId).get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val internID = dataSnapshot.child("InternID").getValue(String::class.java) ?: ""
                    navigateToInternHome(userId, internID)
                } else {
                    Toast.makeText(this, "User role not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToBusinessOwnerHome(userId: String) {
        val intent = Intent(this, BusinessOwnerHomeActivity::class.java).apply {
            putExtra("USER_ID", userId)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToInternHome(userId: String, internID: String) {
        val intent = Intent(this, InternHomeActivity::class.java).apply {
            putExtra("USER_ID", userId)
            putExtra("INTERN_ID", internID)
        }

        startActivity(intent)
        finish()
    }
}