package com.example.ui.navigation

import java.util.UUID

sealed class Screen(val route: String) {
    object BookList : Screen("bookList?query={query}") {
        fun createRoute(query: String? = null): String {
            return if (query.isNullOrEmpty()) {
                "bookList"
            } else {
                "bookList?query=$query"
            }
        }
    }

    object BookDetail : Screen("bookDetail")
    object AuthorBooks : Screen("author/books/{authorId}") {
        fun createRoute(authorId: UUID) = "author/books/$authorId"
    }

    object BbkBooks : Screen("bbk/books/{bbkId}") {
        fun createRoute(bbkId: UUID) = "bbk/books/$bbkId"
    }

    object UserFavorite : Screen("userFavorite/{userId}") {
        fun createRoute(userId: UUID) = "userFavorite/$userId"
    }

    object UserReservation : Screen("userReservation/{userId}") {
        fun createRoute(userId: UUID) = "userReservation/$userId"
    }

    object UserIssuance : Screen("userIssuance/{userId}") {
        fun createRoute(userId: UUID) = "userIssuance/$userId"
    }

    object UserQueue : Screen("userQueue/{userId}") {
        fun createRoute(userId: UUID) = "userQueue/$userId"
    }

    object UserProfile : Screen("userProfile/{userId}") {
        fun createRoute(userId: UUID) = "userProfile/$userId"
    }

    object BookReservation : Screen("bookReservation/{bookId}") {
        fun createRoute(bookId: UUID) = "bookReservation/$bookId"
    }

    object BookIssuance : Screen("bookIssuance/{bookId}") {
        fun createRoute(bookId: UUID) = "bookIssuance/$bookId"
    }

    object User : Screen("user/{userId}") {
        fun createRoute(userId: UUID) = "user/$userId"
    }

    object AddEntity : Screen("addEntity")
    object EditEntity : Screen("editEntity")

    object SelectAuthor : Screen("selectAuthor")
    object SelectBbk : Screen("selectBbk")
    object SelectPublisher : Screen("selectPublisher")
    object SelectApu : Screen("selectApu")
    object SelectBook : Screen("selectBook")
    object SelectUser : Screen("selectUser")

    object Login : Screen("login")
    object Register : Screen("register")

}
