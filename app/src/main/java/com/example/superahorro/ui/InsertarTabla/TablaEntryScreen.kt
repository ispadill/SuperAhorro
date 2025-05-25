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
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superahorro.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import androidx.compose.ui.unit.dp
import com.example.superahorro.ModeloDominio.Sesion

/**
 * Pantalla que permite al usuario ingresar los datos de una nueva tabla y guardarlos.
 *
 * @param navigateBack Función que se ejecuta al guardar o cancelar para volver atrás.
 * @param canNavigateBack Indica si se puede volver atrás.
 * @param viewModel ViewModel encargado de la lógica de entrada de datos.
 */
@Composable
fun TablaEntryScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TablaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        ItemEntryBody(
            tablaUiState = viewModel.tablaUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}
/**
 * Cuerpo del formulario de entrada de la tabla, incluye campos y el botón de guardar.
 *
 * @param tablaUiState Estado de la UI con los datos actuales y su validez.
 * @param onItemValueChange Función que se llama cuando se modifica algún campo del formulario.
 * @param onSaveClick Acción a realizar al hacer clic en el botón de guardar.
 * @param modifier Modificador de estilo para ajustar el diseño.
 */
@Composable
fun ItemEntryBody(
    tablaUiState: TablaUiState,
    onItemValueChange: (DetallesTabla) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier.padding(16.dp)
        ) {
        TablaInputForm(
            detallesTabla = tablaUiState.detallesTabla,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = tablaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
/**
 * Formulario de entrada de los campos que conforman una tabla.
 *
 * @param detallesTabla Datos actuales de la tabla a mostrar o editar.
 * @param modifier Modificador para ajustar el diseño del formulario.
 * @param onValueChange Función que se llama al modificar algún campo editable.
 * @param enabled Indica si los campos deben estar habilitados para edición.
 */
@Composable
fun TablaInputForm(
    detallesTabla: DetallesTabla,
    modifier: Modifier = Modifier,
    onValueChange: (DetallesTabla) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = detallesTabla.titulo,
            onValueChange = { onValueChange(detallesTabla.copy(titulo = it)) },
            label = { Text("Titulo tabla") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = detallesTabla.autor,
            onValueChange = {},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Autor") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            leadingIcon = {},
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Campos obligatorios",
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

