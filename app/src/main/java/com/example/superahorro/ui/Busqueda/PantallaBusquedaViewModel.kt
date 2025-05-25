package com.example.superahorro.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.UsuarioRepository
import com.example.superahorro.ModeloDominio.Sesion
import com.example.superahorro.R
import com.example.superahorro.SuperAhorroScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

/**
 * Estado UI para la pantalla de búsqueda de usuarios
 */
data class PantallaBusquedaUiState(
    val usuarios: List<Loggeado> = emptyList(),
    val usuariosConRating: List<UsuarioConRating> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val searchResults: List<String> = emptyList(),
    val error: String? = null
)

/**
 * Datos extendidos de usuario incluyendo la valoración media
 */
data class UsuarioConRating(
    val usuario: Loggeado,
    val ratingMedio: Float = 0.0f,
    var imagenPerfilBitmap: Bitmap? = null
)

/**
 * ViewModel para PantallaBusqueda que proporciona acceso a los usuarios registrados y sus valoraciones
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 * @param tablaRepository Repositorio para acceder a las tablas y calcular valoraciones
 */
class PantallaBusquedaViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val tablaRepository: TablaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PantallaBusquedaUiState())
    val uiState: StateFlow<PantallaBusquedaUiState> = _uiState.asStateFlow()

    init {
        cargarTodosLosUsuarios()
    }

    /**
     * Carga todos los usuarios registrados en la base de datos
     */
    fun cargarTodosLosUsuarios() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val todosUsuarios = usuarioRepository.getAllUsuariosLoggeados("LOGEADO")

                _uiState.update {
                    it.copy(
                        usuarios = todosUsuarios,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar usuarios: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Carga usuarios con sus valoraciones medias e imágenes de perfil
     */
    fun cargarUsuariosConRatings(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val todosUsuarios = usuarioRepository.getAllUsuariosLoggeados("LOGEADO")
                val usuariosConRating = todosUsuarios.map { usuario ->
                    val ratingMedio = calcularValoracionMediaUsuario(usuario)
                    val imagenPerfil = cargarImagenPerfil(context, usuario.id)

                    UsuarioConRating(
                        usuario = usuario,
                        ratingMedio = ratingMedio,
                        imagenPerfilBitmap = imagenPerfil
                    )
                }

                _uiState.update {
                    it.copy(
                        usuarios = todosUsuarios,
                        usuariosConRating = usuariosConRating,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar usuarios: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Calcula la valoracion media de las tablas del usuario
     */
    private suspend fun calcularValoracionMediaUsuario(usuario: Loggeado): Float {
        val tablasUsuario = tablaRepository.getTablasDeUsuario(usuario.id)

        if (tablasUsuario.isEmpty()) return 0.0f

        val sumaValoraciones = tablasUsuario.sumOf { it.valoracion.toDouble() }
        return (sumaValoraciones / tablasUsuario.size).toFloat()
    }

    /**
     * Carga la imagen de perfil del usuario desde almacenamiento
     */
    fun cargarImagenPerfil(context: Context, usuarioId: String): Bitmap {
        return try {
            val directorio = context.getDir("profile_images", Context.MODE_PRIVATE)
            val archivoImagen = File(directorio, "profile_$usuarioId.jpg")

            if (archivoImagen.exists()) {
                BitmapFactory.decodeFile(archivoImagen.absolutePath)?.also {
                    _uiState.update { currentState ->
                        val updatedUsers = currentState.usuariosConRating.map { user ->
                            if (user.usuario.id == usuarioId) {
                                user.copy(imagenPerfilBitmap = it)
                            } else {
                                user
                            }
                        }
                        currentState.copy(usuariosConRating = updatedUsers)
                    }
                } ?: BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)
            } else {
                BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)
            }
        } catch (e: Exception) {
            BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)
        }
    }

    /**
     * Actualiza el término de búsqueda y realiza la búsqueda
     */
    fun actualizarBusqueda(query: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val resultados = if (query.isNotEmpty()) {
                    currentState.usuarios
                        .filter { it.id.contains(query, ignoreCase = true) ||
                                it.nombre.contains(query, ignoreCase = true) ||
                                it.tipo.contains(query, ignoreCase = true) }
                        .map { "${it.id} (${it.nombre})" }
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
     * Cierra la sesión del usuario actual
     * Regresa a la pantalla de login
     */
    fun cerrarSesion(navController: NavHostController) {
        Sesion.usuario = null
        Sesion.fechaLogin = Date()

        navController.navigate(SuperAhorroScreen.Login.name) {
            popUpTo(0)
        }
    }
}