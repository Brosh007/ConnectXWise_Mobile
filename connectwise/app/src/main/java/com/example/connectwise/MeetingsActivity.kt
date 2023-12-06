package com.example.connectwise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MeetingsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var adapter: MeetingRequestAdapter
    private val meetingRequests = mutableListOf<MeetingRequest>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meetings)
        // Set up the home icon click listener
        val imgHome = findViewById<ImageView>(R.id.imgHome)
        imgHome.setOnClickListener {
            val intent = Intent(this, BusinessOwnerHomeActivity::class.java)
            startActivity(intent)
        }

        listView = findViewById(R.id.listViewMeetingRequests)
        adapter = MeetingRequestAdapter(this, meetingRequests)
        listView.adapter = adapter

        fetchMeetingRequests()
    }

    private fun fetchMeetingRequests() {
        val businessOwnerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val databaseRef = FirebaseDatabase.getInstance().reference.child("MeetingRequests")

        databaseRef.orderByChild("BusinessOwnerID").equalTo(businessOwnerId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                meetingRequests.clear()
                for (requestSnapshot in snapshot.children) {
                    val meetingRequest = requestSnapshot.getValue(MeetingRequest::class.java)
                    meetingRequest?.let { meetingRequests.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }
}