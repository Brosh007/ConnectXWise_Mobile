package com.example.connectwise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WebinarsInternActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WebinarsInternAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webinars_intern)

        recyclerView = findViewById(R.id.rvWebinarsIntern)
        adapter = WebinarsInternAdapter(mutableListOf(), this::onSignUpForWebinar)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadInternRegisteredWebinars()
    }

    private fun loadInternRegisteredWebinars() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            FirebaseDatabase.getInstance().getReference("UserWebinars")
                .orderByChild("InternUid").equalTo(user.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val registeredWebinarIds = snapshot.children.mapNotNull {
                            it.getValue(UserWebinar::class.java)?.WebinarId
                        }
                        loadWebinars(registeredWebinarIds)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(this@WebinarsInternActivity, "Error loading webinars: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun loadWebinars(registeredWebinarIds: List<String>) {
        FirebaseDatabase.getInstance().getReference("Webinars")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val webinarsList = mutableListOf<Webinar>()
                    for (webinarSnapshot in snapshot.children) {
                        val webinar = webinarSnapshot.getValue(Webinar::class.java)
                        if (webinar != null && webinar.FirebaseKey !in registeredWebinarIds) {
                            webinarsList.add(webinar)
                        }
                    }
                    adapter.updateWebinars(webinarsList)
                    recyclerView.adapter = adapter // Reset the adapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@WebinarsInternActivity, "Error loading webinars: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun onSignUpForWebinar(webinar: Webinar) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userWebinar = UserWebinar(InternUid = user.uid, WebinarId = webinar.FirebaseKey)
            FirebaseDatabase.getInstance().getReference("UserWebinars")
                .push() // Generates a new key for each sign-up
                .setValue(userWebinar)
                .addOnSuccessListener {
                    Toast.makeText(this, "Signed up for webinar successfully", Toast.LENGTH_SHORT).show()
                    loadInternRegisteredWebinars() // Reload to update the list
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to sign up: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}