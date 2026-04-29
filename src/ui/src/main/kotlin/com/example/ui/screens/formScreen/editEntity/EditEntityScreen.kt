package com.example.ui.screens.formScreen.editEntity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.ui.screens.formScreen.editEntity.editFormScreen.ApuFormScreen
import com.example.ui.screens.formScreen.editEntity.editFormScreen.AuthorFormScreen
import com.example.ui.screens.formScreen.editEntity.editFormScreen.BbkFormScreen
import com.example.ui.screens.formScreen.editEntity.editFormScreen.BookFormScreen
import com.example.ui.screens.formScreen.editEntity.editFormScreen.EditEntityType
import com.example.ui.screens.formScreen.editEntity.editFormScreen.PublisherFormScreen
import com.example.ui.screens.formScreen.editEntity.editFormScreen.UserFormScreen

@Composable
fun EditEntityScreen(
    navController: NavController,
    viewModel: EditEntityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val selectedType by remember { viewModel::selectedType }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Изменить сущность", style = MaterialTheme.typography.headlineMedium)
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        if (viewModel.buttonVisibility) {
                            Button(
                                onClick = { viewModel.deleteEntity() },
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Удалить")
                            }
                            Button(
                                onClick = { viewModel.editEntity() },
                            ) {
                                Text("Сохранить")
                            }
                        }
                    }
                    if (state is EditEntityState.Error) {
                        Text(
                            text = (state as EditEntityState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (state is EditEntityState.Success) {
                        Text(
                            "Успешно изменено",
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
                    EditEntityType.AUTHOR -> AuthorFormScreen(viewModel, navController)
                    EditEntityType.BOOK -> BookFormScreen(viewModel, navController)
                    EditEntityType.PUBLISHER -> PublisherFormScreen(viewModel, navController)
                    EditEntityType.APU -> ApuFormScreen(viewModel, navController)
                    EditEntityType.BBK -> BbkFormScreen(viewModel, navController)
                    EditEntityType.USER -> UserFormScreen(viewModel, navController)
                }

                Spacer(modifier = Modifier.height(80.dp)) // чтобы форма не залезала под кнопку
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityTypeDropdown(selectedType: EditEntityType, onTypeSelected: (EditEntityType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val types = EditEntityType.entries

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
