package com.example.chatapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapp.viewmodel.ChatViewModel

@Composable
fun ChatScreen(navController: NavController, friendId: String, chatViewModel: ChatViewModel = viewModel()) {
    var message by remember { mutableStateOf("") }

    LaunchedEffect(friendId) {
        chatViewModel.loadMessage(friendId)
    }

    val messages by chatViewModel.messages.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Messages list would go here
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(messages){
                msg->
                Text(
                    text = msg.content,
                    modifier = Modifier.padding(8.dp)
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                chatViewModel.sendMessage(
                    message,
                    friendId
                )
                message = ""
            }) {
                Text("Send")
            }
        }
    }
}
