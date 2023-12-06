package com.example.connectwise

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MessagingActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    private val messagesList = mutableListOf<Message>()

    private val receiverId: String by lazy {
        intent.getStringExtra("RECEIVER_ID") ?: ""
    }
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val senderId: String by lazy {
        firebaseAuth.currentUser?.uid ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging)

        setupViews()
        setupRecyclerView()
        loadMessages()

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupViews() {
        messageRecyclerView = findViewById(R.id.recyclerViewMessages)
        messageEditText = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messagesList)
        messageRecyclerView.adapter = messageAdapter
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadMessages() {
        val chatRef = FirebaseDatabase.getInstance().reference.child("chats").child("${senderId}_$receiverId")

        chatRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (child in snapshot.children) {
                    val message = child.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun sendMessage() {
        val messageText = messageEditText.text.toString().trim()
        if (messageText.isNotEmpty()) {
            // Disable the send button to prevent multiple sends
            sendButton.isEnabled = false

            val message = Message(senderId, receiverId, messageText)
            val chatRef = FirebaseDatabase.getInstance().reference.child("chats").child("${senderId}_$receiverId")
            val newMessageRef = chatRef.push() // Creates a new child with a unique key
            newMessageRef.setValue(message).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // The message will be added in loadMessages()
                    messageEditText.text.clear() // Clear after successful send
                    messageRecyclerView.scrollToPosition(messagesList.size - 1)
                } else {
                    // Handle failure, log it or show a toast
                }
                // Re-enable the send button in both success and failure cases
                sendButton.isEnabled = true
            }.addOnFailureListener {
                // Log or handle failure
                sendButton.isEnabled = true
            }
        }
    }
}