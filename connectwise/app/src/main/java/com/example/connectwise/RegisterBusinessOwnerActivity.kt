package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterBusinessOwnerActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_business_owner)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        // Get references to the EditTexts and Button
        val editTextFirstName = findViewById<EditText>(R.id.editTextFirstName)
        val editTextLastName = findViewById<EditText>(R.id.editTextLastName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextProfileImage = findViewById<EditText>(R.id.editTextProfileImage)
        val editTextCompanyName = findViewById<EditText>(R.id.editTextCompanyName)
        val editTextIndustry = findViewById<EditText>(R.id.editTextIndustry)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        val userId = user?.uid
                        // Save additional user information to the database
                        saveBusinessOwnerInfo(userId,
                            editTextFirstName.text.toString(),
                            editTextLastName.text.toString(),
                            editTextEmail.text.toString(),
                            editTextPhoneNumber.text.toString(),
                            editTextProfileImage.text.toString(),
                            editTextCompanyName.text.toString(),
                            editTextIndustry.text.toString(),
                            editTextLocation.text.toString(),
                            editTextBio.text.toString())
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveBusinessOwnerInfo(userId: String?, firstName: String, lastName: String,email :String, phoneNumber: String, profileImage: String, companyName: String, industry: String, location: String, bio: String) {
        if (userId == null) return

        // Create a new user with a first and last name
        val user = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Email" to email,
            "PhoneNumber" to phoneNumber,
            "ProfileImage" to profileImage,
            "CompanyName" to companyName,
            "Industry" to industry,
            "Location" to location,
            "Bio" to bio
        )

        // Add a new document with a generated ID
        database.reference.child("BusinessOwners").child(userId).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "User information saved successfully", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save user information", Toast.LENGTH_SHORT).show()
            }
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity
    }
}
