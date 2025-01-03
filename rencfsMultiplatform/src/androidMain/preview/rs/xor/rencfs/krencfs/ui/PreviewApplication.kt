package rs.xor.rencfs.krencfs.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.DeviceType
import rs.xor.rencfs.krencfs.RencfsComposeAdaptiveApp


@Composable
@Preview(device = "id:pixel_9_pro_xl")
fun PreviewApplication() {
    RencfsMaterialDarkTheme {
        RencfsComposeAdaptiveApp(deviceType = DeviceType.Phone)
    }
}
