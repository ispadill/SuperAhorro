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


/**
 * ViewModel para EditProfileScreen que recoge los datos que el usuario que ha iniciado sesion quiere cambiar y los modifica
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 */
class EditProfileViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {



    private val _uiState = MutableStateFlow(EditUiState())

    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()


	/**
     * Funcion para comprobar si existe un usuario logeado con ese nombre de usuario
	 * Devuelve true en el caso que no exista, false en caso contrario
     */
    suspend fun isUsernameValid(username: String): Boolean {

        return usuarioRepository.getLoggeadoById(username) == null

    }


	/**
     * Funcion para llamar a isUsernameValid desde una corrutina
     */
    fun isUsernameValidFromUi(username: String, onResult: (Boolean) -> Unit) {

        viewModelScope.launch {

            val isTaken = isUsernameValid(username)

            onResult(isTaken)

        }

    }


	/**
     * Funcion que actualiza en la BD los nuevos datos del usuario
     */
    suspend fun updateLog(log: Loggeado) {

        return usuarioRepository.updateLoggeado(log)

    }


	/**
     * Funcion para llamar a updateLog desde una corrutina
     */
    fun updateLogFromUi(log: Loggeado) {

        viewModelScope.launch {
            try {
                updateLog(log)
                _uiState.update { currentState ->
                    currentState.copy(loggeado = log)
                }
                Sesion.usuario = log
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = "Error al actualizar los datos.")
                }
            }
        }

    }


	/**
     * Funcion para comprobar si existe un usuario logeado con ese nombre de usuario
	 * Devuelve true en el caso que ya exista un logeado con ese username, false en caso contrario
     */
    suspend fun isUsernameTaken(username: String): Boolean {

        return usuarioRepository.getLoggeadoById(username) != null

    }


	/**
     * Funcion para llamar a isUsernameTaken desde una corrutina
     */
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



/**
 * Estado UI para la pantalla de modificar los datos del perfil
 */
data class EditUiState(

    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),

    val isEntryValid: Boolean = false,

    val isLoading: Boolean = false,

    val errorMessage: String? = null

)