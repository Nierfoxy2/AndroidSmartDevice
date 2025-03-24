package fr.isen.leis.androidsmartdevice

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fr.isen.leis.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter

    // Liste des permissions à demander
    private val requiredPermissions = mutableListOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_ADVERTISE)
        }
    }.toTypedArray()

    // Gestionnaire de permission
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.all { it.value }
            if (!allGranted) {
                // Affichage d'un message si l'utilisateur refuse les permissions
                showToast("Les permissions Bluetooth sont requises pour scanner les appareils.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vérification du support Bluetooth
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter ?: run {
            showToast("Le Bluetooth n'est pas disponible sur cet appareil.")
            finish()
            return
        }

        // Vérification et demande des permissions
        checkAndRequestPermissions()

        setContent {
            AndroidSmartDeviceTheme {
                ScanScreen()
            }
        }
    }

    // Vérifie si toutes les permissions sont accordées
    private fun hasAllPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Demande les permissions si nécessaire
    private fun checkAndRequestPermissions() {
        if (!hasAllPermissions()) {
            permissionLauncher.launch(requiredPermissions)
        }
    }

    // Fonction pour afficher un message utilisateur
    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen() {
    var isScanning by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(listOf<String>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    fun checkBluetooth(): Boolean {
        return when {
            bluetoothAdapter == null -> {
                errorMessage = "Le Bluetooth n'est pas disponible sur cet appareil."
                false
            }
            !bluetoothAdapter.isEnabled -> {
                errorMessage = "Veuillez activer le Bluetooth pour scanner les appareils."
                false
            }
            else -> {
                errorMessage = null
                true
            }
        }
    }

    fun startScan() {
        if (checkBluetooth()) {
            isScanning = true
            devices = emptyList() // Initialisation de la liste des appareils détectés
        }
    }

    fun stopScan() {
        isScanning = false
        devices = emptyList() // Réinitialisation des appareils lorsqu'on arrête le scan
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("AndroidSmartDevice", color = Color.White) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF1976D2))
        )

        Spacer(modifier = Modifier.height(32.dp))

        errorMessage?.let {
            // Affichage du message d'erreur en rouge
            Text(
                text = it,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        if (isScanning) {
            ScanProgressUI(devices, onStopScan = { stopScan() })
        } else if (errorMessage == null) {
            ScanStartUI { startScan() }
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
