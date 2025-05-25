
package com.example.superahorro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.superahorro.Datos.BaseDeDatos
import com.example.superahorro.Datos.DatosPrueba
import com.example.superahorro.ui.PantallaInicio
import com.example.superahorro.ui.theme.SuperAhorroTheme
import kotlinx.coroutines.launch

/**
 * Clase principal, que llama a la inicialización de la aplicación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            try {
                // Usar el contexto de la actividad (this@MainActivity)
                DatosPrueba.cargarDatosPrueba(this@MainActivity)
            } catch (e: Exception) {
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperAhorroTheme {
                SuperAhorroApp()
            }
        }
    }
}
