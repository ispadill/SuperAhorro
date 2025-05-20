package com.example.superahorro.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Tabla
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.UsuarioRepository
import com.example.superahorro.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

/**
 * Estado UI para la pantalla de perfil del usuario
 */
data class PantallaPerfilUsuarioUiState(
    val isLoading: Boolean = true,
    val usuario: Loggeado? = null,
    val tablasUsuario: List<Tabla> = emptyList(),
    val ratingMedio: Float = 0.0f,
    val imagenPerfilBitmap: Bitmap? = null,
    val error: String? = null
)

/**
 * ViewModel para PantallaPerfilUsuario que proporciona acceso a los datos del usuario y sus tablas
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 * @param tablaRepository Repositorio para acceder a las tablas del usuario
 */
class PantallaPerfilUsuarioViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val tablaRepository: TablaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PantallaPerfilUsuarioUiState())
    val uiState: StateFlow<PantallaPerfilUsuarioUiState> = _uiState.asStateFlow()

    /**
     * Carga los datos del usuario seleccionado
     *
     * @param context Contexto de la aplicación para acceder a recursos
     * @param usuarioId ID del usuario a cargar
     */
    fun cargarDatosUsuario(context: Context, usuarioId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val usuario = usuarioRepository.getLoggeadoById(usuarioId)

                if (usuario != null) {
                    val imagenPerfil = cargarImagenPerfil(context, usuarioId)

                    _uiState.update {
                        it.copy(
                            usuario = usuario,
                            imagenPerfilBitmap = imagenPerfil,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Usuario no encontrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar datos del usuario: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Carga todas las tablas creadas por el usuario
     *
     * @param usuarioId ID del usuario cuyas tablas se quieren cargar
     */
    fun cargarTablasUsuario(usuarioId: String) {
        viewModelScope.launch {
            try {
                val tablasUsuario = tablaRepository.getTablasDeUsuario(usuarioId)
                val ratingMedio = calcularValoracionMedia(tablasUsuario)

                _uiState.update {
                    it.copy(
                        tablasUsuario = tablasUsuario,
                        ratingMedio = ratingMedio
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

    /**
     * Calcula la valoracion media de las tablas
     */
    private fun calcularValoracionMedia(tablas: List<Tabla>): Float {
        if (tablas.isEmpty()) return 0.0f

        val sumaValoraciones = tablas.sumOf { it.valoracion.toDouble() }
        return (sumaValoraciones / tablas.size).toFloat()
    }

    /**
     * Carga la imagen de perfil del usuario desde almacenamiento
     */
    private fun cargarImagenPerfil(context: Context, usuarioId: String): Bitmap {
        return try {
            val directorio = context.getDir("profile_images", Context.MODE_PRIVATE)
            val archivoImagen = File(directorio, "profile_$usuarioId.jpg")

            BitmapFactory.decodeFile(archivoImagen.absolutePath)
                ?: _uiState.value.usuario?.imagenPerfilUri?.takeIf { it != "default.jpg" }?.let { uri ->
                    BitmapFactory.decodeFile(uri)
                }
                ?: BitmapFactory.decodeResource(context.resources, R.drawable.logoapp).also { bitmap ->
                    // Actualizar el estado si usamos la imagen por defecto
                    _uiState.update { currentState ->
                        currentState.copy(
                            usuario = currentState.usuario?.copy(
                                imagenPerfilUri = "default.jpg"
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            Log.e("ImageLoad", "Error cargando imagen para $usuarioId: ${e.message}")
            BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)
        }
    }
}