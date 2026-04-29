package com.example.ui.screens.formScreen.addEntity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.screens.formScreen.addEntity.addFormScreen.AddEntityType
import com.example.ui.screens.formScreen.addEntity.addFormScreen.ApuFormScreen
import com.example.ui.screens.formScreen.addEntity.addFormScreen.AuthorFormScreen
import com.example.ui.screens.formScreen.addEntity.addFormScreen.BbkFormScreen
import com.example.ui.screens.formScreen.addEntity.addFormScreen.BookFormScreen
import com.example.ui.screens.formScreen.addEntity.addFormScreen.PublisherFormScreen

@Composable
fun AddEntityScreen(
    navController: NavController,
    viewModel: AddEntityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedType by remember { viewModel::selectedType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Добавить сущность", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Выбор типа сущности
        EntityTypeDropdown(
            selectedType = selectedType,
            onTypeSelected = {
                viewModel.selectedType = it
                viewModel.changeEntityType()
            }
        )

        Scaffold(
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { viewModel.addEntity() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Сохранить")
                    }

                    if (state is AddEntityState.Error) {
                        Text(
                            text = (state as AddEntityState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (state is AddEntityState.Success) {
                        Text(
                            "Успешно добавлено",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                when (selectedType) {
                    AddEntityType.AUTHOR -> AuthorFormScreen(viewModel)
                    AddEntityType.BOOK -> BookFormScreen(viewModel, navController)
                    AddEntityType.PUBLISHER -> PublisherFormScreen(viewModel)
                    AddEntityType.APU -> ApuFormScreen(viewModel, navController)
                    AddEntityType.BBK -> BbkFormScreen(viewModel)
                }

                Spacer(modifier = Modifier.height(80.dp)) // чтобы форма не залезала под кнопку
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityTypeDropdown(selectedType: AddEntityType, onTypeSelected: (AddEntityType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val types = AddEntityType.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Тип сущности") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
