package com.example.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EntitySelector(
    label: String,
    showingValue: String?,
    isError: Boolean,
    onSelectClick: () -> Unit
) {
    OutlinedTextField(
        value = showingValue ?: "",
        label = { Text(label) },
        onValueChange = {},
        isError = isError,
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onSelectClick) {
                Icon(Icons.Default.Search, contentDescription = label)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
