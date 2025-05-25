package com.example.superahorro.ui



import android.Manifest

import android.content.Context

import android.content.pm.PackageManager

import android.graphics.Bitmap

import android.graphics.BitmapFactory

import android.net.Uri

import android.os.Build

import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.Image

import androidx.compose.foundation.background

import androidx.compose.foundation.border

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.*

import androidx.compose.runtime.*

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

import androidx.core.content.ContextCompat

import androidx.core.content.FileProvider

import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.superahorro.Datos.Loggeado

import com.example.superahorro.ModeloDominio.Sesion

import com.example.superahorro.R

import java.io.File

import java.io.FileOutputStream



/**
 * Pantalla que muestra el perfil del usuario con sus datos personales y permite modificarlos.
 *
 * @param onAcceptChangesClicked Callback para modificar los datos del perfil
 * @param onHomeButtonClicked Callback para navegar al inicio
 * @param onSearchClicked Callback para realizar búsquedas
 * @param onProfileClicked Callback para acceder al perfil
 * @param onFavoritesClicked Callback para acceder a favoritos
 */
@Composable

fun EditProfileScreen(

    onAcceptChangesClicked: () -> Unit,

    onHomeButtonClicked: () -> Unit,

    onSearchClicked: () -> Unit,

    onProfileClicked: () -> Unit,

    onFavoritesClicked: () -> Unit,

    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {

    val usuarioLogueado = Sesion.usuario

    val context = LocalContext.current



    val fullName = remember { mutableStateOf("${usuarioLogueado?.nombre}") }

    val correo = remember { mutableStateOf("${usuarioLogueado?.correo}") }

    val errorMessage = remember { mutableStateOf("") }

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val currentImageUri = remember { mutableStateOf<Uri?>(null) }
    /**
    * Variables para asignar colores al borde de la foto de perfil y el icono de edit
    */
    var color: Color
    var colorIconoEdit: Color
    var colorBordeIconoEdit: Color


    if(isSystemInDarkTheme()){
        color = Color(0xFFAC2CC4)
        colorIconoEdit = Color(0xFF2D3794)
        colorBordeIconoEdit = Color(0xFFAC2CC4)
    }else{
        color = Color(0xFFF55C7A)
        colorIconoEdit = Color(0xFFFF9800)
        colorBordeIconoEdit= Color(0xFFF55C7A)
    }

    LaunchedEffect(Unit) {

        usuarioLogueado?.id?.let { userId ->

            viewModel.loadCurrentUser(userId)

        }


        usuarioLogueado?.imagenPerfilUri?.let { uriString ->

            val file = File(uriString)

            if (file.exists()) {

                currentImageUri.value = Uri.fromFile(file)

            }

        }

    }



    val requiredPermission = remember {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Manifest.permission.READ_MEDIA_IMAGES

        } else {

            Manifest.permission.READ_EXTERNAL_STORAGE

        }

    }



    val hasStoragePermission = remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(context, requiredPermission) == PackageManager.PERMISSION_GRANTED

        )

    }

    val galleryLauncher = rememberLauncherForActivityResult(

        ActivityResultContracts.GetContent()

    ) { uri ->

        uri?.let {

            selectedImageUri.value = it

        }

    }

    val permissionLauncher = rememberLauncherForActivityResult(

        ActivityResultContracts.RequestPermission()

    ) { isGranted ->

        hasStoragePermission.value = isGranted

        if (isGranted) {

            galleryLauncher.launch("image/*")

        } else {

            errorMessage.value = "Se necesita permiso para seleccionar imágenes. Por favor, activa el permiso en Configuración."

        }

    }
	/**
	 * Funcion para la gestion de imagenes
	 * Si la aplicación ya tiene permisos de almacenamiento, lanza un selector de imágenes desde la galería.
	 * En caso contrario, solicita al usuario el permiso necesario para acceder al almacenamiento.
	 */
    fun handleImageClick() {

        if (hasStoragePermission.value) {

            galleryLauncher.launch("image/*")

        } else {

            permissionLauncher.launch(requiredPermission)

        }

    }
	
	/**
	 * Función para modificar los datos del usuario en la BD y en sesion.
	 */
    fun updateFields() {
        when {
            fullName.value.isEmpty() -> errorMessage.value = "Por favor, ingrese un nombre."
            correo.value.isEmpty() -> errorMessage.value = "Por favor, ingrese un correo electrónico."
            else -> {
                errorMessage.value = ""

                usuarioLogueado?.nombre = fullName.value
                usuarioLogueado?.correo = correo.value
                val newLog: Loggeado = usuarioLogueado ?: Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf())
                viewModel.updateLogFromUi(newLog)
                selectedImageUri.value?.let { uri ->
                    usuarioLogueado?.let { user ->
                        viewModel.updateUserProfileImage(context, user, uri) {
                            onAcceptChangesClicked()
                        }
                    } ?: run {
                        onAcceptChangesClicked()
                    }
                } ?: run {
                    // No hay imagen para actualizar
                    onAcceptChangesClicked()
                }
            }
        }
    }



    Scaffold(

        modifier = Modifier.fillMaxSize(),

        //containerColor = Color(0xfff6bc66), // Fondo de la pantalla

        bottomBar = {

            BottomNavigationBar(onHomeButtonClicked, onSearchClicked, onProfileClicked, onFavoritesClicked)

        }

    ) { innerPadding ->

        Column(

            modifier = Modifier

                .fillMaxWidth()

                .fillMaxSize()

                //.background(Color(0xfff6bc66))

                .padding(innerPadding),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Top

        ) {

            Box(

                modifier = Modifier

                    .padding(16.dp),

                contentAlignment = Alignment.Center

            ) {

                // Contenedor principal para la foto

                Box(

                    modifier = Modifier

                        .size(150.dp)

                        .clip(CircleShape)

                        .border(2.dp, color, CircleShape)

                        .background(Color.White)

                        .clickable {

                            if (hasStoragePermission.value) {

                                galleryLauncher.launch("image/*")

                            } else {

                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                            }

                        },

                    contentAlignment = Alignment.Center

                ) {

                    when {

                        selectedImageUri.value != null -> {

                            val bitmap = remember(selectedImageUri.value) {

                                val inputStream = context.contentResolver.openInputStream(selectedImageUri.value!!)

                                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()

                            }

                            bitmap?.let {

                                Image(

                                    bitmap = it,

                                    contentDescription = "Foto de perfil",

                                    modifier = Modifier.fillMaxSize(),

                                    contentScale = ContentScale.Crop

                                )

                            }

                        }

                        currentImageUri.value != null -> {

                            val bitmap = remember(currentImageUri.value) {

                                BitmapFactory.decodeFile(File(currentImageUri.value.toString()).absolutePath)?.asImageBitmap()

                            }

                            bitmap?.let {

                                Image(

                                    bitmap = it,

                                    contentDescription = "Foto de perfil",

                                    modifier = Modifier.fillMaxSize(),

                                    contentScale = ContentScale.Crop

                                )

                            } ?: Image(

                                painter = painterResource(R.drawable.logoapp),

                                contentDescription = "Imagen predeterminada",

                                modifier = Modifier.fillMaxSize(),

                                contentScale = ContentScale.Crop

                            )

                        }

                        else -> {

                            Image(

                                painter = painterResource(R.drawable.logoapp),

                                contentDescription = "Imagen predeterminada",

                                modifier = Modifier.fillMaxSize(),

                                contentScale = ContentScale.Crop

                            )

                        }

                    }

                }



                // Icono de edición en la esquina inferior derecha

                Box(

                    modifier = Modifier

                        .align(Alignment.BottomEnd)

                        .offset(x = (-10).dp, y = (-10).dp)

                        .size(40.dp)

                        .clip(CircleShape)

                        .background(colorIconoEdit)

                        .border(1.dp, colorBordeIconoEdit, CircleShape),

                    contentAlignment = Alignment.Center

                ) {

                    Icon(

                        imageVector = Icons.Default.Edit,

                        contentDescription = "Editar foto",

                        tint = Color.White,

                        modifier = Modifier.size(24.dp)

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

                            text = "Nombre de usuario: ${usuarioLogueado?.id}",

                            fontSize = 18.sp,

                            fontWeight = FontWeight.Bold,

                            //color = Color.Black,

                            textAlign = TextAlign.Center

                        )

                    }



                    Spacer(modifier = Modifier.height(30.dp))



                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Text(

                            text = "Nombre Completo: ",

                            fontSize = 18.sp,

                            fontWeight = FontWeight.Bold,

                            //color = Color.Black,

                            textAlign = TextAlign.Center

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(

                            value = fullName.value,

                            onValueChange = { fullName.value = it },

                            singleLine = true,

                            modifier = Modifier

                                .fillMaxWidth(0.8f)



                                .padding(vertical = 4.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),

                        )

                    }



                    Spacer(modifier = Modifier.height(20.dp))



                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Image(

                            painter = painterResource(R.drawable.gmail),

                            contentDescription = null,

                            contentScale = ContentScale.Fit,

                            modifier = Modifier.size(24.dp)

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(

                            text = "Correo: ",

                            fontSize = 18.sp,

                            fontWeight = FontWeight.Bold,

                            //color = Color.Black,

                            textAlign = TextAlign.Center

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(

                            value = correo.value,

                            onValueChange = { correo.value = it },

                            singleLine = true,

                            modifier = Modifier

                                .fillMaxWidth(0.8f)

                                .padding(vertical = 4.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        )

                    }



                    if (errorMessage.value.isNotEmpty()) {

                        Text(

                            text = errorMessage.value,

                            //color = Color.Red,

                            fontSize = 14.sp,

                            modifier = Modifier.padding(8.dp)

                        )

                    }

                }

            }



            Spacer(modifier = Modifier.height(30.dp))



            Box(

                modifier = Modifier

                    .fillMaxWidth()

                    .padding(8.dp)

            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                    Button(

                        onClick = { updateFields() },

                        modifier = Modifier

                            .fillMaxWidth()

                            .padding(16.dp),

                        colors = ButtonDefaults.buttonColors(

                            //containerColor = Color(0xfff55c7a),

                            //contentColor = Color.Black

                        ),

                        ) {

                        Text(

                            text = "Aceptar Cambios",

                            fontSize = 18.sp,

                            fontWeight = FontWeight.Bold,

                            //color = Color.Black,

                            textAlign = TextAlign.Center

                        )

                    }

                }

            }

        }

    }

}


/**
 * Previsualización de la pantalla de editar perfil para Android Studio.
 *
 * Muestra una representación estática del diseño para facilitar el desarrollo.
 */
@Preview(showBackground = true)

@Composable

fun PantallaEditarPerfilPreview() {

    EditProfileScreen(

        onAcceptChangesClicked = {},

        onHomeButtonClicked = {},

        onSearchClicked = {},

        onProfileClicked = {},

        onFavoritesClicked = {}

    )

}