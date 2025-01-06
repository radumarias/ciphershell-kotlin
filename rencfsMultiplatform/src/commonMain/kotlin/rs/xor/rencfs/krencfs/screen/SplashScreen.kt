package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.application_icon
import krencfs.rencfsmultiplatform.generated.resources.application_name
import krencfs.rencfsmultiplatform.generated.resources.button_label_add_folder
import krencfs.rencfsmultiplatform.generated.resources.welcome_screen_welcome_text
import krencfs.rencfsmultiplatform.generated.resources.welcome_screen_welcome_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun SplashScreen() {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                modifier = Modifier.wrapContentSize(),
                imageVector = vectorResource(Res.drawable.application_icon),
                contentDescription = stringResource(Res.string.application_name),
                contentScale = ContentScale.Inside
            )
        }
    }
}
