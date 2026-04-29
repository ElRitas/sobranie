package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ui.screens.authorBooks.AuthorBooksScreen
import com.example.ui.screens.bbkBooks.BbkBooksScreen
import com.example.ui.screens.bookDetail.BookDetailScreen
import com.example.ui.screens.bookIssuance.BookIssuanceScreen
import com.example.ui.screens.bookList.BookListScreen
import com.example.ui.screens.bookReservation.BookReservationScreen
import com.example.ui.screens.formScreen.addEntity.AddEntityScreen
import com.example.ui.screens.formScreen.editEntity.EditEntityScreen
import com.example.ui.screens.login.LoginScreen
import com.example.ui.screens.profileScreen.ProfileScreen
import com.example.ui.screens.register.RegisterScreen
import com.example.ui.screens.selectScreen.apuSelect.SelectApuScreen
import com.example.ui.screens.selectScreen.authorSelect.SelectAuthorScreen
import com.example.ui.screens.selectScreen.bbkSelect.SelectBbkScreen
import com.example.ui.screens.selectScreen.bookSelect.SelectBookScreen
import com.example.ui.screens.selectScreen.publisherSelect.SelectPublisherScreen
import com.example.ui.screens.selectScreen.userSelect.SelectUserScreen
import com.example.ui.screens.userFavorite.UserFavoriteScreen
import com.example.ui.screens.userIssuance.UserIssuanceScreen
import com.example.ui.screens.userQueue.UserQueueScreen
import com.example.ui.screens.userReservation.UserReservationScreen
import com.example.ui.screens.userScreen.UserScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.BookList.route,
        modifier = modifier
    ) {
        composable(
            route = "bookList?query={query}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            BookListScreen(navController = navController)
        }
        composable(route = Screen.BookDetail.route) {
            BookDetailScreen(navController = navController)
        }
        composable(
            route = Screen.AuthorBooks.route,
            arguments = listOf(navArgument("authorId") { type = NavType.StringType })
        ) {
            AuthorBooksScreen(navController = navController)
        }
        composable(
            route = Screen.BbkBooks.route,
            arguments = listOf(navArgument("bbkId") { type = NavType.StringType })
        ) {
            BbkBooksScreen(navController = navController)
        }

        composable(
            route = Screen.Login.route,
        ) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.Register.route,
        ) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = Screen.User.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserScreen(navController = navController)
        }
        composable(
            route = Screen.UserFavorite.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserFavoriteScreen(navController = navController)
        }
        composable(
            route = Screen.UserReservation.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserReservationScreen(navController = navController)
        }
        composable(
            route = Screen.UserProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            ProfileScreen()
        }
        composable(
            route = Screen.UserIssuance.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserIssuanceScreen(navController = navController)
        }
        composable(
            route = Screen.UserQueue.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) {
            UserQueueScreen(navController = navController)
        }
        composable(
            route = Screen.AddEntity.route,
        ) {
            AddEntityScreen(navController = navController)
        }
        composable(
            route = Screen.EditEntity.route
        ) {
            EditEntityScreen(navController = navController)
        }
        composable(
            route = Screen.SelectAuthor.route
        ) {
            SelectAuthorScreen(navController = navController)
        }
        composable(
            route = Screen.SelectBbk.route
        ) {
            SelectBbkScreen(navController = navController)
        }
        composable(
            route = Screen.SelectPublisher.route
        ) {
            SelectPublisherScreen(navController = navController)
        }
        composable(
            route = Screen.SelectApu.route
        ) {
            SelectApuScreen(navController = navController)
        }
        composable(
            route = Screen.SelectBook.route
        ) {
            SelectBookScreen(navController = navController)
        }
        composable(
            route = Screen.SelectUser.route
        ) {
            SelectUserScreen(navController = navController)
        }
        composable(
            route = Screen.BookReservation.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            BookReservationScreen(navController = navController)
        }
        composable(
            route = Screen.BookIssuance.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) {
            BookIssuanceScreen(navController = navController)
        }
    }
}
