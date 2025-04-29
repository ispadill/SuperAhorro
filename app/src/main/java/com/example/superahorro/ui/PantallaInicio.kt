package com.example.superahorro.ui

import com.example.superahorro.ui.theme.AppBarTitleStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.superahorro.ModeloDominio.Plantilla
import com.example.superahorro.ModeloDominio.Tabla
import com.example.superahorro.R
import androidx.compose.material3.Divider

val plantillaDefault = Plantilla(nombre="Prueba", numColumnas =3,
    nombresColumnas = listOf("Tienda","Producto","Precio")
)

val tablasDePrueba = listOf(
    Tabla(id = "1",titulo ="Mercadona", autor = "Pedro", valoracion = 3F, numeroValoraciones = 10, plantilla = plantillaDefault),
    Tabla(id = "2",titulo ="BM", autor = "Paco", valoracion = 5F, numeroValoraciones = 4, plantilla = plantillaDefault),
    Tabla(id = "3",titulo ="Carrefour", autor = "Maria", valoracion = 1F, numeroValoraciones = 100, plantilla = plantillaDefault)
)

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

@Composable
fun PantallaInicio() {
    val modifier = Modifier.fillMaxSize()

    var isSearchVisible by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    var showSuggestions by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(color = Color(0xfff6bc66)),
        containerColor = Color(0xfff6bc66),
        topBar = {
            TablasTopAppBar(
                title = "MIS TABLAS",
                onSearchClick = {
                    isSearchVisible = !isSearchVisible
                    if (!isSearchVisible) {
                        searchQuery = ""
                        showSuggestions = false
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                shape = MaterialTheme.shapes.medium,
                containerColor = Color(0xfff68c70),
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
                .background(Color(0xfff6bc66))
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (isSearchVisible) {
                SearchBarBelowAppBar(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                        searchResults = performSearch(it, tablasDePrueba)
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
                tablasList = if (searchQuery.isEmpty()) tablasDePrueba else {
                    tablasDePrueba.filter { it.titulo.contains(searchQuery, ignoreCase = true) ||
                            it.autor.contains(searchQuery, ignoreCase = true) }
                },
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = innerPadding.calculateBottomPadding()
                ),
            )
        }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xfff68c70),
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.height(50.dp).width(110.dp), contentAlignment = Alignment.Center
                ){
                    Text(
                        text = tabla.titulo,
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
                            text = (tabla.valoracion).toString(),
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablasTopAppBar(
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

@Composable
fun SearchBarBelowAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    showSuggestions: Boolean,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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