package rs.xor.rencfs.krencfs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.data.Database
import rs.xor.rencfs.krencfs.data.domain.model.VaultDataModel
import rs.xor.rencfs.krencfs.design.custom.AutoDismissibleSnackBar

@Composable
fun NavigationPanel(
    modifier: Modifier = Modifier,
    items: Map<String, VaultDataModel>,
    itemClicked: (String, VaultDataModel) -> Unit,
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                state = rememberLazyListState(),
            ) {
                itemsIndexed(
                    items = items.toList(),
                    key = { _, entry -> entry.first })
                { _, item ->
                    Surface(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .clickable {
                                itemClicked.invoke(item.first, item.second)
                            },
                        color = Color(0xFF333333)
                    ) {
                        Text(item.second.name)
                        VerticalDivider(modifier = Modifier.height(1.dp).fillMaxWidth())
                    }
                }
            }
        }
    }
}


@Composable
fun EditVaultPanel(
    modifier: Modifier = Modifier,
    key: String,
    vault: VaultDataModel,
    onSave: (VaultDataModel) -> Unit,
) {
    var vault by remember {  mutableStateOf(vault)  }
    Box(modifier = modifier)
    {
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                modifier = Modifier.padding(10.dp).wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Edit Vault"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surface)
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.padding(10.dp)) {
                    TextField(
                        placeholder = {
                            Text("Name")
                        },
                        value = vault.name,
                        onValueChange = {
                            vault = vault.copy(name = it)
                        },
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                Row(modifier = Modifier.padding(10.dp)) {
                    TextField(
                        placeholder = {
                            Text(
                                "Mount point"
                            )
                        },
                        value = vault.mountPoint,
                        onValueChange = {
                            vault = vault.copy(mountPoint = it)
                        },
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                Row(modifier = Modifier.padding(10.dp)) {
                    TextField(
                        placeholder = {
                            Text("Data folder path")
                        },
                        value = vault.dataDir,
                        onValueChange = {
                            vault = vault.copy(dataDir = it)
                        },
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    // FileKit Compose
                    val launcher = rememberDirectoryPickerLauncher(
                        title = "Choose data folder",
                        initialDirectory = vault.dataDir
                    ) { directory ->
                        directory?.path?.apply {
                            vault = vault.copy(dataDir = this)
                        }
                    }
                    IconButton(onClick = {
                        launcher.launch()
                    })
                    {
                        Icon(
                            Icons.Outlined.MoreHoriz,
                            contentDescription = null,
                        )
                    }
                }
                Button(
                    onClick = {
                        onSave(vault)
                    },
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@Preview
@Composable
fun KrencfsUI() {
    val scope = rememberCoroutineScope()
    var vaultKey by remember { mutableStateOf<String?>(null) }

    var vaults by remember { mutableStateOf<Map<String, VaultDataModel>>(emptyMap()) }
    LaunchedEffect(Unit) {
        scope.launch {
            Database.getVaultRepository()
                .observeVaults()
                .collect { newVaults ->
                    println("newVaults: $newVaults")
                    vaults = newVaults
                }
        }
    }

    Surface {
        Scaffold(
            /*TODO*/
        )
        {
            val message by remember { mutableStateOf<String?>("Dummy snack autoclose showcase") }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                Row {

                    Column(
                        Modifier
                            .weight(0.3f)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .wrapContentSize()
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                modifier = Modifier
                                    .wrapContentSize(),

                                style = MaterialTheme.typography.bodyMedium,
                                text = "Vaults",
                            )
                            FloatingActionButton(
                                onClick = {
                                    // TODO: ask input via modal/dialog
                                    scope.launch {
                                        Database.getVaultRepository().addVault()
                                    }
                                }
                            ) {
                                Text("+")
                            }
                        }
                        HorizontalDivider()
                        NavigationPanel(
                            modifier = Modifier
                                .fillMaxSize(),
                            items = vaults,
                            itemClicked = { key, vault ->
                                println("$key, $vault")
                                vaultKey = key
                            }
                        )
                        VerticalDivider()
                    }
                    Box(
                        Modifier
                            .weight(0.7f)
                    )
                    {
                        vaultKey?.apply {
                            vaults.get(this)?.let { vaultModel ->
                                EditVaultPanel(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    key = this,
                                    vault = vaultModel,
                                    onSave = { updatedVault ->
                                        println("onSave $this")
                                        scope.launch {
                                            Database.getVaultRepository().updateVault(
                                                this@apply,
                                                updatedVault.name,
                                                updatedVault.dataDir,
                                                updatedVault.mountPoint
                                            )
                                        }
                                    }
                                )
                            }

                        } ?: Text(modifier = Modifier.align(alignment = Alignment.Center), text = "Empty State")
                    }
                }
                message?.apply {
                    AutoDismissibleSnackBar(
                        modifier = Modifier
                            .padding(8.dp)
                            .wrapContentSize()
                            .align(alignment = Alignment.BottomCenter),
                        message = this
                    )
                }
            }
        }
    }
}
