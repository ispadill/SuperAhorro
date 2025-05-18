package com.example.superahorro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
        cargarTablasDeUsuarioAleatorio()
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

                if (tablasIds.isEmpty()) {
                    // Si el usuario no tiene tablas, cargar todas las tablas
                    cargarTodasLasTablas()
                    return@launch
                }

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
}

/*Funciones imagenes
En la clase de la interfaz: viewModelScope.launch {
    guardarImagenPerfil(context, "Juan", nuevoBitmap)
}
En el viewmodel:
suspend fun guardarImagenPerfil(context: Context, usuarioId: String, bitmap: Bitmap) {
    val directorio = context.getDir("profile_images", Context.MODE_PRIVATE)
    val archivoImagen = File(directorio, "$usuarioId.jpg")

    try {
        FileOutputStream(archivoImagen).use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
        }

        // Actualizar URI en la base de datos
        val loggeado = usuarioRepository.getLoggeadoById(usuarioId)
        loggeado?.let {
            it.imagenPerfilUri = archivoImagen.absolutePath
            usuarioRepository.updateLoggeado(it)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
En la clase de la interfaz: viewModelScope.launch {
    guardarImagenPerfil(context, "Juan", nuevoBitmap)
}
En el viewmodel:
val imagen = cargarImagenPerfil(context, "Juan")
fun cargarImagenPerfil(context: Context, usuarioId: String): Bitmap {
    val loggeado = usuarioRepository.getLoggeadoById(usuarioId)
    val uri = loggeado?.imagenPerfilUri ?: "default.jpg" // Si es null, usa predeterminada

    val directorio = context.getDir("profile_images", Context.MODE_PRIVATE)
    val archivoImagen = File(directorio, if (uri == "default.jpg") uri else "$usuarioId.jpg")

    return BitmapFactory.decodeFile(archivoImagen.absolutePath) ?:
           BitmapFactory.decodeResource(context.resources, R.drawable.default)
}
 */