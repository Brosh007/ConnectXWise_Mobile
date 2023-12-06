package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView

class BusinessOwnerHomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WebinarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_owner_home)

        recyclerView = findViewById(R.id.recyclerViewWebinars)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WebinarAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchWebinars()
        setupBottomNavigationView()
    }

    private fun fetchWebinars() {
        val database = Firebase.database
        val webinarsRef = database.getReference("Webinars")

        lifecycleScope.launch {
            try {
                val dataSnapshot = webinarsRef.get().await()
                val webinars = dataSnapshot.children.mapNotNull { snapshot ->
                    snapshot.getValue(Webinar::class.java)
                }
                adapter.updateData(webinars)
            } catch (e: Exception) {
                Log.e("BusinessOwnerHomeActivity", "Error fetching webinars", e)
            }
        }
    }
    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_meetings -> {
                    val intent = Intent(this, MeetingsActivity::class.java)
                    startActivity(intent)
                    true // Return true to indicate that the event was handled
                }
                R.id.nav_messaging -> {
                    val intent = Intent(this, InternListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_webinars -> {
                    val intent = Intent(this, WebinarsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}