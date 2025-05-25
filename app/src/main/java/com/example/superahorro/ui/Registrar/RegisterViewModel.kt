package com.example.superahorro.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.Usuario
import com.example.superahorro.Datos.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/**
 * ViewModel para RegisterScreen que recoge los datos del formulario de registro de la app
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 */
class RegisterViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    var registerUiState by mutableStateOf(RegisterUiState())
        private set

	/**
     * Funcion para llamar a validateInput y actualizar el UiState
     */
    fun updateUiState(loggeado: Loggeado) {
        registerUiState = RegisterUiState(
            loggeado = loggeado,
            isEntryValid = validateInput(loggeado)
        )
    }
	
	/**
     * Funcion que guarda el nuevo logeado en la bd
     */
    suspend fun saveLoggeado(loggeado: Loggeado) {
        usuarioRepository.insertLoggeado(loggeado)
    }
	
	/**
     * Funcion que guarda el nuevo usuario en la bd
     */
    suspend fun saveUsuario(usuario: Usuario) {
        usuarioRepository.insertUsuario(usuario)
    }
	
	/**
     * Funcion para llamar a saveLoggeado desde una corrutina
     */
    fun saveLoggeadoFromUi(loggeado: Loggeado) {
        viewModelScope.launch {
            saveLoggeado(loggeado)  // Esto ahora se llama dentro de una corrutina
        }
    }
	
	/**
     * Funcion para llamar a saveUsuario desde una corrutina
     */
    fun saveUserFromUi(usuario: Usuario) {
        viewModelScope.launch {
            saveUsuario(usuario)  // Esto ahora se llama dentro de una corrutina
        }
    }
	
	/**
     * Funcion para comprobar si existe un usuario logeado con ese nombre de usuario
	 * Devuelve true en el caso que ya exista un logeado con ese username, false en caso contrario
     */
    suspend fun isUsernameTaken(username: String): Boolean {
        return usuarioRepository.getLoggeadoById(username)!=null
    }
	
	/**
     * Funcion para llamar a isUsernameTaken desde una corrutina
     */
    fun isUsernameTakenFromUi(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isTaken = isUsernameTaken(username)
            onResult(isTaken) // Devolvemos el resultado a través del callback
        }
    }

	/**
     * Funcion que comprueba que los campos nombre,correo y contraseña del nuevo logeado no esten vacios
     */
    private fun validateInput(loggeado: Loggeado = registerUiState.loggeado): Boolean {
        return loggeado.nombre.isNotBlank() && loggeado.correo.isNotBlank() && loggeado.contraseña.isNotBlank()
    }
}


/**
 * Estado UI para la pantalla de registro
 */
data class RegisterUiState(
    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),
    val isEntryValid: Boolean = false
)
