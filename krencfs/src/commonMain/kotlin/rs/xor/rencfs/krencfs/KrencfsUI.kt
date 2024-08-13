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
import org.jetbrains.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.design.custom.AutoDismissibleSnackBar
import rs.xor.rencfs.krencfs.data.domain.model.VaultDataModel

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
    Box(modifier = modifier)
    {
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                modifier = Modifier.padding(10.dp).wrapContentSize(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Edit Vault"
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.surface)
            var name by remember(key) { mutableStateOf(vault.name) }
            var mountPoint by remember(key) { mutableStateOf(vault.mountPoint) }
            var dataDir by remember(key) { mutableStateOf(vault.dataDir) }
            Column(modifier = Modifier.padding(10.dp)) {
                Row(modifier = Modifier.padding(10.dp)) {
                    TextField(
                        placeholder = {
                            Text("Name")
                        },
                        value = name,
                        onValueChange = {
                            name = it
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
                        value = mountPoint,
                        onValueChange = {
                            mountPoint = it
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
                        value = dataDir,
                        onValueChange = {
                            dataDir = it
                        },
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    // FileKit Compose
                    val launcher = rememberDirectoryPickerLauncher(
                        title = "Choose data folder",
                        initialDirectory = dataDir
                    ) { directory ->
                        directory?.path?.apply {
                            dataDir = this
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
                        val toSave = VaultDataModel(name, mountPoint, dataDir)
                        println("saving $name $toSave")
                        onSave.invoke(toSave)
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
                    var items by remember {
                        mutableStateOf<Map<String, VaultDataModel>?>(
                            null
                        )
                    }
                    var vaultKey by remember { mutableStateOf<String?>(null) }
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
                                    val mutableItems = items?.toMutableMap() ?: mutableMapOf()
                                    mutableItems.put(mutableItems.size.toString(), VaultDataModel("", "", ""))
                                    items = mutableItems
                                }
                            ) {
                                Text("+")
                            }
                        }
                        HorizontalDivider()
                        items?.apply {
                            NavigationPanel(
                                modifier = Modifier
                                    .fillMaxSize(),
                                items = this,
                                itemClicked = { key, vault ->
                                    println("$key, $vault")
                                    vaultKey = key
                                }
                            )
                        } ?: Text(
                            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                            text = "Empty state, Start by adding something"
                        )
                        VerticalDivider()
                    }
                    Box(
                        Modifier
                            .weight(0.7f)
                    )
                    {
                        vaultKey?.apply {
                            items?.get(this)?.let { vaultModel ->
                                EditVaultPanel(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    key = this,
                                    vault = vaultModel,
                                    onSave = { updatedVault ->
                                        println("onSave $this")
                                        val mutableItems = items?.toMutableMap() ?: mutableMapOf()
                                        mutableItems.replace(this@apply, updatedVault)
                                        items = mutableItems
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
