package com.example.superahorro.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.UsuarioRepository
import kotlinx.coroutines.launch
import com.example.superahorro.ModeloDominio.Sesion
import java.util.Date


/**
 * ViewModel para LoginScreen que recoge los datos del formulario de login de la app
 *
 * @param usuarioRepository Repositorio para acceder a los datos de usuarios
 */
class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {
	
	/**
     * Funcion para comprobar si existe un usuario logeado con ese nombre de usuario
	 * Devuelve true en el caso que no exista, false en caso contrario
     */
    suspend fun isUsernameValid(username: String): Boolean {
        return usuarioRepository.getLoggeadoById(username)==null
    }
	/**
     * Funcion para llamar a isUsernameValid desde una corrutina
     */
    fun isUsernameValidFromUi(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isTaken = isUsernameValid(username)
            onResult(isTaken) // Devolvemos el resultado a través del callback
        }
    }
	
	/**
     * Funcion que devuelve el objeto logeado a partir del nombre de usuario (si existe lo devuelve si no devuelve null)
     */
    suspend fun devLogeado(username: String): Loggeado? {
        return usuarioRepository.getLoggeadoById(username)
    }
	/**
     * Funcion para llamar a devLogeado desde una corrutina
     */
    fun devLogeadoFromUi(username: String, onResult: (Loggeado?) -> Unit) {
        viewModelScope.launch {
            val usuario = devLogeado(username)
            if (usuario != null) {
                Sesion.usuario = usuario  // Guardamos la sesión global aquí
                Sesion.fechaLogin = Date()
            }
            onResult(usuario)
        }
    }





}


/**
 * Estado UI para la pantalla de login
 */
data class LoginUiState(
    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),
    val isEntryValid: Boolean = false
)