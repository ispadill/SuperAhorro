package com.example.superahorro.ui

import android.view.Surface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.ModeloDominio.Sesion
import com.example.superahorro.R

/**
 * Pantalla principal que muestra el perfil del usuario con sus datos personales.
 *
 * @param onEditProfileClicked Callback para editar el perfil
 * @param onHomeButtonClicked Callback para navegar al inicio
 * @param onSearchClicked Callback para realizar búsquedas
 * @param onProfileClicked Callback para acceder al perfil
 * @param onFavoritesClicked Callback para acceder a favoritos
 * @param modifier [Modifier] personalizable para el diseño de la pantalla
 */
@Composable
fun ProfileScreen(
    onEditProfileClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,

    modifier: Modifier = Modifier,
) {
    val usuarioLogueado = Sesion.usuario
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xfff6bc66), // Fondo de la pantalla
        bottomBar = {
            BottomNavigationBar(onHomeButtonClicked,
                onSearchClicked,
                onProfileClicked,
                onFavoritesClicked)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(Color(0xfff6bc66))
                .padding(innerPadding), // Agregar el padding del Scaffold
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo de la aplicación
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, top = 50.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp)) {
                    Image(
                        painter = painterResource(R.drawable.logoapp),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

            // Información del perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {


                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(

                            text = "Nombre de Usuario: ${usuarioLogueado?.id}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(

                            text = "Nombre Completo: ${usuarioLogueado?.nombre}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.gmail),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(

                            text = "Correo: ${usuarioLogueado?.correo}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(bottom = 150.dp)) {
                    Button(
                        onClick = onEditProfileClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xfff55c7a),
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "Editar Perfil",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


/**
 * Previsualización de la pantalla de perfil para Android Studio.
 *
 * Muestra una representación estática del diseño para facilitar el desarrollo.
 */
@Preview(showBackground = true)
@Composable
fun PantallaPerfilPreview() {

        ProfileScreen(
            onEditProfileClicked = {},
            onHomeButtonClicked = {},
            onSearchClicked = {},
            onProfileClicked = {},
            onFavoritesClicked = {},
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))

}