package com.example.superahorro.ui



import android.content.Context

import android.graphics.Bitmap

import android.graphics.BitmapFactory

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.superahorro.Datos.Loggeado

import com.example.superahorro.Datos.UsuarioRepository

import com.example.superahorro.ModeloDominio.Sesion

import com.example.superahorro.R

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

import java.io.File


/**
 * ViewModel para ProfileScreen que proporciona los datos del usuario que ha iniciado sesion
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 */
class ProfileViewModel(

    private val usuarioRepository: UsuarioRepository

) : ViewModel() {



    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()



    init {

        loadCurrentUser()

    }


	/**
     * Funcion que carga el usuario actual
     */
    fun loadCurrentUser() {

        viewModelScope.launch {

            try {

                val userId = Sesion.usuario?.id ?: return@launch

                val user = usuarioRepository.getLoggeadoById(userId)

                user?.let {

                    _uiState.value = ProfileUiState(

                        usuario = it,

                        isLoading = false

                    )

                }

            } catch (e: Exception) {

                _uiState.value = ProfileUiState(

                    error = "Error al cargar usuario: ${e.message}",

                    isLoading = false

                )

            }

        }

    }


	/**
     * Funcion que carga la imagen de perfil del usuario
     */
    fun cargarImagenPerfil(context: Context, usuarioId: String): Bitmap {

        return try {

            val user = _uiState.value.usuario ?: return getDefaultImage(context)

            val uri = user.imagenPerfilUri ?: return getDefaultImage(context)



            if (uri == "default.jpg") {

                return getDefaultImage(context)

            }



            val file = File(uri)

            if (file.exists()) {

                BitmapFactory.decodeFile(file.absolutePath) ?: getDefaultImage(context)

            } else {

                getDefaultImage(context)

            }

        } catch (e: Exception) {

            getDefaultImage(context)

        }

    }


	/**
     * Funcion que devuelve una imagen de perfil predeterminada
     */
    private fun getDefaultImage(context: Context): Bitmap {

        return BitmapFactory.decodeResource(context.resources, R.drawable.logoapp)

    }

}


/**
 * Estado UI para la pantalla de ver los datos del perfil
 */
data class ProfileUiState(

    val usuario: Loggeado? = null,

    val isLoading: Boolean = true,

    val error: String? = null

)