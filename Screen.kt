package com.example.chatapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object FriendsList : Screen("friends_list")
    object Group : Screen("group_chat/{groupId}"){
        fun createRoute(friendId: String) = "chat/$friendId"
    }
    object Chat : Screen("chat/{friendId}") {
        fun createRoute(friendId: String) = "chat/$friendId"
    }
}
