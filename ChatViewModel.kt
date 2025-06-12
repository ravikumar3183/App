package com.example.chatapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.Message
import com.example.chatapp.model.User
import com.example.chatapp.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class ChatViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    private val _friends = MutableStateFlow<List<User>>(emptyList())
    val friends: StateFlow<List<User>> = _friends

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages:StateFlow<List<Message>> = _messages


    init {
        viewModelScope.launch {
            repository.getFriends().collect {
                _friends.value = it
            }
        }
    }

    fun loadMessage(friendId:String){
        viewModelScope.launch {
            repository.getMessages(friendId).collect{
                _messages.value=it
            }
        }
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()

    }

    fun sendMessage(content: String,friendId: String) {
        val message = Message(content = content, friendId = friendId, timestamp = System.currentTimeMillis())
        viewModelScope.launch {
            repository.sendMessage(message)
        }
    }
}