import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.improta.androidsmartdevice.screen.ScanActivity
import fr.isen.improta.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                HomeScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AndroidSmartDevice") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Bluetooth,
                contentDescription = "Bluetooth Icon",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenue dans votre application\nSmart Device",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Pour démarrer vos interactions avec les appareils BLE environnants, cliquez sur commencer.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(id = R.drawable.ble2),
                contentDescription = "Bluetooth Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                    val bluetoothAdapter = bluetoothManager.adapter

                    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                    when {
                        bluetoothAdapter == null -> {
                            showAlert(context, "Bluetooth non supporté", "Ce dispositif ne supporte pas le Bluetooth.")
                        }
                        !bluetoothAdapter.isEnabled -> {
                            showAlert(context, "Bluetooth désactivé", "Veuillez activer le Bluetooth pour continuer.")
                        }
                        !isLocationEnabled -> {
                            showAlert(context, "Localisation désactivée", "Veuillez activer la localisation pour scanner les appareils BLE.")
                        }
                        else -> {
                            context.startActivity(Intent(context, ScanActivity::class.java))
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Start Icon",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "COMMENCER",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                tint = Color.Gray,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

fun showAlert(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK", null)
        .show()
}
