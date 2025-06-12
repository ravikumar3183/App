package com.example.chatapp.chatappui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chatapp.model.User
import com.example.chatapp.navigation.Screen
import com.example.chatapp.viewmodel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import kotlinx.serialization.internal.throwMissingFieldException

@Composable
fun FriendsListScreen(navController: NavController,onCreateGroup:(String,List<User>)->Unit, chatViewModel: ChatViewModel = viewModel()) {
    val friends by chatViewModel.friends.collectAsState()
    val auth = FirebaseAuth.getInstance()
    var showCreateGroupDialog by remember { mutableStateOf(false) }

    Column (modifier = Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.align(alignment = Alignment.End),
            onClick = {
                auth.signOut()
                navController.navigate("login"){
                    popUpTo(0){
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
        Button(
            //modifier = Modifier.align(alignment = Alignment.Start),
            onClick = {
               showCreateGroupDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        )
        {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create Group")
        }
        if(showCreateGroupDialog){
            CreateGroupDialog(
                friends=friends,
                onDismiss = {showCreateGroupDialog = false},
                onCreateGroup = { groupName,selectedFriends ->
                    onCreateGroup(groupName,selectedFriends)
                    showCreateGroupDialog = false
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
           /* items(friends) { friend: User ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate(Screen.Chat.createRoute(friend.id))
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(friend.name)
                    Text(
                        if (friend.isOnline) "Online" else "Offline",
                        color = if (friend.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                }
            }*/

            items(friends){ friend: User->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable {
                            navController.navigate(Screen.Chat.createRoute(friend.id))
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = friend.name.take(2).uppercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = friend.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(8.dp)
                                            .background(
                                                color = if(friend.isOnline) Color(0xFF4CAF50) else Color(0xFF9E9E9E),
                                                shape = CircleShape
                                            )
                                    )
                                    Text(
                                        text = if (friend.isOnline) "Online" else "Offline",
                                        color = if (friend.isOnline){
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                        IconButton(onClick = {
                            navController.navigate(Screen.Chat.createRoute(friend.id))
                        },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                                .size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "send message to ${friend.name}",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }}

@Composable
private fun CreateGroupDialog(friends: List<User>, onDismiss: () -> Unit, onCreateGroup:(String,List<User>)->Unit) {
    var groupName by remember { mutableStateOf("") }
    var selectedFriends by remember { mutableStateOf(setOf<User>()) }


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = groupName,
            onValueChange = {groupName=it},
            label = { Text("Group Name") },
            placeholder = { Text("Enter Group Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Text(
            text = "Select friends(${selectedFriends.size} selected)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(friends) { friend ->
                FriendSelectionItem(
                    friend = friend,
                    isSelected = selectedFriends.contains(friend),
                    onSelectionChange = { isSelected ->
                        selectedFriends = if (isSelected) {
                            selectedFriends + friend
                        } else {
                            selectedFriends - friend
                        }
                    }
                )
            }
        }
                TextButton(
                    onClick = {
                        if (groupName.isNotBlank() && selectedFriends.isNotEmpty()) {
                            onCreateGroup(groupName, selectedFriends.toList())
                        }
                    },
                    enabled = groupName.isNotBlank() && selectedFriends.isNotEmpty()
                ) {
                    Text("Create")
                }
            }

                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }




@Composable
fun FriendSelectionItem(friend: User, isSelected: Boolean, onSelectionChange:(Boolean)->Unit) {
    Row(modifier = Modifier.fillMaxWidth()
        .clickable { onSelectionChange(!isSelected) }
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = friend.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (friend.isOnline) "online" else "offline",
                style = MaterialTheme.typography.bodySmall,
                color = if(friend.isOnline) {
                    Color(0xFF4CAF50)
                }
                else{
                    MaterialTheme.colorScheme.onSurface.copy(0.6f)
                }
            )
        }
    }
    Checkbox(
        checked = isSelected,
        onCheckedChange = onSelectionChange
    )
}
