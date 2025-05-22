package com.example.superahorro.ui

import com.example.superahorro.ui.theme.AppBarTitleStyle
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla

/**
 * Pantalla que muestra el perfil de un usuario específico y sus tablas.
 *
 * @param usuarioId ID del usuario cuyo perfil se está visualizando
 * @param onBackButtonClicked Callback para volver a la pantalla anterior
 * @param onViewTableClicked Callback para ver una tabla específica
 * @param onHomeButtonClicked Callback para navegar a la pantalla de inicio
 * @param onSearchClicked Callback para navegar a la pantalla de búsqueda
 * @param onProfileClicked Callback para navegar al perfil propio
 * @param onFavoritesClicked Callback para navegar a favoritos
 */
@Composable
fun PantallaPerfilUsuario(
    usuarioId: String,
    onBackButtonClicked: () -> Unit,
    onViewTableClicked: (Int) -> Unit,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    viewModel: PantallaPerfilUsuarioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imagenPerfil = uiState.imagenPerfilBitmap
    val usuario = uiState.usuario

    LaunchedEffect(usuarioId) {
        viewModel.cargarDatosUsuario(context, usuarioId)
        viewModel.cargarTablasUsuario(usuarioId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF6BC66),
        topBar = {
            PerfilUsuarioTopAppBar(
                usuario = usuario,
                onBackClick = onBackButtonClicked,
                uiState = uiState
            )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6BC66))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF68C70)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Row{
                    Column(modifier=Modifier
                        .width(100.dp)
                        .padding(all = 6.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                        if (uiState.usuario != null && imagenPerfil != null) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                                ) {
                                if (usuario != null) {
                                    Image(
                                        bitmap = imagenPerfil.asImageBitmap(),
                                        contentDescription = "Foto de perfil de ${usuario.nombre}",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        alignment = Alignment.Center
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Cargando perfil...",
                                style = AppBarTitleStyle
                            )
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column (modifier = Modifier
                            .fillMaxWidth()
                        ){
                            Row {
                                if (usuario != null) {
                                    Text(
                                        text = usuario.nombre,
                                        style = AppBarTitleStyle,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(25.dp))
                            Row {
                                Estadisticas(
                                    value = "${uiState.tablasUsuario.size}",
                                    label = "Tablas",
                                    icono = false,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Divider(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .width(1.dp),
                                    color = Color.Black

                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Estadisticas(
                                    value = "%.2f".format(uiState.ratingMedio),
                                    label = "Valoración media",
                                    icono=true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            Card (
                modifier = Modifier
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xfff55c7a)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, Color.Black)
            ){
                Text(
                    text = "TABLAS",
                    style = AppBarTitleStyle,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            PerfilUsuarioTablasList(
                tablasList = uiState.tablasUsuario,
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                ),
                modifier = Modifier.padding(horizontal = 8.dp),
                onViewTableClicked = onViewTableClicked
            )
        }
    }
}

/**
 * Componente para mostrar una estadística con valor y etiqueta
 */
@Composable
private fun Estadisticas(
    value: String,
    label: String,
    icono: Boolean,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier=Modifier.padding(top = 3.dp)
    ) {
        Row (modifier=Modifier.padding(top = 3.dp)){
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Row(modifier=Modifier.padding(top = 3.dp)) {
            if(icono==true){
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Valoración media",
                    tint = Color(0xFFF6BC66),
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontSize = 18.sp
            )
        }
    }
}

/**
 * TopAppBar personalizada para mostrar la información del perfil del usuario
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PerfilUsuarioTopAppBar(
    usuario: Loggeado?,

    onBackClick: () -> Unit,
    uiState: PantallaPerfilUsuarioUiState,
    modifier: Modifier = Modifier
) {
    Column {
        androidx.compose.material3.TopAppBar(
            title = {
                if (usuario != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "@${usuario.id}",
                                style = AppBarTitleStyle,
                                fontSize = 20.sp
                            )
                        }
                } else {
                    Text(
                        text = "Cargando perfil...",
                        style = AppBarTitleStyle
                    )
                }
            },
            modifier = modifier,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF55C7A)
            ),
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
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

/**
 * Componente que muestra la lista de tablas del usuario
 */
@Composable
private fun PerfilUsuarioTablasList(
    tablasList: List<Tabla>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onViewTableClicked: (Int) -> Unit
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
                    .clickable { onViewTableClicked(tabla.id) },
            )
        }
    }
}
