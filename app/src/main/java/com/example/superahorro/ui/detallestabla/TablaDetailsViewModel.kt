package com.example.superahorro.ui.tabla

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.ui.item.DetallesTabla
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.ui.PantallaPerfilUsuarioUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * Estado UI para la pantalla de detalles de una tabla
 */
data class ViewTableUiState(
    val isLoading: Boolean = true,
    val usuario: Loggeado? = null,
    val tablaUsuario: Tabla? = null,
    val error: String? = null
)

class TablaDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tablaRepository: TablaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViewTableUiState())
    val uiState: StateFlow<ViewTableUiState> = _uiState.asStateFlow()

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    /**
     * Carga todas las tablas creadas por el usuario
     *
     * @param usuarioId ID del usuario cuyas tablas se quieren cargar
     */
    fun cargarTablaUsuario(tablaId: Int) {
        viewModelScope.launch {
            try {
                val tabla = tablaRepository.getTablaById(tablaId)

                _uiState.update {
                    it.copy(
                        tablaUsuario = tabla,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error al cargar tablas del usuario: ${e.message}"
                    )
                }
            }
        }
    }

    suspend fun deleteTabla(tablaId: Int) {
        tablaRepository.getTablaById(tablaId)?.let { tablaRepository.deleteTabla(it) }
    }
}

/**
 * UI state for TablaDetailsScreen
 */
data class DetallesTablaUiState(
    val outOfStock: Boolean = true,
    val detallesTabla: DetallesTabla = DetallesTabla()
)