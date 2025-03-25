package fr.isen.leis.androidsmartdevice

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen(
    name: String,
    address: String,
    rssi: Int,
    onBack: () -> Unit,
    onConnectClick: () -> Unit,
    connectionStatus: String,
    isConnected: Boolean,
    ledStates: List<Boolean>,
    onLedToggle: (Int) -> Unit,
    isSubscribed: Boolean,
    onSubscribeToggle: (Boolean) -> Unit,
    counter: Int
) {
    val ledColors = listOf(
        Color(0xFF1976D2), // LED 1 - Bleu
        Color(0xFF4CAF50), // LED 2 - Vert
        Color(0xFFF44336)  // LED 3 - Rouge
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AndroidSmartDevice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isConnected) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Connexion à :", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Adresse : $address", fontSize = 14.sp)
                Text("RSSI : $rssi dBm", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(connectionStatus, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onConnectClick) {
                    Text("Se connecter")
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Votre Sapin De Noël", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                Spacer(modifier = Modifier.height(24.dp))
                Text("Vos Guirlandes", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                Row(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ledStates.forEachIndexed { index, isOn ->
                        val color = ledColors.getOrNull(index) ?: Color.Gray
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Button(
                                onClick = { onLedToggle(index) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isOn) color else Color.LightGray
                                ),
                                modifier = Modifier
                                    .height(64.dp)
                                    .width(100.dp) // un peu plus large
                            ) {
                                Text(
                                    text = "LED ${index + 1}",
                                    color = Color.White,
                                    maxLines = 1 // 👈 empêche retour à la ligne
                                )
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Abonnez vous pour recevoir\nle nombre d'incrémentation",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Checkbox(
                        checked = isSubscribed,
                        onCheckedChange = { onSubscribeToggle(it) }
                    )
                    Text("RECEVOIR")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Nombre : $counter", fontSize = 20.sp)
            }
        }
    }
}