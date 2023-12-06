package com.example.connectwise

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class InternHomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var calendarView: CalendarView
    private lateinit var notesEditText: EditText
    private lateinit var profileTextView: TextView
    private lateinit var profileDetTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_home)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        calendarView = findViewById(R.id.calendarView)
        notesEditText = findViewById(R.id.editTextNotes)
        profileTextView = findViewById(R.id.tvProfileTitle)
        profileDetTextView = findViewById(R.id.tvInternDetails)

        loadInternProfile()
        setupBottomNavigationView()
    }

    private fun loadInternProfile() {
        val currentUserId = firebaseAuth.currentUser?.uid
        currentUserId?.let { userId ->
            database.reference.child("Interns").child(userId).get()
                .addOnSuccessListener { dataSnapshot ->
                    if (dataSnapshot.exists()) {
                        val intern = dataSnapshot.getValue(Intern::class.java)
                        intern?.let { updateProfileUI(it) }
                    }
                }.addOnFailureListener {
                    // Handle any errors
                }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProfileUI(intern: Intern) {
        // Update UI elements with intern's data
        profileDetTextView.text = "Profile of ${intern.FirstName} ${intern.LastName}\n" +
                "Email:   ${intern.Email}\n" +
                "Education:   ${intern.Education}\n" +
                "Skills:   ${intern.Skills}\n" +
                "Interests:   ${intern.Interests}\n" +
                "Location:   ${intern.Location}"
        // Add more details as needed
    }
    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_meetings -> {
                    val intent = Intent(this, InternMeetingsActivity::class.java)
                    startActivity(intent)
                    true // Return true to indicate that the event was handled
                }
                R.id.nav_messaging -> {
                    val intent = Intent(this, InternChatListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_webinars -> {
                    val intent = Intent(this, WebinarsInternActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}
