package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                InfoItem(
                    title = "Version",
                    content = "1.0.0",
                )
                InfoItem(
                    title = "Description",
                    content = "Rencfs is an encrypted filesystem implementation in Kotlin",
                )
                InfoItem(
                    title = "License",
                    content = "MIT License",
                )
                InfoItem(
                    title = "Repository",
                    content = "github.com/radumarias/rencfs-kotlin",
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    title: String,
    content: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
