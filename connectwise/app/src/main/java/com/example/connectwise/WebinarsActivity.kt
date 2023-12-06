package com.example.connectwise



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.widget.SearchView

class WebinarsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WebinarsAdapter
    private var webinars = mutableListOf<Webinar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webinars)

        recyclerView = findViewById(R.id.rvWebinars)
        adapter = WebinarsAdapter(webinars)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchWebinars(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchWebinars(it) }
                return true
            }
        })

        fetchWebinars()
    }

    private fun fetchWebinars() {
        FirebaseDatabase.getInstance().getReference("Webinars")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    webinars.clear()
                    snapshot.children.mapNotNullTo(webinars) { it.getValue(Webinar::class.java) }
                    adapter.updateWebinars(webinars)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    private fun searchWebinars(query: String) {
        val filteredList = if (query.isEmpty()) {
            webinars
        } else {
            webinars.filter {
                it.Title.contains(query, ignoreCase = true) ||
                        it.SpeakerName.contains(query, ignoreCase = true)
            }
        }
        adapter.updateWebinars(filteredList)
    }
}