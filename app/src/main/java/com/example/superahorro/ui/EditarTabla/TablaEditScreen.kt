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

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.ui.AppViewModelProvider
import com.example.superahorro.ui.tabla.TablaEditViewModel
import kotlinx.coroutines.launch

object TablaEditDestination{
    val route = "item_edit"
    val titleRes = "Editar tabla"
    const val tablaIdArg = "tablaId"
    val routeWithArgs = "$route/{$tablaIdArg}"
}

/**
 * Pantalla para editar los detalles de una tabla existente.
 *
 * @param navigateBack Callback que se ejecuta al guardar o cancelar para volver atrás.
 * @param modifier Modificador para aplicar ajustes de diseño a la pantalla.
 * @param viewModel ViewModel asociado a la pantalla, encargado de la lógica de edición.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablaEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TablaEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // Mantenemos el estado local para la edición
    var detallesTabla by remember { mutableStateOf(DetallesTabla()) }

    // Actualizamos el estado local cuando se cargan los datos
    LaunchedEffect(uiState.tablaUsuario) {
        uiState.tablaUsuario?.let { tabla ->
            detallesTabla = DetallesTabla(
                id = tabla.id,
                titulo = tabla.titulo,
                autor = tabla.autor,
                valoracion = tabla.valoracion,
                numeroValoraciones = tabla.numeroValoraciones,
                plantillaId = tabla.plantillaId
            )
        }
    }

    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        ItemEntryBody(
            tablaUiState = TablaUiState(
                detallesTabla = detallesTabla,
                isEntryValid = viewModel.validarEntrada(detallesTabla.titulo, detallesTabla.autor)
            ),
            onItemValueChange = { nuevosDetalles ->
                detallesTabla = nuevosDetalles
                viewModel.actualizarDetallesTabla(nuevosDetalles)
            },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.guardarCambios()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}