package com.example.connectwise

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class InternChatListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var chatListAdapter: ArrayAdapter<String>
    private val chatList = mutableListOf<String>()
    private val internId: String by lazy {
        intent.getStringExtra("INTERN_ID") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intern_chat_list)

        listView = findViewById(R.id.listViewChats)
        chatListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chatList)
        listView.adapter = chatListAdapter

        fetchChatConversations()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedChatId = chatList[position]
            val intent = Intent(this, MessagingInternActivity::class.java).apply {
                putExtra("CHAT_ID", selectedChatId)
            }
            startActivity(intent)
        }
    }

    private fun fetchChatConversations() {
        val database = FirebaseDatabase.getInstance().reference.child("chats")
        database.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                chatList.clear()
                for (snapshot in dataSnapshot.children) {
                    val chatId = snapshot.key ?: continue
                    if (chatId.contains("_$internId")) {
                        chatList.add(chatId)
                    }
                }
                // Set ArrayAdapter with custom layout
                chatListAdapter = ArrayAdapter(this, R.layout.chat_item, R.id.textViewChatItem, chatList)
                listView.adapter = chatListAdapter
            }
        }.addOnFailureListener {
            // Log or handle the failure
        }
    }
}