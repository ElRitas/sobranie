package com.example.ui.screens.formScreen.editEntity.editFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.component.EntitySelector
import com.example.ui.model.ApuModel
import com.example.ui.model.BbkModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.ApuMapper

@Composable
fun ApuFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.apuForm

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- АПУ ---
    val apuJson = savedStateHandle?.get<String>("selectedApu")
    val selectedApu = apuJson?.let {
        appJson.decodeFromString(ApuModel.serializer(), it)
    }

    LaunchedEffect(selectedApu) {
        selectedApu?.let { apu ->
            if (apu.id != form.id) {
                viewModel.apuForm = ApuMapper.toForm(apu)
            }
            viewModel.buttonVisibility = true
        }
    }

    // --- ББК ---
    val bbkJson = savedStateHandle?.get<String>("selectedBbk")
    val selectedBbk = bbkJson?.let {
        appJson.decodeFromString(BbkModel.serializer(), it)
    }
    LaunchedEffect(selectedBbk) {
        selectedBbk?.let { bbk ->
            if (viewModel.apuForm.bbk != bbk) {
                viewModel.apuForm = viewModel.apuForm.copy(
                    bbk = bbk
                )
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EntitySelector(
            label = "АПУ",
            showingValue = if (form.term.isNotBlank()) form.term else "",
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectApu.route) }
        )

        if (selectedApu != null) {

            Column {
                OutlinedTextField(
                    value = form.term,
                    onValueChange = { viewModel.apuForm = form.copy(term = it) },
                    label = { Text("Ключевое слово*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = form.term.isBlank()
                )
                EntitySelector(
                    label = "ББК*",
                    showingValue = viewModel.apuForm.bbk?.code ?: "",
                    isError = viewModel.apuForm.bbk == null,
                    onSelectClick = { navController.navigate(Screen.SelectBbk.route) },
                )
            }
        }
    }
}
