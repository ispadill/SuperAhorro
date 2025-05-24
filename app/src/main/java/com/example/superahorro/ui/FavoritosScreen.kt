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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.superahorro.ModeloDominio.Sesion
import com.example.superahorro.ui.theme.AppBarTitleStyle

/**
 * Pantalla que muestra las tablas favoritas del usuario logueado.
 *
 * Esta pantalla accede a la base de datos local para obtener las tablas marcadas como favoritas por el usuario.
 * Se muestra una lista con tarjetas para cada tabla favorita, donde se permite quitarla de favoritos.
 * 
 * - Usa un `Scaffold` con barra superior (TopAppBar) y barra inferior de navegación.
 * - Cambia el color de los elementos si el sistema está en modo oscuro.
 *
 * @param navHostController Controlador de navegación (puede ser nulo si no se necesita en esta pantalla)
 * @param onHomeButtonClicked Callback para ir a la pantalla principal
 * @param onSearchClicked Callback para navegar a búsqueda
 * @param onProfileClicked Callback para ir al perfil
 * @param onFavoritesClicked Callback para volver a favoritos
 * @param onViewTableClicked Callback para ver el detalle de una tabla (actualmente no se usa en el código)
 * @param viewModel ViewModel compartido (PantallaInicioViewModel) que contiene funciones globales como cerrar sesión
 */

@Composable
fun FavoritosScreen(
    navHostController: NavHostController?,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    onViewTableClicked: () -> Unit,
    viewModel: PantallaInicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var tablasFavoritas by remember { mutableStateOf<List<Tabla>>(emptyList()) }

    var color: Color
    var colorCardBorder: Color
    var colorBotonQuitar: Color


    if(isSystemInDarkTheme()){
        color = Color(0xFF3F51B5)
        colorCardBorder = Color.White
        colorBotonQuitar = Color(0xFF7344CB)
    }else{
        color = Color(0xfff68c70)
        colorCardBorder = Color.Black
        colorBotonQuitar = Color(0xFFF55C7A)

    }

    LaunchedEffect(true) {
        scope.launch {
            val db = BaseDeDatos.getDatabase(context)
            val usuario =
                Sesion.usuario?.let { db.loggeadoDao().getUsuarioPorId(it.id) }
            if (usuario != null) {
                tablasFavoritas = db.tablaDao().getTablasByIds(usuario.tablasFavoritas)
            }
        }
    }
    
    // UI con barra superior, inferior y lista de favoritas
    Scaffold(
        modifier = Modifier.fillMaxSize()//.background(color = Color(0xfff6bc66)),
        //containerColor = Color(0xfff6bc66),
        ,
        topBar = {
            if (navHostController != null) {
                FavoritosTopAppBar(
                    title = "FAVORITOS",
                    onSearchClick = onSearchClicked,
                    viewModel = viewModel,
                    navController = navHostController
                )
            }
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
                            //color = Color.DarkGray
                        )
                    }
                }
            } else {
                items(tablasFavoritas, key = { it.id }) { tabla ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = color),
                        border = BorderStroke(1.dp, colorCardBorder)
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
                                    Image(
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

                                        val usuario =  Sesion.usuario?.let { db.loggeadoDao().getUsuarioPorId(it.id) }
                                        val nuevasFavoritas = usuario?.tablasFavoritas?.toMutableList()
                                        if (tabla != null) {
                                            if (nuevasFavoritas != null) {
                                                nuevasFavoritas.remove(tabla.id)
                                                val usuarioActualizado =
                                                    usuario.copy(tablasFavoritas = nuevasFavoritas)
                                                loggeadoDAO.update(usuarioActualizado)
                                                // Refrescar lista
                                                tablasFavoritas = db.tablaDao().getTablasByIds(nuevasFavoritas)
                                            }
                                        }

                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorBotonQuitar,
                                    //contentColor = Color.Black
                                )
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

/**
 * Barra superior personalizada para la pantalla de favoritos.
 *
 * Contiene:
 * - Título centrado con el nombre de la pantalla
 * - Botón de menú que despliega un `DropdownMenu` con opción de cerrar sesión
 * - Botón de búsqueda a la derecha
 *
 * @param title Título mostrado en la barra
 * @param onSearchClick Acción al pulsar el botón de búsqueda
 * @param viewModel ViewModel que contiene la función de cierre de sesión
 * @param navController Controlador de navegación necesario para redirigir al cerrar sesión
 * @param modifier Modificador opcional para personalizar la barra
 */
 
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosTopAppBar(
    title: String,
    onSearchClick: () -> Unit,
    viewModel: PantallaInicioViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }
    Column {
        androidx.compose.material3.CenterAlignedTopAppBar(
            title = { Text(
                text = title,
                style = AppBarTitleStyle
            )  },
            modifier = modifier,
            colors = topAppBarColors(
                //containerColor = Color(0xfff55c7a)
            ),
            navigationIcon = {
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.logoapp),
                            contentDescription = "Menú de usuario",
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    UserDropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        onLogout = { viewModel.viewModelScope.launch {
                            if (navController != null) {
                                viewModel.cerrarSesion(navController)
                            }
                        }
                            showMenu = false
                        }
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
            //color = Color.Black,
            thickness = 3.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
