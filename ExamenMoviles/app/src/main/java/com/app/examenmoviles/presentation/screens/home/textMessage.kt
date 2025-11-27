package com.app.examenmoviles.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun textMessage(onclickBack: () -> Unit) {
    Column(
        modifier =
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
    ) {
        Text(
            text = "Llegaste a esta parte de la aplicación",
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onclickBack) {
            Text("Regresar")
        }
    }
}
