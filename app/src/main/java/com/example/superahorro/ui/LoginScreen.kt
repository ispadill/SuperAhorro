package com.example.superahorro.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.superahorro.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.R
import com.example.superahorro.ModeloDominio.Sesion

@Composable
fun LoginScreen(
    onAceptarClicked: () -> Unit,
    onRegistrarseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val errorMessage = remember { mutableStateOf("") }

    fun validateAndLogin() {
        when {
            username.value.isEmpty() -> errorMessage.value = "Por favor, ingrese su nombre de usuario."
            password.value.isEmpty() -> errorMessage.value = "Por favor, ingrese una contraseña."
            else -> {
                viewModel.isUsernameValidFromUi(username.value) { isTaken ->
                    if (isTaken) {
                        errorMessage.value = "No existe un usuario con ese nombre y esa contraseña"
                    } else {
                        errorMessage.value = ""
                        viewModel.devLogeadoFromUi(username.value) { usuario ->
                            if (usuario != null) {
                                onAceptarClicked()
                            } else {
                                errorMessage.value = "Error al recuperar el usuario."
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xfff6bc66), // Fondo de la pantalla

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


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, top = 50.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp)) {
                    Text(
                        text = "SuperAhorro",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
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
                            text = "Contraseña: ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = password.value,
                            onValueChange = { password.value = it },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .padding(vertical = 4.dp),

                            visualTransformation = PasswordVisualTransformation()
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
                            text = "¿No tienes cuenta? ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onRegistrarseClicked,
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xfff55c7a),
                                contentColor = Color.Black
                            ),
                        ) {
                            Text(
                                text = "Regístrate",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
                    if (errorMessage.value.isNotEmpty()) {
                        Text(
                            text = errorMessage.value,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp)
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
                        onClick = onAceptarClicked,

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xfff55c7a),
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "Aceptar",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
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
        }
    }
}


/**
 * Previsualización de la pantalla de login para Android Studio.
 *
 * Muestra una representación estática del diseño para facilitar el desarrollo.
 */
@Preview(showBackground = true)
@Composable
fun PantallaLoginPreview() {

    LoginScreen(
        onAceptarClicked = {},
        onRegistrarseClicked = {},
        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))

}