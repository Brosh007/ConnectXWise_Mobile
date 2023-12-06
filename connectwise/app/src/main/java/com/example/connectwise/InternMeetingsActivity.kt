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

class InternMeetingsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InternMeetingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_meetings)

        recyclerView = findViewById(R.id.rvInternMeetings)
        adapter = InternMeetingAdapter(mutableListOf(), this::onAcceptMeeting, this::onDeclineMeeting)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadInternId()
    }

    private fun loadInternId() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            FirebaseDatabase.getInstance().getReference("Interns").child(user.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val internId = snapshot.child("InternID").getValue(String::class.java)
                        internId?.let { id ->
                            loadMeetingRequests(id)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }

    private fun loadMeetingRequests(internId: String) {
        FirebaseDatabase.getInstance().getReference("MeetingRequests")
            .orderByChild("InternID").equalTo(internId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val requests = snapshot.children.mapNotNull { it.getValue(MeetingRequest::class.java) }
                        .filter { it.Status == "Pending" } // Filter only pending requests
                    adapter.updateMeetingsRequests(requests)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun onAcceptMeeting(request: MeetingRequest) {
        updateMeetingRequestStatus(request, "Accepted")
    }

    private fun onDeclineMeeting(request: MeetingRequest) {
        updateMeetingRequestStatus(request, "Declined")
    }

    private fun updateMeetingRequestStatus(request: MeetingRequest, status: String) {
        val updates = hashMapOf<String, Any>("Status" to status)
        FirebaseDatabase.getInstance().getReference("MeetingRequests")
            .child(request.FirebaseKey)
            .updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Meeting $status", Toast.LENGTH_SHORT).show()
                loadInternId() // Reload meeting requests to reflect changes
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update meeting: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}