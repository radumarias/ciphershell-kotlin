package rs.xor.rencfs.krencfs.ui.customcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.xor.rencfs.krencfs.ui.design.DesignSystem
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme
import rs.xor.rencfs.krencfs.ui.design.backgroundAngularGradient
import rs.xor.rencfs.krencfs.ui.design.customcomponents.AutoDismissibleSnackBar

@Preview(device = "id:pixel_9_pro_xl")
@Composable
fun Preview_AutoDismissibleSnackBar_SnackySnack() {
    RencfsMaterialDarkTheme {
        Box {
            Surface {
                AutoDismissibleSnackBar(
                    modifier = Modifier
                        .padding(8.dp)
                        .backgroundAngularGradient(DesignSystem.Colors.Gradient.Oxidized_Copper)
                        .wrapContentSize()
                        .align(alignment = Alignment.BottomCenter),
                    message = "Snacky Snack >= Multiplatform",
                )
            }
        }
    }
}
