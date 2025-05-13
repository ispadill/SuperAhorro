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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 */
class TablaEntryViewModel(private val tablaRepository: TablaRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var tablaUiState by mutableStateOf(TablaUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(detallesTabla: DetallesTabla) {
        tablaUiState =
            TablaUiState(detallesTabla = detallesTabla, isEntryValid = validateInput(detallesTabla))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            tablaRepository.insertTabla(tablaUiState.detallesTabla.toTabla())
        }
    }

    private fun validateInput(uiState: DetallesTabla = tablaUiState.detallesTabla): Boolean {
        return with(uiState) {
            titulo.isNotBlank() && autor.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class TablaUiState(
    val detallesTabla: DetallesTabla= DetallesTabla(),
    val isEntryValid: Boolean = false
)

data class DetallesTabla(
    val id: Int = 0,
    val titulo: String="",
    val autor: String="",
    val valoracion: Float=0.0f,
    val numeroValoraciones: Int=0,
    val plantillaId: Int=0
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.precio] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.valoracion] is not a valid [Int], then the quantity will be set to 0
 */
fun DetallesTabla.toTabla(): Tabla = Tabla(
    id = id,
    titulo = titulo,
    autor=autor,
    valoracion = valoracion,
    numeroValoraciones=numeroValoraciones,
    plantillaId=plantillaId
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Tabla.toTablaUiState(isEntryValid: Boolean = false): TablaUiState = TablaUiState(
    detallesTabla = this.toDetallesTabla(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Tabla.toDetallesTabla(): DetallesTabla = DetallesTabla(
    id = id,
    titulo = titulo,
    autor=autor,
    valoracion = valoracion,
    numeroValoraciones=numeroValoraciones,
    plantillaId=plantillaId
)
