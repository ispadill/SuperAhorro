/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.ui.AppViewModelProvider
import com.example.superahorro.ui.tabla.DetallesTablaUiState
import com.example.superahorro.ui.tabla.TablaDetailsViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.superahorro.Datos.BaseDeDatos
import com.example.superahorro.ModeloDominio.Sesion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ViewTableScreen(
    tablaId: Int,
    navigateToEditTabla: (Int) -> Unit,
    onReturnClicked: () -> Unit,
    onAddFavoritosClicked: () -> Unit,
    onPublicarClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TablaDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)

) {
    LaunchedEffect(tablaId) {
        println("Tabla ID recibido: $tablaId")
        viewModel.cargarTablaUsuario(tablaId)
    }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
//            InventoryTopAppBar(
//                title = stringResource(ItemDetailsDestination.titleRes),
//                canNavigateBack = true,
//                navigateUp = navigateBack
//            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { uiState.tablaUsuario?.let { navigateToEditTabla(tablaId) } },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tabla",
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        DetallesTablaCuerpo(
            onDelete = {
                coroutineScope.launch {
                    val db = BaseDeDatos.getDatabase(context)
                    val loggeadoDAO = db.loggeadoDao()
                    val usuario = Sesion.usuario?.let { loggeadoDAO.getUsuarioPorId(it.id) }

                    uiState.tablaUsuario?.let { tabla ->
                        val nuevasFavoritas = usuario?.tablasFavoritas?.toMutableList() ?: mutableListOf()
                        val nuevasPublicas = usuario?.tablasPublicas?.toMutableList() ?: mutableListOf()
                        val nuevasPropias = usuario?.tablasPropias?.toMutableList() ?: mutableListOf()

                        nuevasFavoritas.remove(tabla.id)
                        nuevasPublicas.remove(tabla.id)
                        nuevasPropias.remove(tabla.id)

                        val nuevoUsuario = usuario?.copy(
                            tablasFavoritas = nuevasFavoritas,
                            tablasPublicas = nuevasPublicas,
                            tablasPropias = nuevasPropias
                        )
                        if (nuevoUsuario != null) {
                            loggeadoDAO.update(nuevoUsuario)
                        }

                        viewModel.deleteTabla(tabla.id)
                    }

                    onReturnClicked()
                }
            },
            onAddFavoritosClicked=onAddFavoritosClicked,
            onPublicarClicked=onPublicarClicked,
            scope = coroutineScope,
            context = LocalContext.current,
            tabla = uiState.tablaUsuario,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
        println("Tabla ID recibido: ${uiState.tablaUsuario?.id}")
    }
}



@Composable
private fun DetallesTablaCuerpo(
    onPublicarClicked: () -> Unit,
    onDelete: () -> Unit,
    onAddFavoritosClicked: () -> Unit,
    scope: CoroutineScope,
    context: android.content.Context,
    tabla: Tabla?,
    modifier: Modifier = Modifier
) {
    var esPublica by remember { mutableStateOf(false) }

    LaunchedEffect(tabla?.id) {
        if (tabla != null) {
            val db = BaseDeDatos.getDatabase(context)
            val loggeadoDAO = db.loggeadoDao()
            val usuario = Sesion.usuario?.let { loggeadoDAO.getUsuarioPorId(it.id) }
            esPublica = usuario?.tablasPublicas?.contains(tabla.id) == true
        }
    }

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

        if (tabla != null) {
            DetallesTabla(
                tabla = tabla,
                esPublica = esPublica,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Botón dinámico de publicar/despublicar
        Button(
            onClick = {
                scope.launch {
                    val db = BaseDeDatos.getDatabase(context)
                    val loggeadoDAO = db.loggeadoDao()
                    val usuario = Sesion.usuario?.let { loggeadoDAO.getUsuarioPorId(it.id) }
                    val nuevasPublicas = usuario?.tablasPublicas?.toMutableList() ?: mutableListOf()

                    tabla?.let {
                        if (esPublica) {
                            nuevasPublicas.remove(it.id)
                        } else {
                            nuevasPublicas.add(it.id)
                        }
                        val nuevoUsuario = usuario?.copy(tablasPublicas = nuevasPublicas)
                        if (nuevoUsuario != null) {
                            loggeadoDAO.update(nuevoUsuario)
                            esPublica = !esPublica
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(if (esPublica) "Despublicar" else "Publicar")
        }
        Button(
            onClick = {
                scope.launch {
                    val db = BaseDeDatos.getDatabase(context)
                    val loggeadoDAO = db.loggeadoDao()

                    val usuario =  Sesion.usuario?.let { db.loggeadoDao().getUsuarioPorId(it.id) }

                    val nuevasFavoritas = usuario?.tablasFavoritas?.toMutableList()
                    if (tabla != null) {
                        if (nuevasFavoritas != null) {
                            if (!nuevasFavoritas.contains(tabla.id)) {
                                        nuevasFavoritas.add(tabla.id)
                                    onAddFavoritosClicked()
                                val nuevoUsuario = nuevasFavoritas?.let { usuario.copy(tablasFavoritas = it) }
                                if (nuevoUsuario != null) {
                                    loggeadoDAO.update(nuevoUsuario)
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Añadir a favoritos")
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(Color.Red)
        ) {
            Text("Eliminar", color = Color.Black)

        }


        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

@Composable
fun DetallesTabla(
    tabla: Tabla,
    esPublica: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            FilaTabla(
                label = "Título",
                itemDetail = tabla.titulo,
                modifier = Modifier.padding(
                    horizontal = 20.dp
                )
            )
            FilaTabla(
                label = "Autor",
                itemDetail = tabla.autor,
                modifier = Modifier.padding(
                    horizontal = 20.dp
                )
            )
            FilaTabla(
                label = "Valoracion",
                itemDetail = "%.2f".format(tabla.valoracion),
                modifier = Modifier.padding(
                    horizontal = 20.dp
                )
            )
            if (esPublica) {
                Text(
                    text = "Esta tabla es pública",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

    }
}

@Composable
private fun FilaTabla(
     label: String, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(label)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Atención") },
        text = { Text("¿Seguro que quieres eliminar esta tabla?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Si")
            }
        })
}
