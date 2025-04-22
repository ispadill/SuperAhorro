package com.example.superahorro.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.superahorro.ModeloDominio.Plantilla
import com.example.superahorro.ModeloDominio.Tabla

val plantillaDefault = Plantilla(nombre="Prueba", numColumnas =3,
    nombresColumnas = listOf("Tienda","Producto","Precio")
)

val tablasDePrueba = listOf(
    Tabla(id = "1",titulo ="Mercadona", autor = "Pedro", valoracion = 3F, numeroValoraciones = 10, plantilla = plantillaDefault),
    Tabla(id = "1",titulo ="BM", autor = "Paco", valoracion = 5F, numeroValoraciones = 4, plantilla = plantillaDefault),
    Tabla(id = "1",titulo ="Carrefour", autor = "Maria", valoracion = 1F, numeroValoraciones = 100, plantilla = plantillaDefault)
)

@Composable
fun PantallaInicio() {

    val modifier=Modifier.fillMaxSize()

    Scaffold(
        topBar = {
            TablasTopAppBar(
                title = "SuperAhorro",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick ={},
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        },
    ) { innerPadding ->
        InicioCuerpo(
            tablasList = tablasDePrueba,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun InicioCuerpo(
    tablasList: List<Tabla>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
            TablasList(
                tablasList = tablasList,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
    }
}

@Composable
private fun TablasList(
    tablasList: List<Tabla>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = tablasList, key = { it.id }) { tabla ->
            TablaItem(
                tabla = tabla,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { }
            )
        }
    }
}

@Composable
private fun TablaItem(
    tabla: Tabla,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = tabla.titulo,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = tabla.autor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TablasTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    androidx.compose.material3.TopAppBar(
        title = { Text(text = title) },
        modifier = modifier
    )
}