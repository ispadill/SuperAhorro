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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.Star
import com.example.superahorro.R
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.Datos.Loggeado
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

/**
 * Función para filtrar usuarios según criterio de búsqueda.
 *
 * @param query Texto de búsqueda
 * @param usuarios Lista completa de usuarios
 * @return Lista de resultados formateados como "ID (Tipo)"
 */
fun performUserSearch(query: String, usuarios: List<Loggeado>): List<String> {
    return if (query.isNotEmpty()) {
        usuarios
            .filter {
                it.id.contains(query, ignoreCase = true) ||
                        it.nombre.contains(query, ignoreCase = true)
            }
            .map { "${it.id} (${it.nombre})" }
    } else {
        emptyList()
    }
}

/**
 * Pantalla de búsqueda de la aplicación que muestra una lista de usuarios y funcionalidades de búsqueda.
 *
 * Utiliza un [Scaffold] para estructurar los componentes principales:
 * - TopAppBar con logo y búsqueda
 * - LazyColumn para lista de elementos
 * - BottomAppBar con navegación
 * - FloatingActionButton para creación de nuevos usuarios
 *
 * @param onViewUserClicked Callback para visualización de detalles de usuario
 * @param onHomeButtonClicked Callback para navegación a inicio
 * @param onSearchClicked Callback para activar búsqueda
 * @param onProfileClicked Callback para navegación a perfil
 * @param onFavoritesClicked Callback para navegación a favoritos
 */
@Composable
fun PantallaBusqueda(
    navHostController: NavHostController?,
    onViewUserClicked: (String) -> Unit,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    viewModel: PantallaBusquedaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val modifier = Modifier.fillMaxSize()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var showSuggestions by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.cargarUsuariosConRatings(context)
    }

    LaunchedEffect(searchQuery) {
        viewModel.actualizarBusqueda(searchQuery)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(color = Color(0xfff6bc66)),
        containerColor = Color(0xfff6bc66),
        topBar = {
            UsuariosTopAppBar(
                title = "BUSCAR USUARIOS",
                onSearchClick = {
                    isSearchVisible = !isSearchVisible
                    if (!isSearchVisible) {
                        searchQuery = ""
                        showSuggestions = false
                    }
                },
                viewModel = viewModel,
                navController=navHostController
            )
        },
        bottomBar = {
            BottomNavigationBar(onHomeButtonClicked,
                onSearchClicked,
                onProfileClicked,
                onFavoritesClicked)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff6bc66))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (isSearchVisible) {
                UserSearchBarBelowAppBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        searchResults = performUserSearch(it, uiState.usuarios)
                        showSuggestions = it.isNotEmpty() && searchResults.isNotEmpty()
                    },
                    onClearClick = {
                        searchQuery = ""
                        showSuggestions = false
                    },
                    showSuggestions = showSuggestions,
                    searchResults = searchResults,
                    onResultClick = { resultText ->
                        val idOnly = resultText.split(" (").firstOrNull() ?: resultText
                        searchQuery = idOnly
                        showSuggestions = false
                    }
                )
            }

            BusquedaCuerpo(
                usuariosConValoraciones = if (searchQuery.isEmpty()) uiState.usuariosConRating else {
                    uiState.usuariosConRating.filter {
                        it.usuario.id.contains(searchQuery, ignoreCase = true) ||
                                it.usuario.nombre.contains(searchQuery, ignoreCase = true) ||
                                it.usuario.tipo.contains(searchQuery, ignoreCase = true)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                ),
                onViewUserClicked = onViewUserClicked,
                viewModel = viewModel
            )
        }
    }
}

/**
 * Contenedor principal del cuerpo de la pantalla de búsqueda.
 *
 * Encapsula la lista de usuarios dentro de un diseño Column centrado horizontalmente.
 *
 * @param usuariosConValoraciones Lista de usuarios con valoraciones a mostrar
 * @param modifier Modificador para personalización del layout
 * @param contentPadding Padding interno para la lista de usuarios
 * @param onViewUserClicked Callback al hacer clic en un usuario
 * @param viewModel ViewModel para operaciones adicionales
 */
@Composable
private fun BusquedaCuerpo(
    usuariosConValoraciones: List<UsuarioConRating>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onViewUserClicked: (String) -> Unit,
    viewModel: PantallaBusquedaViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        UsuariosList(
            usuariosConValoraciones = usuariosConValoraciones,
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = 8.dp),
            onViewUserClicked = onViewUserClicked,
            viewModel = viewModel
        )
    }
}

/**
 * Componente que muestra una lista desplazable de usuarios usando LazyColumn.
 *
 * @param usuariosConValoraciones Lista de usuarios con valoraciones a mostrar
 * @param contentPadding Padding interno para la lista
 * @param modifier Modificador para personalización del layout
 * @param onViewUserClicked Callback al hacer clic en un usuario
 * @param viewModel ViewModel para operaciones adicionales
 */
@Composable
private fun UsuariosList(
    usuariosConValoraciones: List<UsuarioConRating>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onViewUserClicked: (String) -> Unit,
    viewModel: PantallaBusquedaViewModel
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = usuariosConValoraciones, key = { it.usuario.id }) { usuarioRating ->
            UsuarioItem(
                usuarioConRating = usuarioRating,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onViewUserClicked(usuarioRating.usuario.id) },
                viewModel = viewModel
            )
        }
    }
}

/**
 * Componente que representa un ítem individual de la lista de usuarios.
 *
 * @param usuarioConRating Datos del usuario con valoración a mostrar
 * @param modifier Modificador para personalización del layout
 * @param viewModel ViewModel para operaciones adicionales
 */
@Composable
fun UsuarioItem(
    usuarioConRating: UsuarioConRating,
    modifier: Modifier = Modifier,
    viewModel: PantallaBusquedaViewModel
) {
    val context = LocalContext.current
    val usuario = usuarioConRating.usuario

    val imagenPerfil = usuarioConRating.imagenPerfilBitmap ?:
    viewModel.cargarImagenPerfil(context, usuario.id)

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xfff68c70),
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = imagenPerfil.asImageBitmap(),
                        contentDescription = "Foto de perfil de ${usuario.nombre}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().background(Color.White)
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    modifier = Modifier.weight(1f).width(50.dp)
                ) {
                    Text(
                        text = usuario.nombre,
                        style = MaterialTheme.typography.titleLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "@${usuario.id}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                    )
                }

                Spacer(Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Valoración media",
                            tint = Color(0xFFF6BC66),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text ="%.2f".format(usuarioConRating.ratingMedio),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${usuario.tablasPropias.size} TABLAS",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

/**
 * Barra superior personalizada para la pantalla de usuarios.
 *
 * Incluye:
 * - Logo de la aplicación
 * - Título centrado
 * - Botón de búsqueda
 * - Divisor inferior decorativo
 *
 * @param title Texto a mostrar como título
 * @param onSearchClick Callback para el botón de búsqueda
 * @param modifier Modificador para personalización del layout
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosTopAppBar(
    title: String,
    onSearchClick: () -> Unit,
    viewModel: PantallaBusquedaViewModel,
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
                containerColor = Color(0xfff55c7a)
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
            color = Color.Black,
            thickness = 3.dp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Componente que muestra la barra de búsqueda con sugerencias dinámicas.
 *
 * @param query Texto actual de búsqueda
 * @param onQueryChange Callback para cambios en el texto
 * @param onClearClick Callback para limpiar búsqueda
 * @param showSuggestions Bandera para mostrar sugerencias
 * @param searchResults Lista de resultados de búsqueda
 * @param onResultClick Callback al seleccionar un resultado
 * @param modifier Modificador para personalización del layout
 */
@Composable
fun UserSearchBarBelowAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    showSuggestions: Boolean,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PantallaBusquedaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFF6BC66))
            .padding(bottom = 4.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text("Buscar usuarios") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF555555)
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar",
                            tint = Color(0xFF555555)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    performUserSearch(query, uiState.usuarios)
                }
            )
        )
        if (showSuggestions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        searchResults.forEach { resultText ->
                            ListItem(
                                headlineContent = { Text(resultText) },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                                modifier = Modifier
                                    .clickable {
                                        onResultClick(resultText)
                                    }
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}