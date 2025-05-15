
package com.example.superahorro.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.superahorro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onAcceptChangesClicked: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val username = remember { mutableStateOf("Juanito666") }
    val fullName = remember { mutableStateOf("Juan Palomo") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xfff6bc66), // Fondo de la pantalla
        bottomBar = {
            BottomNavigationBar(onHomeButtonClicked, onSearchClicked, onProfileClicked, onFavoritesClicked)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .background(Color(0xfff6bc66))
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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
                            text = "Nombre de Usuario: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = username.value,
                            onValueChange = { username.value = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 4.dp),
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                containerColor = Color(0xfff68c70), //Color del contenedor
//                                focusedBorderColor = Color.Black, // Color del borde cuando el campo está enfocado
//                                unfocusedBorderColor = Color(0xfff55c7a) // Color del borde
//                            )

                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Nombre Completo: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
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
//                            colors = TextFieldDefaults.outlinedTextFieldColors(
//                                containerColor = Color(0xfff68c70), //Color del contenedor
//                                focusedBorderColor = Color.Black, // Color del borde cuando el campo está enfocado
//                                unfocusedBorderColor = Color(0xfff55c7a) // Color del borde
//                            )
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
                            text = "Correo: JuanPalomo@gmail.com",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = onAcceptChangesClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xfff55c7a),
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "Aceptar Cambios",
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

@Preview(showBackground = true)
@Composable
fun PantallaEditarPerfilPreview() {

    EditProfileScreen(
        onAcceptChangesClicked = {},
        onHomeButtonClicked = {},
        onSearchClicked = {},
        onProfileClicked = {},
        onFavoritesClicked = {},
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center)
    )
}
