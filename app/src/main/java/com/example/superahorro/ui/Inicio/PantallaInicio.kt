package com.example.superahorro.ui

import android.content.Intent
import android.widget.Toast
import com.example.superahorro.ui.theme.AppBarTitleStyle
import androidx.compose.ui.text.font.FontWeight
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import com.example.superahorro.R
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.Datos.Tabla
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

/**
 * Función para filtrar tablas según criterio de búsqueda.
 *
 * @param query Texto de búsqueda
 * @param tablas Lista completa de tablas
 * @return Lista de resultados formateados como "Título (Autor)"
 */
fun performSearch(query: String, tablas: List<Tabla>): List<String> {
    return if (query.isNotEmpty()) {
        tablas
            .filter { it.titulo.contains(query, ignoreCase = true) ||
                    it.autor.contains(query, ignoreCase = true) }
            .map { "${it.titulo} (${it.autor})" }
    } else {
        emptyList()
    }
}
/**
 * Pantalla principal de la aplicación que muestra una lista de tablas y funcionalidades de búsqueda.
 *
 * Utiliza un [Scaffold] para estructurar los componentes principales:
 * - TopAppBar con logo y búsqueda
 * - LazyColumn para lista de elementos
 * - BottomAppBar con navegación
 * - FloatingActionButton para creación de nuevas tablas
 *
 * @param onCreateTableClicked Callback para creación de nueva tabla
 * @param onOtherProfileClicked Callback para navegación a perfil de otro usuario
 * @param onViewTableClicked Callback para visualización de tabla
 * @param onHomeButtonClicked Callback para navegación a inicio
 * @param onSearchClicked Callback para activar búsqueda
 * @param onProfileClicked Callback para navegación a perfil
 * @param onFavoritesClicked Callback para navegación a favoritos
 */
@Composable
fun PantallaInicio(
    navHostController: NavHostController?,
    onCreateTableClicked: () -> Unit,
    onViewTableClicked: (Int) -> Unit,
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    viewModel: PantallaInicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
    ) {
    val modifier = Modifier.fillMaxSize()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var showSuggestions by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        viewModel.actualizarBusqueda(searchQuery)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()//.background(color = Color(0xfff6bc66)),
        //containerColor = Color(0xfff6bc66),
        ,
        topBar = {
            if (navHostController != null) {
                TablasTopAppBar(
                    title = "MIS TABLAS",
                    onSearchClick = {
                        isSearchVisible = !isSearchVisible
                        if (!isSearchVisible) {
                            searchQuery = ""
                            showSuggestions = false
                        }
                    },
                    viewModel =viewModel,
                    navHostController
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(onHomeButtonClicked,
                onSearchClicked,
                onProfileClicked,
                onFavoritesClicked)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateTableClicked,
                shape = MaterialTheme.shapes.medium,
                //containerColor = Color(0xfff55c7a),
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir tabla"
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color(0xfff6bc66))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (isSearchVisible) {
                SearchBarBelowAppBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        searchResults = performSearch(it, uiState.tablas)
                        showSuggestions = it.isNotEmpty() && searchResults.isNotEmpty()
                    },
                    onClearClick = {
                        searchQuery = ""
                        showSuggestions = false
                    },
                    showSuggestions = showSuggestions,
                    searchResults = searchResults,
                    onResultClick = { resultText ->
                        val titleOnly = resultText.split(" (").firstOrNull() ?: resultText
                        searchQuery = titleOnly
                        showSuggestions = false
                    }
                )
            }

            InicioCuerpo(
                tablasList = if (searchQuery.isEmpty()) uiState.tablas else {
                    uiState.tablas.filter { it.titulo.contains(searchQuery, ignoreCase = true) ||
                            it.autor.contains(searchQuery, ignoreCase = true) }
                },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                ),
                onViewTableClicked,
            )
        }
    }
}

/**
 * Contenedor principal del cuerpo de la pantalla de inicio.
 *
 * Encapsula la lista de tablas dentro de un diseño Column centrado horizontalmente.
 *
 * @param tablasList Lista de tablas a mostrar
 * @param modifier Modificador para personalización del layout
 * @param contentPadding Padding interno para la lista de tablas
 */
@Composable
private fun InicioCuerpo(
    tablasList: List<Tabla>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onViewTableClicked: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        TablasList(
            tablasList = tablasList,
            contentPadding = contentPadding,
            modifier = Modifier.padding(horizontal = 8.dp),
            onViewTableClicked
        )
    }
}

/**
 * Componente que muestra una lista desplazable de tablas usando LazyColumn.
 *
 * @param tablasList Lista de tablas a mostrar
 * @param contentPadding Padding interno para la lista
 * @param modifier Modificador para personalización del layout
 */
@Composable
private fun TablasList(
    tablasList: List<Tabla>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onViewTableClicked: (Int) -> Unit,
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
                    .clickable {onViewTableClicked(tabla.id)},
            )
        }
    }
}

/**
 * Componente que representa un ítem individual de la lista de tablas.
 *
 * @param tabla Datos de la tabla a mostrar
 * @param modifier Modificador para personalización del layout
 */
@Composable
fun TablaItem(

    tabla: Tabla,
    modifier: Modifier = Modifier,
) {

    var color: Color
    var colorCardBorder: Color


    if(isSystemInDarkTheme()){
        color = Color(0xFF3F51B5)
        colorCardBorder = Color.White
    }else{
        color = Color(0xfff68c70)
        colorCardBorder = Color.Black
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
        border = BorderStroke(1.dp, colorCardBorder),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.height(50.dp).width(130.dp), contentAlignment = Alignment.Center
                ){
                    Text(
                        text = tabla.titulo.split(" ").firstOrNull() ?: "",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier.height(50.dp).width(80.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabla.autor,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier.height(50.dp).width(80.dp), contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.estrella),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Spacer(Modifier.size(2.dp))
                        Text(
                            text = "%.2f".format(tabla.valoracion),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun compartirApp() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "¿Quieres comparar valoraciones de productos? ¡Descarga SuperAhorro!\n https://github.com/ispadill/SuperAhorro")
        }

        try {
            context.startActivity(Intent.createChooser(shareIntent, "Compartir con"))
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir el menú de compartir", Toast.LENGTH_SHORT).show()
        }
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    "Compartir app",
                    //color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            onClick = {
                compartirApp()
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    "Cerrar sesión",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            },
            onClick = {
                onLogout()
                onDismissRequest()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablasTopAppBar(
    title: String,
    onSearchClick: () -> Unit,
    viewModel: PantallaInicioViewModel,
    navController: NavHostController?,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }


    Column {
        androidx.compose.material3.CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = AppBarTitleStyle
                )
            },
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
                    )
                }
            }
        )
        Divider(
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
fun SearchBarBelowAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    showSuggestions: Boolean,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PantallaInicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.background(Color(0xFFF6BC66))
            .padding(bottom = 4.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text("Buscar tablas") },
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
                    performSearch(query,uiState.tablas)
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

/**
 * Barra de navegación inferior con acciones principales.
 *
 * @param onHomeButtonClicked Callback para navegación a inicio
 * @param onSearchClicked Callback para activar búsqueda
 * @param onProfileClicked Callback para navegación a perfil
 * @param onFavoritesClicked Callback para navegación a favoritos
 */
@Composable
fun BottomNavigationBar(
    onHomeButtonClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
) {

    var bottomBarColor: Color
    var onBottomBarColor: Color

    if(isSystemInDarkTheme()){
        bottomBarColor = Color(0xFF9C27B0)
        onBottomBarColor = Color(0xFFBDBDBD)
    }else{
        bottomBarColor = Color(0xFFF55C7A)
        onBottomBarColor = Color.Black
    }

    BottomAppBar(
        containerColor = bottomBarColor, // Color de fondo de la barra inferior
        contentColor = onBottomBarColor, // Color del contenido (íconos y texto)
        actions = {
            IconButton(
                onClick = onHomeButtonClicked,
                modifier = Modifier.padding(horizontal = 28.dp)) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio",Modifier.size(40.dp))
            }
            IconButton(
                onClick = onSearchClicked,
                modifier = Modifier.padding(horizontal = 28.dp)) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar",Modifier.size(40.dp))
            }
            IconButton(
                onClick = onProfileClicked,
                modifier = Modifier.padding(horizontal = 28.dp)) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil",Modifier.size(40.dp))
            }
            IconButton(
                onClick = onFavoritesClicked,
                modifier = Modifier.padding(horizontal = 18.dp)
                .size(100.dp)){
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favoritos",Modifier.size(35.dp))
            }
        }
    )
}

/**
 * Previsualización de la pantalla principal con datos de prueba.
 *
 * Muestra una implementación estática del layout para visualización en Android Studio.
 * Utiliza callbacks vacíos para propósitos de demostración.
 */
@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {

    PantallaInicio(onHomeButtonClicked = {}, onProfileClicked = {}, onFavoritesClicked = {}, onSearchClicked = {}, onViewTableClicked = {}, onCreateTableClicked = {}, navHostController = null)

}