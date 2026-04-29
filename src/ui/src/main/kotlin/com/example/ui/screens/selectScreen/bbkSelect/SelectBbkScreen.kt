package com.example.ui.screens.selectScreen.bbkSelect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.component.SearchBar
import com.example.ui.items.BbkItem
import com.example.ui.model.BbkModel
import com.example.ui.screens.selectScreen.SearchState


@Composable
fun SelectBbkScreen(
    navController: NavController,
    viewModel: SelectBbkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Поле поиска
        SearchBar(
            query = query,
            placeholder = "Поиск ББК",
            onQueryChange = { query = it },
            onSearchSubmit = { viewModel.search(query) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        // Состояния
        when (state) {
            is SearchState.Empty -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Введите ББК код и нажмите поиск")
                }
            }

            is SearchState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is SearchState.Error -> {
                val error = (state as SearchState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
                }
            }

            is SearchState.Success -> {
                val bbkList: List<BbkModel> = (state as SearchState.Success).results
                if (bbkList.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ББК код не найден")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(bbkList) { bbk ->
                            BbkItem(bbk) {
                                val bbkJson =
                                    appJson.encodeToString(BbkModel.serializer(), bbk)
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("selectedBbk", bbkJson)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}
