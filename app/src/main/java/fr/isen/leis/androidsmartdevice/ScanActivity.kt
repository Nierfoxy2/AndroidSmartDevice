package fr.isen.leis.androidsmartdevice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.leis.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen() {
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(emptyList<String>()) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Barre bleue avec titre
        TopAppBar(
            title = { Text("AndroidSmartDevice", color = Color.White) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF1976D2))
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!isScanning) {
            // Affichage avant le scan
            ScanStartUI { isScanning = true }
        } else {
            // Affichage du scan en cours
            ScanProgressUI(devices) { isScanning = false }
        }
    }
}

@Composable
fun ScanStartUI(onStartScan: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bouton Lancer le Scan BLE avec une icône de flèche
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartScan() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lancer le Scan BLE",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ar_icon), // Ajoute une icône flèche dans drawable
                contentDescription = "Start Scan",
                tint = Color.Gray
            )
        }

        // Ligne de séparation
        Divider(
            color = Color(0xFFB0BEC5), // Gris clair
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ScanProgressUI(devices: List<String>, onStopScan: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Texte d'état du scan
        Text(
            text = "Scan en cours...",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Green,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour arrêter le scan
        Button(
            onClick = { onStopScan() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Arrêter le Scan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Liste des appareils trouvés
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(devices) { device ->
                DeviceItem(deviceName = device)
            }
        }
    }
}

@Composable
fun DeviceItem(deviceName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Text(
            text = deviceName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}
