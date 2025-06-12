package com.example.chatapp.repository

import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signup(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val userId = auth.currentUser?.uid ?: return
        val user = User(id = userId, email = email, name = email.substringBefore("@"), isOnline = true)
        firestore.collection("users").document(userId).set(user).await()
    }

    fun getFriends(): Flow<List<User>> = flow {
        val snapshot = firestore.collection("users").get().await()
        val friends = snapshot.documents.mapNotNull { it.toObject(User::class.java) }
        emit(friends)
    }

    fun getMessages(friendId:String):Flow<List<Message>> = callbackFlow {
        val senderId = auth.currentUser?.uid ?: run {
            close()
            return@callbackFlow
        }
        val listener = firestore.collection("chats")
            .document(senderId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if(error!=null){
                    close(error)
                    return@addSnapshotListener
                }
                val messages = snapshot?.documents?.mapNotNull {
                    it.toObject(Message::class.java)
                }?.filter { it.friendId == friendId }?: emptyList()
                trySend(messages)
            }
        awaitClose { listener.remove() }
    }

    suspend fun sendMessage(message: Message) {
        val senderId = auth.currentUser?.uid ?: return
        val receiverId = message.friendId
        val messageWithSender = message.copy(senderId = senderId)
        firestore.collection("chats")
            .document(senderId)
            .collection("messages")
            .add(messageWithSender)
            .await()
        firestore.collection("chats")
            .document(receiverId)
            .collection("chats")
            .add(messageWithSender)
            .await()
    }
}
