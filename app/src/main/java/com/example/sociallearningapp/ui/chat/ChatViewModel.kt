package com.example.sociallearningapp.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val root: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("chat")
    private val auth = FirebaseAuth.getInstance()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    // 🔹 Send a new message
    fun sendMessage(receiverId: String, text: String) {
        val senderId = auth.currentUser?.uid ?: return
        val senderThread = root.child(senderId).child(receiverId)
        val receiverThread = root.child(receiverId).child(senderId)

        val key = senderThread.push().key ?: return
        val msg = Message(
            id = key,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            timestamp = System.currentTimeMillis(),
            status = "sent" // single tick
        )

        // write to sender branch
        senderThread.child(key).setValue(msg)
            .addOnSuccessListener {
                // mirror to receiver branch
                receiverThread.child(key).setValue(msg)

                // 🔹 Send notification to receiver
                sendNotificationToReceiver(receiverId, text)
            }
    }

    // 🔹 Minimal notification log (replace with real FCM if backend ready)
    private fun sendNotificationToReceiver(receiverId: String, messageText: String) {
        val usersRef = FirebaseDatabase.getInstance().getReference("users")
        usersRef.child(receiverId).child("fcmToken").get().addOnSuccessListener { snap ->
            val token = snap.value?.toString() ?: return@addOnSuccessListener
            println("Send FCM to token: $token with message: $messageText")
        }
    }

    // 🔹 Listen for messages between current user and receiver
    private var threadListener: ValueEventListener? = null
    fun loadMessages(receiverId: String) {
        val currentUid = auth.currentUser?.uid ?: return

        threadListener?.let {
            root.child(currentUid).child(receiverId).removeEventListener(it)
        }

        val ref = root.child(currentUid).child(receiverId)
        threadListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val msgs = snapshot.children
                    .mapNotNull { it.getValue(Message::class.java) }
                    .sortedBy { it.timestamp }

                viewModelScope.launch { _messages.emit(msgs) }

                // 🔹 Mark messages sent to me as delivered (single tick)
                msgs.filter { it.receiverId == currentUid && it.status == "sent" }
                    .forEach { msg ->
                        ref.child(msg.id).child("status").setValue("delivered")
                    }

                // 🔹 Mark messages as seen (double blue tick) when chat opened
                msgs.filter { it.receiverId == currentUid && it.status != "seen" }
                    .forEach { msg ->
                        ref.child(msg.id).child("status").setValue("seen")
                    }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(threadListener as ValueEventListener)
    }

    override fun onCleared() {
        super.onCleared()
        // optional cleanup
    }
}