package com.example.ui.screens.formScreen.editEntity.editFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.common.enums.UserRole
import com.example.ui.common.json.appJson
import com.example.ui.component.EntitySelector
import com.example.ui.items.DetailRow
import com.example.ui.model.UserModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.UserMapper

@Composable
fun UserFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.userForm

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- Пользователь ---
    val userJson = savedStateHandle?.get<String>("selectedUser")
    val selectedUser = userJson?.let {
        appJson.decodeFromString(UserModel.serializer(), it)
    }

    LaunchedEffect(selectedUser) {
        selectedUser?.let { user ->
            if (user.id != form.id) {
                viewModel.userForm = UserMapper.toForm(user)
            }
            viewModel.buttonVisibility = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        EntitySelector(
            label = "Пользователь",
            showingValue = if (form.phoneNumber.isNotBlank()) form.name else "",
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectUser.route) }
        )
        if (selectedUser != null) {
            Spacer(modifier = Modifier.height(16.dp))

            DetailRow("Фамилия", form.surname)
            DetailRow("Имя", form.name)
            DetailRow("Отчество", form.secondName)
            DetailRow("Номер телефона", form.phoneNumber)

            Spacer(Modifier.height(16.dp))
            RoleDropdown(form.role) { newRole ->
                viewModel.userForm = viewModel.userForm.copy(role = newRole)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleDropdown(selectedType: UserRole, onTypeSelected: (UserRole) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val types = UserRole.entries

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedType.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Роль") },
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
                    text = { Text(type.name) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
