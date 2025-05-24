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

package com.example.superahorro.ui.tabla

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.ui.item.DetallesTabla
import com.example.inventory.ui.item.TablaEditDestination
import com.example.inventory.ui.item.TablaUiState
import com.example.inventory.ui.item.toTabla
import com.example.inventory.ui.item.toTablaUiState
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Estado UI para la pantalla de detalles de una tabla
 */
data class EditTableUiState(
    val isLoading: Boolean = true,
    val usuario: Loggeado? = null,
    val tablaUsuario: Tabla? = null,
    val error: String? = null
)

/**
 * ViewModel to retrieve and update a tabla from the [TablaRepository]'s data source.
 */
class TablaEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tablaRepository: TablaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTableUiState())
    val uiState: StateFlow<EditTableUiState> = _uiState.asStateFlow()

    private val tablaId: Int = checkNotNull(savedStateHandle[TablaEditDestination.tablaIdArg])

    init {
        cargarTablaUsuario(tablaId)
    }

    /**
     * Carga la tabla del usuario
     */
    fun cargarTablaUsuario(tablaId: Int) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val tabla = tablaRepository.getTablaById(tablaId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        tablaUsuario = tabla,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar la tabla: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Actualiza los detalles de la tabla
     */
    fun actualizarDetallesTabla(detalles: DetallesTabla) {
        _uiState.update { currentState ->
            currentState.copy(
                tablaUsuario = currentState.tablaUsuario?.copy(
                    titulo = detalles.titulo,
                    autor = detalles.autor,
                    valoracion = detalles.valoracion
                )
            )
        }
    }

    /**
     * Guarda los cambios en la tabla
     */
    suspend fun guardarCambios() {
        uiState.value.tablaUsuario?.let { tabla ->
            try {
                tablaRepository.updateTabla(tabla)
                _uiState.update {
                    it.copy(error = null)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Error al guardar cambios: ${e.message}")
                }
                throw e
            }
        }
    }

    /**
     * Valida los datos de entrada
     */
    fun validarEntrada(titulo: String, autor: String): Boolean {
        return titulo.isNotBlank() && autor.isNotBlank()
    }
}
