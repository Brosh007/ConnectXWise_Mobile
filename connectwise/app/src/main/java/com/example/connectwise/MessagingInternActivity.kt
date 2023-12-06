package com.example.connectwise


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessagingInternActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton

    private val messagesList = mutableListOf<Message>()
    private val chatId: String by lazy { intent.getStringExtra("CHAT_ID") ?: "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messaging_intern)

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
        val chatRef = FirebaseDatabase.getInstance().reference.child("chats").child(chatId)
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
                // Handle errors, log them or display a message
            }
        })
    }

    private fun sendMessage() {
        val messageText = messageEditText.text.toString().trim()
        if (messageText.isNotEmpty()) {
            sendButton.isEnabled = false

            val parts = chatId.split("_")
            val senderId = parts[1]  // InternID
            val receiverId = parts[0]  // businessOwnerUID

            val message = Message(senderId, receiverId, messageText)
            val newMessageRef = FirebaseDatabase.getInstance().reference.child("chats").child(chatId).push()
            newMessageRef.setValue(message).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Only clear the EditText after a successful send
                    messageEditText.text.clear()
                    messageRecyclerView.scrollToPosition(messagesList.size - 1)
                } else {
                    // Handle failure, log it or show a toast
                }
                sendButton.isEnabled = true
            }.addOnFailureListener {
                // Handle failure, log it or show a toast
                sendButton.isEnabled = true
            }
        }
    }
}