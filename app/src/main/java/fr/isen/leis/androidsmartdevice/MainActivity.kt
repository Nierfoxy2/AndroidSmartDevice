package fr.isen.leis.androidsmartdevice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.leis.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidSmartDeviceTheme {
                MainScreen {
                    startActivity(Intent(this, ScanActivity::class.java))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onStartScan: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Barre bleue avec le titre
        TopAppBar(
            title = { Text("AndroidSmartDevice", color = Color.White) },
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF1976D2))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Texte de bienvenue
        Text(
            text = "Bienvenue dans votre application\nSmart Device",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2), // Bleu
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Texte explicatif
        Text(
            text = "Pour démarrer vos interactions avec les appareils BLE\nenvironnants, cliquez sur commencer",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Icône Bluetooth
        Image(
            painter = painterResource(id = R.drawable.bl_icon), // Remplace avec ton icône Bluetooth
            contentDescription = "Bluetooth Icon",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bouton Commencer
        Button(
            onClick = onStartScan,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.8f)
                .height(50.dp)
        ) {
            Text(text = "COMMENCER", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
