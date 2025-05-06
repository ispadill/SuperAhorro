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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import com.example.superahorro.R

@Composable
fun ProfileScreen(
    onEditProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier.fillMaxWidth().fillMaxSize().background(Color(0xfff55c7a)), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

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


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(text = "Nombre de Usuario: Juanito666 ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center)
                }

                Spacer(modifier = Modifier.height(40.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(text = "Nombre Completo:  Juan Palomo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center)
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
                    Text(text = "Correo: JuanPalomo@gmail.com", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center)
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(bottom = 150.dp)) {
                Button(
                    onClick = onEditProfileClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xfff6bc66),  // Color de fondo del botón
                        contentColor = Color.Black   // Color del texto
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

@Preview(showBackground = true)
@Composable
fun PantallaPerfilPreview() {

        ProfileScreen(
            onEditProfileClicked = {},
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))

}