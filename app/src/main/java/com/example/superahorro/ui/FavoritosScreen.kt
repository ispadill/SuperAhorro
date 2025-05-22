package com.example.superahorro.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.superahorro.Datos.BaseDeDatos
import com.example.superahorro.Datos.Tabla
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.superahorro.R
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import com.example.superahorro.ui.theme.AppBarTitleStyle


@Composable
fun FavoritosScreen(
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onViewTableClicked: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tablasFavoritas by remember { mutableStateOf<List<Tabla>>(emptyList()) }

    LaunchedEffect(true) {
        scope.launch {
            val db = BaseDeDatos.getDatabase(context)
            val usuario = db.loggeadoDao().getUsuarioPorId("Juan") // Cambia luego por ID real
            tablasFavoritas = db.tablaDao().getTablasByIds(usuario.tablasFavoritas)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(color = Color(0xfff6bc66)),
        containerColor = Color(0xfff6bc66),
        topBar = {
            FavoritosTopAppBar(title = "FAVORITOS", onSearchClick = onSearchClicked)
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeButtonClicked = onHomeButtonClicked,
                onSearchClicked = onSearchClicked,
                onProfileClicked = onProfileClicked,
                onFavoritesClicked = onFavoritesClicked
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (tablasFavoritas.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay tablas favoritas.",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.DarkGray
                        )
                    }
                }
            } else {
                items(tablasFavoritas, key = { it.id }) { tabla ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xfff68c70)),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = tabla.titulo, modifier = Modifier.weight(1f))
                                Text(text = tabla.autor, modifier = Modifier.weight(1f))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.estrella),
                                        contentDescription = null,
                                        modifier = Modifier.size(30.dp)
                                    )
                                    Text(
                                        text = "%.2f".format(tabla.valoracion),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        val db = BaseDeDatos.getDatabase(context)
                                        val loggeadoDAO = db.loggeadoDao()

                                        val usuario = loggeadoDAO.getUsuarioPorId("Juan") // usa el ID real luego
                                        val nuevasFavoritas = usuario.tablasFavoritas.toMutableList()
                                        nuevasFavoritas.remove(tabla.id)
                                        val usuarioActualizado = usuario.copy(tablasFavoritas = nuevasFavoritas)
                                        loggeadoDAO.update(usuarioActualizado)

                                        // Refrescar lista
                                        tablasFavoritas = db.tablaDao().getTablasByIds(nuevasFavoritas)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Quitar de favoritos")
                            }
                        }
                    }
                }

            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosTopAppBar(
    title: String,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        androidx.compose.material3.CenterAlignedTopAppBar(
            title = { Text(
                text = title,
                style = AppBarTitleStyle
            )  },
            modifier = modifier,
            colors = topAppBarColors(
                containerColor = Color(0xfff55c7a)
            ),
            navigationIcon = {
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(id = R.drawable.logoapp),
                        contentDescription = "Logo de la aplicación",
                        modifier = Modifier.size(100.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color.Black
                    )
                }
            }
        )
        Divider(
            color = Color.Black,
            thickness = 3.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
