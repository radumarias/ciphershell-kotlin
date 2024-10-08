package rs.xor.rencfs.krencfs.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.data.sqldelight.SQLDelightDB
import rs.xor.rencfs.krencfs.data.vault.VaultModel
import rs.xor.rencfs.krencfs.ui.customcomponents.AutoDismissibleSnackBar

@Composable
fun NavigationPanel(
    modifier: Modifier = Modifier,
    items: Map<String, VaultModel>,
    itemClicked: (String, VaultModel) -> Unit,
) {
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
                ) {
                    Text(item.second.name)
                    HorizontalDivider(
                        modifier = Modifier.height(1.dp).fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EditVaultPanel(
    modifier: Modifier = Modifier,
    key: String,
    vaultModel: VaultModel,
    onSave: (VaultModel) -> Unit,
) {
    var vault by remember(key) { mutableStateOf(vaultModel) }
    Box(modifier = modifier)
    {
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                modifier = Modifier.padding(10.dp).wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Edit Vault"
            )
            HorizontalDivider()
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.padding(10.dp)) {
                    TextField(
                        placeholder = {
                            Text("Name")
                        },
                        value = vault.name,
                        onValueChange = {
                            vault = vault.copy(name = it)
                        }
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
                        }
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

@Composable
fun RencfsMainUI() {
    val scope = rememberCoroutineScope()
    var vaultKey by remember { mutableStateOf<String?>(null) }

    var vaults by remember { mutableStateOf<Map<String, VaultModel>>(emptyMap()) }

    LaunchedEffect(Unit) {
        scope.launch {
            SQLDelightDB.getVaultRepositoryAsync()
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
                                        SQLDelightDB.getVaultRepositoryAsync().addVault()
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
                    }
                    VerticalDivider()
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
                                    vaultModel = vaultModel,
                                    onSave = { updatedVault ->
                                        println("onSave $this")
                                        scope.launch {
                                            SQLDelightDB.getVaultRepositoryAsync().updateVault(
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
