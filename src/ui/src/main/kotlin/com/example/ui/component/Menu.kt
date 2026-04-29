package com.example.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ui.common.enums.UserRole
import com.example.ui.navigation.Screen
import com.example.ui.util.UserStore

val searchBarVisibleRoutes = listOf(
    Screen.BookList.route,
    Screen.AuthorBooks.route,
    Screen.BbkBooks.route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navController: NavController,
    searchText: String,
    onSearchChange: (String) -> Unit,
    onSearchSubmit: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSearchVisible =
        searchBarVisibleRoutes.any { currentRoute?.startsWith(it.substringBefore("/{")) == true }

    var expanded by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = { Text("Моя библиотека") },
            actions = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Меню")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Домой") },
                        onClick = {
                            expanded = false
                            navController.navigate(Screen.BookList.route) {
                                popUpTo(0)
                            }
                        }
                    )
                    if (UserStore.getRole() == null) {
                        DropdownMenuItem(
                            text = { Text("Войти") },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Зарегистрироваться") },
                            onClick = {
                                expanded = false
                                navController.navigate(Screen.Register.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    } else {
                        when (UserStore.getRole()) {
                            UserRole.READER -> {
                                DropdownMenuItem(
                                    text = { Text("Профиль") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(
                                            Screen.UserProfile.createRoute(
                                                UserStore.getId()!!
                                            )
                                        ) {
                                            popUpTo(0)
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Избранное") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(
                                            Screen.UserFavorite.createRoute(
                                                UserStore.getId()!!
                                            )
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Мои заказы") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(
                                            Screen.UserReservation.createRoute(
                                                UserStore.getId()!!
                                            )
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Мои книги") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(
                                            Screen.UserIssuance.createRoute(
                                                UserStore.getId()!!
                                            )
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Очереди") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(
                                            Screen.UserQueue.createRoute(
                                                UserStore.getId()!!
                                            )
                                        )
                                    }
                                )

                            }

                            UserRole.LIBRARIAN -> {}
                            UserRole.MODERATOR -> {
                                DropdownMenuItem(
                                    text = { Text("Добавить") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(Screen.AddEntity.route)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Изменить") },
                                    onClick = {
                                        expanded = false
                                        navController.navigate(Screen.EditEntity.route)
                                    }
                                )
                            }

                            null -> {}
                        }
                        DropdownMenuItem(
                            text = { Text("Выйти") },
                            onClick = {
                                expanded = false
                                UserStore.clear()
                                navController.navigate(Screen.BookList.route) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                }
            }
        )
        if (isSearchVisible) {
            SearchBar(
                query = searchText,
                placeholder = "Поиск книг...",
                onQueryChange = onSearchChange,
                onSearchSubmit = onSearchSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}
