package rs.xor.rencfs.krencfs.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.RencfsComposeAdaptiveApp
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.ui.design.RencfsMaterialDarkTheme


@Composable
@Preview(device = "id:pixel_9_pro_xl")
fun PreviewApplication() {
    RencfsMaterialDarkTheme {
        RencfsComposeAdaptiveApp(deviceType = DisplayType.Phone)
    }
}
