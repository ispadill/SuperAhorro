package com.example.superahorro.ui.tabla

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.ui.item.DetallesTabla
import com.example.inventory.ui.item.DetallesTablaDestination
import com.example.inventory.ui.item.toDetallesTabla
import com.example.superahorro.Datos.TablaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


class TablaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tablaRepository: TablaRepository
) : ViewModel() {


    //private val tablaId: Int =checkNotNull(savedStateHandle[DetallesTablaDestination.tablaIdArg])
    private val tablaId: Int = savedStateHandle[DetallesTablaDestination.tablaIdArg]?:1 //No se pasan los detalles de la tabla correctamente. Al solucionar el error, eliminar esta línea y sustituir por lo de arriba.


    val uiState: StateFlow<DetallesTablaUiState> =
        tablaRepository.getTablaById(tablaId)
            .filterNotNull()
            .map {
                DetallesTablaUiState(outOfStock = it.valoracion <= 0, detallesTabla = it.toDetallesTabla())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DetallesTablaUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun deleteTabla() {
        tablaRepository.getTablaById(tablaId).filterNotNull().map {
            tablaRepository.deleteTabla(it)
        }
    }
}

/**
 * UI state for TablaDetailsScreen
 */
data class DetallesTablaUiState(
    val outOfStock: Boolean = true,
    val detallesTabla: DetallesTabla = DetallesTabla()
)