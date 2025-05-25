package com.example.superahorro.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.Surface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.ModeloDominio.Sesion
import com.example.superahorro.ModeloDominio.Sesion.usuario
import com.example.superahorro.R
import java.io.File


/**
 * Pantalla que muestra el perfil del usuario con sus datos personales.
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
    val context = LocalContext.current
    val viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var reloadTrigger by remember { mutableStateOf(false) }

    LaunchedEffect(reloadTrigger) {
        viewModel.loadCurrentUser()
    }

    val imagenPerfil by remember(uiState.usuario?.imagenPerfilUri, reloadTrigger) {
        derivedStateOf {
            viewModel.cargarImagenPerfil(context, uiState.usuario?.id ?: "")
        }
    }

    var color: Color
    var colorBoxBorder: Color
    var colorCardBorder: Color


    if(isSystemInDarkTheme()){
        color = Color(0xFF3F51B5)
        colorBoxBorder = Color(0xFF9341A4)
        colorCardBorder = Color.White
    }else{
        color = Color(0xfff68c70)
        colorBoxBorder = Color(0xfff55c7a)
        colorCardBorder = Color.Black
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        //containerColor = Color(0xfff6bc66),
        bottomBar = {
            BottomNavigationBar(
                onHomeButtonClicked,
                onSearchClicked,
                onProfileClicked,
                onFavoritesClicked
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                //.background(Color(0xfff6bc66))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Añade espacio uniforme entre elementos
        ) {
            Spacer(modifier = Modifier.height(16.dp)) // Espacio superior adicional

            // Foto de perfil (sin otros cambios)
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, colorBoxBorder, CircleShape)
            ) {
                if (uiState.usuario != null) {
                    Image(
                        bitmap = imagenPerfil.asImageBitmap(),
                        contentDescription = "Foto de perfil de ${uiState.usuario!!.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center
                    )
                }
            }

            // Resto del contenido exactamente igual que antes
            uiState.usuario?.let { usuario ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nombre de Usuario: ${usuario.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Text(
                        text = "Nombre Completo: ${usuario.nombre}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.Black,
                        textAlign = TextAlign.Center
                    )

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
                            text = "Correo: ${usuario.correo}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            //color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    reloadTrigger = !reloadTrigger
                    onEditProfileClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    //containerColor = Color(0xfff55c7a),
                    //contentColor = Color.Black
                ),
            ) {
                Text(
                    text = "Editar Perfil",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    //color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}