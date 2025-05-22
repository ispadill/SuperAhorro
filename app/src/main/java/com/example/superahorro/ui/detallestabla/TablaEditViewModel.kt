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
import com.example.superahorro.Datos.TablaRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update a tabla from the [TablaRepository]'s data source.
 */
class TablaEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tablaRepository: TablaRepository
) : ViewModel() {

    /**
     * Holds current tabla ui state
     */
    var tablaUiState by mutableStateOf(TablaUiState())
        private set

    private val tablaId: Int = checkNotNull(savedStateHandle[TablaEditDestination.tablaIdArg])

    private fun validateInput(uiState: DetallesTabla = tablaUiState.detallesTabla): Boolean {
        return with(uiState) {
            titulo.isNotBlank() && autor.isNotBlank()
        }
    }

    init{
//        viewModelScope.launch {
//            tablaUiState = tablaRepository.getTablaById(tablaId)
//                .first()
//                .toTablaUiState(true)
//        }
    }

    fun updateUiState(detallesTabla: DetallesTabla) {
        tablaUiState =
            TablaUiState(detallesTabla = detallesTabla, isEntryValid = validateInput(detallesTabla))
    }

    suspend fun updateTabla() {
        if (validateInput(tablaUiState.detallesTabla)) {
            tablaRepository.updateTabla(tablaUiState.detallesTabla.toTabla())
        }
    }
}
