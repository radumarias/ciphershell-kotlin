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
import krencfs.rencfsmultiplatform.generated.resources.Res
import krencfs.rencfsmultiplatform.generated.resources.app_description
import krencfs.rencfsmultiplatform.generated.resources.app_license
import krencfs.rencfsmultiplatform.generated.resources.app_repository
import krencfs.rencfsmultiplatform.generated.resources.app_version
import krencfs.rencfsmultiplatform.generated.resources.title_description
import krencfs.rencfsmultiplatform.generated.resources.title_license
import krencfs.rencfsmultiplatform.generated.resources.title_repository
import krencfs.rencfsmultiplatform.generated.resources.title_version
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.GlobalContext.get
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingExtraSmall
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal
import rs.xor.rencfs.krencfs.ui.design.customcomponents.ClickableText
import rs.xor.rencfs.krencfs.utils.UrlLauncher

@Composable
fun AboutScreen() {
    val urlLauncher: UrlLauncher = get().get()
    val repositoryUrl = "https://${stringResource(Res.string.app_repository)}"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingNormal),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(paddingNormal)) {
                InfoItem(
                    title = stringResource(Res.string.title_version),
                    content = stringResource(Res.string.app_version),
                )
                InfoItem(
                    title = stringResource(Res.string.title_description),
                    content = stringResource(Res.string.app_description),
                )
                InfoItem(
                    title = stringResource(Res.string.title_license),
                    content = stringResource(Res.string.app_license),
                )
                InfoItem(
                    title = stringResource(Res.string.title_repository),
                    content = stringResource(Res.string.app_repository),
                    isClickable = true,
                    onClick = { urlLauncher.openUrl(repositoryUrl) },
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    title: String,
    content: String,
    isClickable: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = paddingExtraSmall),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        ClickableText(
            text = content,
            onClick = onClick,
            isClickable = isClickable,
        )
    }
}
