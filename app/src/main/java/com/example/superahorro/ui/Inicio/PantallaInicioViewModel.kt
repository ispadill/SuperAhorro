package com.example.superahorro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.UsuarioRepository
import com.example.superahorro.ModeloDominio.Sesion
import com.example.superahorro.SuperAhorroScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

/**
 * Estado UI para la pantalla de inicio
 */
data class PantallaInicioUiState(
    val tablas: List<Tabla> = emptyList(),
    val isLoading: Boolean = true,
    val usuarioActual: String = "",
    val searchQuery: String = "",
    val searchResults: List<String> = emptyList(),
    val error: String? = null
)

/**
 * ViewModel para PantallaInicio que proporciona tablas de un usuario aleatorio
 *
 * @param tablaRepository Repositorio para acceder a los datos de tablas
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 */
class PantallaInicioViewModel(
    private val tablaRepository: TablaRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    // Estado UI para la pantalla
    private val _uiState = MutableStateFlow(PantallaInicioUiState())
    val uiState: StateFlow<PantallaInicioUiState> = _uiState.asStateFlow()

    init {
        println("PantallaInicioViewModel - Sesion.usuario: ${Sesion.usuario?.id}")

        viewModelScope.launch {
            delay(100)
            Sesion.usuario?.let { usuario ->
                cargarTablasPorUsuario(usuario)
            }
        }
    }

    /**
     * Carga las tablas de un usuario aleatorio de la base de datos
     */
    fun cargarTablasDeUsuarioAleatorio() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val usuarios = usuarioRepository.getAllUsuariosLoggeados()
                val usuarioAleatorio = usuarios.random()

                if (usuarioAleatorio != null) {
                    if (usuarioAleatorio.tipo == "LOGEADO") {
                        val loggeado = usuarioRepository.getLoggeado(usuarioAleatorio.id)
                        if (loggeado != null) {
                            cargarTablasPorUsuario(loggeado)
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "No se pudo cargar el usuario loggeado"
                                )
                            }
                        }
                    } else {
                        // Si es anónimo, carga todas las tablas
                        cargarTodasLasTablas()
                    }
                } else {
                    cargarTodasLasTablas()
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Carga todas las tablas disponibles en la base de datos
     */
    private fun cargarTodasLasTablas() {
        viewModelScope.launch {
            tablaRepository.getAllTablas().collect { tablas ->
                _uiState.update {
                    it.copy(
                        tablas = tablas,
                        isLoading = false,
                        usuarioActual = "Todos los usuarios"
                    )
                }
            }
        }
    }

    /**
     * Carga las tablas asociadas a un usuario específico
     */
    private fun cargarTablasPorUsuario(usuario: Loggeado) {
        viewModelScope.launch {
            try {
                val tablasIds = usuario.tablasPropias + usuario.tablasPublicas

                tablaRepository.getAllTablas().collect { todasTablas ->
                    val tablasDelUsuario = todasTablas.filter { tabla ->
                        tabla.id in tablasIds || tabla.autor == usuario.id
                    }

                    _uiState.update {
                        it.copy(
                            tablas = tablasDelUsuario,
                            isLoading = false,
                            usuarioActual = usuario.id
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar tablas del usuario: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Actualiza el término de búsqueda y realiza la búsqueda
     */
    fun actualizarBusqueda(query: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val resultados = if (query.isNotEmpty()) {
                    currentState.tablas
                        .filter { it.titulo.contains(query, ignoreCase = true) ||
                                it.autor.contains(query, ignoreCase = true) }
                        .map { "${it.titulo} (${it.autor})" }
                } else {
                    emptyList()
                }

                currentState.copy(
                    searchQuery = query,
                    searchResults = resultados
                )
            }
        }
    }

    /**
     * Limpia el término de búsqueda
     */
    fun limpiarBusqueda() {
        _uiState.update { it.copy(searchQuery = "", searchResults = emptyList()) }
    }

    suspend fun cerrarSesion(navController: NavHostController) {
        Sesion.usuario = null
        Sesion.fechaLogin = Date()

        navController.navigate(SuperAhorroScreen.Login.name) {
            popUpTo(0)
        }
    }
}

