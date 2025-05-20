package com.example.superahorro.ui



import android.content.Context

import android.net.Uri

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.superahorro.Datos.Loggeado

import com.example.superahorro.Datos.UsuarioRepository

import com.example.superahorro.ModeloDominio.Sesion

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch

import java.io.File

import java.io.FileOutputStream



class EditProfileViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {



    private val _uiState = MutableStateFlow(EditUiState())

    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()



    suspend fun isUsernameValid(username: String): Boolean {

        return usuarioRepository.getLoggeadoById(username) == null

    }



    fun isUsernameValidFromUi(username: String, onResult: (Boolean) -> Unit) {

        viewModelScope.launch {

            val isTaken = isUsernameValid(username)

            onResult(isTaken)

        }

    }



    suspend fun updateLog(log: Loggeado) {

        return usuarioRepository.updateLoggeado(log)

    }



    fun updateLogFromUi(log: Loggeado) {

        viewModelScope.launch {

            updateLog(log)

            _uiState.update { currentState ->

                currentState.copy(loggeado = log)

            }

        }

    }



    suspend fun isUsernameTaken(username: String): Boolean {

        return usuarioRepository.getLoggeadoById(username) != null

    }



    fun isUsernameTakenFromUi(username: String, onResult: (Boolean) -> Unit) {

        viewModelScope.launch {

            val isTaken = isUsernameTaken(username)

            onResult(isTaken)

        }

    }

    /**

     * Guarda la imagen seleccionada en el almacenamiento interno de la app

     * y devuelve la ruta del archivo guardado

     */

    fun saveProfileImage(context: Context, uri: Uri, userId: String): String {

        val inputStream = context.contentResolver.openInputStream(uri)

        val outputDir = context.getDir("profile_images", Context.MODE_PRIVATE)



        File(outputDir, "profile_$userId.jpg").delete()



        val outputFile = File(outputDir, "profile_$userId.jpg")



        FileOutputStream(outputFile).use { outputStream ->

            inputStream?.copyTo(outputStream)

        }



        return outputFile.absolutePath

    }



    /**

     * Actualiza el usuario con la nueva imagen de perfil

     */

    fun updateUserProfileImage(
        context: Context,
        usuario: Loggeado,
        imageUri: Uri,
        onComplete: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val imagePath = saveProfileImage(context, imageUri, usuario.id)
                val updatedUser = usuario.copy(imagenPerfilUri = imagePath)
                updateLog(updatedUser)
                _uiState.update { currentState ->
                    currentState.copy(loggeado = updatedUser)
                }
                Sesion.usuario = updatedUser
                onComplete()
            } catch (e: Exception) {
                onComplete()
            }
        }
    }



    /**

     * Carga el usuario actual para mostrar sus datos

     */

    fun loadCurrentUser(userId: String) {

        viewModelScope.launch {

            try {

                val user = usuarioRepository.getLoggeadoById(userId)

                user?.let {

                    _uiState.update { currentState ->

                        currentState.copy(loggeado = it)

                    }

                }

            } catch (e: Exception) {

                println("Error al cargar el usuario: ${e.message}")

            }

        }

    }

}



data class EditUiState(

    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),

    val isEntryValid: Boolean = false,

    val isLoading: Boolean = false,

    val errorMessage: String? = null

)