package com.example.connectwise


import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class InternListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var internAdapter: InternAdapter
    private lateinit var internsList: ArrayList<Intern>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_list)

        listView = findViewById(R.id.listViewInterns)
        internsList = ArrayList()
        internAdapter = InternAdapter(this, internsList)

        listView.adapter = internAdapter
        fetchInterns()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedIntern = internsList[position]


            val intent = Intent(this, MessagingActivity::class.java).apply {
                putExtra("RECEIVER_ID", selectedIntern.InternID)

            }
            startActivity(intent)
        }
    }

    private fun fetchInterns() {
        val database = FirebaseDatabase.getInstance().reference.child("Interns")
        database.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val intern = snapshot.getValue(Intern::class.java)
                    intern?.let { internsList.add(it) }
                }
                internAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            // Handle the error
        }
    }
}