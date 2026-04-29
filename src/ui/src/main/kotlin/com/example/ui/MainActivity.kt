package com.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ui.component.Menu
import com.example.ui.navigation.NavGraph
import com.example.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var searchText by rememberSaveable { mutableStateOf("") }

            Scaffold(
                topBar = {
                    Menu(
                        navController = navController,
                        searchText = searchText,
                        onSearchChange = { searchText = it },
                        onSearchSubmit = {
                            val query = searchText.trim()
                            if (query.isNotEmpty()) {
                                navController.navigate(Screen.BookList.createRoute(query))
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavGraph(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
