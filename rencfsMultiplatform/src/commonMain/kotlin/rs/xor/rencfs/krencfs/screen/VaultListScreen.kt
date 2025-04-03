package rs.xor.rencfs.krencfs.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import krencfs.rencfsmultiplatform.generated.resources.vault_list_add_vault_button
import krencfs.rencfsmultiplatform.generated.resources.vault_list_item_name_unnamed
import krencfs.rencfsmultiplatform.generated.resources.vault_list_item_no_mount_point
import krencfs.rencfsmultiplatform.generated.resources.welcome_screen_welcome_text
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.display.DisplayType
import rs.xor.rencfs.krencfs.screen.usecase.OnCreateVaultUseCase
import rs.xor.rencfs.krencfs.screen.usecase.OnVaultSelectedUseCase
import rs.xor.rencfs.krencfs.screen.usecase.SelectVaultUseCaseParams
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenState
import rs.xor.rencfs.krencfs.screen.usecase.VaultListScreenUseCase
import rs.xor.rencfs.krencfs.ui.design.DesignSystem.Dimensions.paddingNormal

@Composable
fun VaultListScreen(firstTime: Boolean) {
    object : KoinComponent {
        val viewState: VaultListScreenState by inject { parametersOf(firstTime) }
        val useCase: VaultListScreenUseCase by inject()
    }.apply {
        val vaults by viewState.vaults.collectAsState(initial = emptyMap())

        if (viewState.firstStart && vaults.isEmpty()) {
            VaultsEmptyStateScreen(onAddFolderClick = useCase.onCreateVault)
        } else {
            VaultListContent(
                vaults = vaults,
                onVaultSelected = useCase.onVaultSelected,
                onCreateVault = useCase.onCreateVault,
            )
        }
    }
}

@Composable
fun VaultListContent(
    vaults: Map<String, VaultModel>,
    onVaultSelected: OnVaultSelectedUseCase,
    onCreateVault: OnCreateVaultUseCase,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            VaultList(
                vaults = vaults,
                onVaultClick = { onVaultSelected(SelectVaultUseCaseParams(it)) },
            )
        }
        onCreateVault.apply {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(paddingNormal),
                onClick = { onCreateVault() },
            ) {
                Icon(Icons.Filled.Add, stringResource(Res.string.vault_list_add_vault_button))
            }
        }
    }
}

@Composable
private fun VaultList(
    vaults: Map<String, VaultModel>,
    onVaultClick: (String) -> Unit,
) {
    LazyColumn {
        items(vaults.entries.toList(), key = { it.key }) { entry ->
            VaultListItem(vault = entry.value, onClick = { onVaultClick(entry.key) })
            if (entry.key != vaults.keys.last()) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun VaultListItem(
    vault: VaultModel,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(paddingNormal)) {
            Text(
                text = vault.name.let {
                    it.ifEmpty {
                        stringResource(Res.string.vault_list_item_name_unnamed)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = vault.mountPoint.let { it.ifEmpty { stringResource(Res.string.vault_list_item_no_mount_point) } },
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun VaultsEmptyStateScreen(
    onAddFolderClick: OnCreateVaultUseCase,
) {
    Surface {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingNormal),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Image(
                    imageVector = vectorResource(Res.drawable.application_icon),
                    contentDescription = stringResource(Res.string.application_name),
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.size(220.dp).clip(CircleShape).background(Color.White),
                )
            }
            val displayType: DisplayType = DisplayType.Desktop; // LocalDisplayTypeProvider.current
            // if (displayType == DisplayType.Mobile) { 0.5f } else { 1f }
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(
                            when (displayType) {
                                DisplayType.Phone -> 0.5f
                                else -> 1f
                            },
                        ).padding(vertical = paddingNormal),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.welcome_screen_welcome_text),
                )
            }

            item {
                Button(onClick = { onAddFolderClick() }) {
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        text = stringResource(Res.string.button_label_add_folder).uppercase(),
                    )
                }
            }
        }
    }
}
