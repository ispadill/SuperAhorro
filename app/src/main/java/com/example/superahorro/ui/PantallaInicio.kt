package com.example.superahorro.ui

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.example.superahorro.ModeloDominio.Plantilla
import com.example.superahorro.ModeloDominio.Tabla
import com.example.superahorro.R

val plantillaDefault = Plantilla(nombre="Prueba", numColumnas =3,
    nombresColumnas = listOf("Tienda","Producto","Precio")
)

val tablasDePrueba = listOf(
    Tabla(id = "1",titulo ="Mercadona", autor = "Pedro", valoracion = 3F, numeroValoraciones = 10, plantilla = plantillaDefault),
    Tabla(id = "2",titulo ="BM", autor = "Paco", valoracion = 5F, numeroValoraciones = 4, plantilla = plantillaDefault),
    Tabla(id = "3",titulo ="Carrefour", autor = "Maria", valoracion = 1F, numeroValoraciones = 100, plantilla = plantillaDefault)
)

@Composable
fun PantallaInicio() {

    val modifier=Modifier.fillMaxSize()

    Scaffold(
        Modifier.background(color = Color(0xfff6bc66)),
        topBar = {
            TablasTopAppBar(
                title = "MIS TABLAS",
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick ={},
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
                    contentDescription = ""
                )
            }
        },
    ) { innerPadding ->
        SearchAppBar()
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TablasTopAppBar(
    title: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        colors = topAppBarColors(
            containerColor = Color(0xfff55c7a))
    )
}

@ExperimentalMaterial3Api
@Composable
fun SearchAppBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    // Customization options
    placeholder: @Composable () -> Unit = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
){
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },

                // Customizable input field implementation

                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon


        ) {
            // Show search results in a lazy column for better performance
            LazyColumn {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = supportingContent?.let { { it(resultText) } },
                        leadingContent = leadingContent,
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier
                            .clickable {
                                onResultClick(resultText)
                                expanded = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}