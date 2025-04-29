package com.example.superahorro

import android.content.Context
import android.graphics.drawable.shapes.Shape
import android.widget.Space
import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

//Posiblemente habrá que borrarlo pero por ahora dejalo que igual uso algo de aqui luego
/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablaAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        //Aquí hay que pasar el nombre correcto que obtendremos de la página anterior
        //o igual de persistencia
        title = { "Test" },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
*/
@Composable
fun TablaView(
    //Aquí le tenemos que pasar el viewModel que hemos creado
    //viewModel: ViewModel = viewModel(),//OrderViewModel = viewModel(),
    //navController: NavHostController = rememberNavController()
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //poner un card o box dentro de la columna con otra columna dentro y dos filas dentro de eso y al fondo una sola row y lo de en medio que ocupe el espacio máximo
        Column(
            modifier = Modifier.fillMaxSize().background(Color.LightGray),

            //Cambiarlo a SpaceBetween
            verticalArrangement = Arrangement.Top,
            //horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.height(100.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Spacer(modifier = Modifier.width(10.dp))
                OutlinedCard(
                    modifier = Modifier.width(220.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 2.dp, color = Color.Black)
                ) { Text(" " + "Titulo de la tabla", //El título se saca de la BD
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 5.dp)) }
                Spacer(modifier = Modifier.width(15.dp))
                OutlinedCard(
                    modifier = Modifier.width(150.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 2.dp, color = Color.Black)
                ) { Text(" " + "Valoración", //Sacada de la función de las tablas
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 5.dp)) }

            }
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ){
                Spacer(modifier = Modifier.width(30.dp))
                OutlinedCard(
                    modifier = Modifier.width(200.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 2.dp, color = Color.Black)
                ) { Text(" " + "Creador de la tabla", //El creador se saca de la BD
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 5.dp)) }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End
            ){
                OutlinedCard(
                    modifier = Modifier.width(150.dp),
                    shape = RectangleShape,
                    border = BorderStroke(width = 2.dp, color = Color.Black)
                ) { Text(" " + "Cambiar la tabla", //Sería para añadir, modificar o borrar tuplas de la tabla
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 5.dp)) }
            }
        }


    }
}