package rs.xor.rencfs.krencfs.screen.walkthrough

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Step $currentStep/$totalSteps",
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier
    )
}
