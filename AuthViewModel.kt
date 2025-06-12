package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.repository.FirebaseRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val firebaseRepository = FirebaseRepository()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            firebaseRepository.login(email, password)
        }
    }

    fun signup(email: String, password: String) {
        viewModelScope.launch {
            firebaseRepository.signup(email, password)
        }
    }
}
