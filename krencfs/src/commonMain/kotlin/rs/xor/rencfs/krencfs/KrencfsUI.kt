package rs.xor.rencfs.krencfs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import rs.xor.rencfs.krencfs.design.custom.AutoDismissibleSnackBar

@Composable
fun NavigationPanel(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentSize(),
                text = "Vaults"
            )
            Divider()
        }
    }
}

@Composable
fun ContentPanel(modifier: Modifier = Modifier) {
    Box(modifier = modifier)
    {
        Column(modifier = Modifier.padding(start = 20.dp)) {
            Text(
                modifier = Modifier.padding(10.dp).wrapContentSize(),
                text = "Vault Detail"
            )
            Divider(color = MaterialTheme.colors.surface)
            val coroutineScope = rememberCoroutineScope()
            var name by remember { mutableStateOf("") }
            var mountPoint by remember { mutableStateOf("") }
            var dataDir by remember { mutableStateOf("") }
            var isEnabled by remember { mutableStateOf(true) }
            Column (modifier = Modifier.padding(10.dp)) {
                Button(
                    onClick = {
//                        isEnabled = !isEnabled
                        coroutineScope.launch {
                            println("name= $name, isEnabled = $isEnabled, mountPoint = $mountPoint, dataDir = $dataDir")
                        }
                    },
                    enabled = isEnabled
                ) {
                    Text(if (isEnabled) "Lock" else "Unlock")
                }

                Row (modifier = Modifier.padding(10.dp)) {
                    Text("Name:")
                    OutlinedTextField(value = name, onValueChange = {
                        name = it
                    })

                }
                Row (modifier = Modifier.padding(10.dp)) {
                    Text("Mount Point:")
                    OutlinedTextField(value = mountPoint, onValueChange = {
                        mountPoint = it
                    })
                }
                Row (modifier = Modifier.padding(10.dp)) {
                    Text("Data Dir:")
                    OutlinedTextField(value = dataDir, onValueChange = {
                        dataDir = it
                    })
                    // FileKit Compose
                    val launcher = rememberDirectoryPickerLauncher(
                        title = "Pick a directory",
                        initialDirectory = dataDir
                    ) { directory ->
                        directory?.path?.apply {
                            dataDir = this
                        }
                    }
                    IconButton( onClick = {
                        launcher.launch()
                    })
                    {
                        Icon(Icons.Outlined.MoreVert,
                            contentDescription = null,
                        )
                    }
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
                Divider()
                Row {
                    NavigationPanel(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxSize()
                    )
                    ContentPanel(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxSize()
                    )
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
