package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.chatappui.FriendsListScreen
import com.example.chatapp.navigation.Screen
import com.example.chatapp.ui.*
import com.example.chatapp.ui.theme.ChatappTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatappTheme {
                val navController = rememberNavController()
                val isLoggedIn = FirebaseAuth.getInstance().currentUser!=null
                val startDestination = if(isLoggedIn) Screen.FriendsList.route
                                        else Screen.Login.route

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(Screen.Login.route) {
                            LoginScreen(navController)
                        }
                        composable(Screen.Signup.route) {
                            SignupScreen(navController)
                        }
                        composable(Screen.FriendsList.route) {
                            FriendsListScreen(navController = navController,
                                onCreateGroup = {
                                    groupName, selectedMembers ->
                                    navController.navigate("group_chat")
                                })
                        }
                        composable(Screen.Chat.route) { backStackEntry ->
                            val friendId = backStackEntry.arguments?.getString("friendId") ?: ""
                            ChatScreen(navController, friendId)
                        }
                    }
                }
            }
        }
    }
}
