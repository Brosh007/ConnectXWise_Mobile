package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterInternActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_intern)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Get references to the EditTexts and Button
        val editTextFirstName = findViewById<EditText>(R.id.editTextFirstName)
        val editTextLastName = findViewById<EditText>(R.id.editTextLastName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextProfileImage = findViewById<EditText>(R.id.editTextProfileImage)
        val editTextBio = findViewById<EditText>(R.id.editTextBio)
        val editTextEducation = findViewById<EditText>(R.id.editTextEducation)
        val editTextInterests = findViewById<EditText>(R.id.editTextInterests)
        val editTextInternID = findViewById<EditText>(R.id.editTextInternID)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextSkills = findViewById<EditText>(R.id.editTextSkills)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid ?: return@addOnCompleteListener
                        saveInternInfo(userId,
                            editTextFirstName.text.toString(),
                            editTextLastName.text.toString(),
                            email,
                            editTextPhoneNumber.text.toString(),
                            editTextProfileImage.text.toString(),
                            editTextBio.text.toString(),
                            editTextEducation.text.toString(),
                            editTextInterests.text.toString(),
                            editTextInternID.text.toString(),
                            editTextLocation.text.toString(),
                            editTextSkills.text.toString())
                    } else {
                        Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveInternInfo(userId: String, firstName: String, lastName: String, email: String, phoneNumber: String, profileImage: String, bio: String, education: String, interests: String, internID: String, location: String, skills: String) {
        val intern = hashMapOf(
            "FirstName" to firstName,
            "LastName" to lastName,
            "Email" to email,
            "PhoneNumber" to phoneNumber,
            "ProfileImage" to profileImage,
            "Bio" to bio,
            "Education" to education,
            "Interests" to interests,
            "InternID" to internID,
            "Location" to location,
            "Skills" to skills
        )

        database.reference.child("Interns").child(userId).setValue(intern)
            .addOnSuccessListener {
                Toast.makeText(this, "Intern registration successful", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to register intern: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}