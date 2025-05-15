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

class RegisterViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    var registerUiState by mutableStateOf(RegisterUiState())
        private set

    fun updateUiState(loggeado: Loggeado) {
        registerUiState = RegisterUiState(
            loggeado = loggeado,
            isEntryValid = validateInput(loggeado)
        )
    }

    suspend fun saveLoggeado(loggeado: Loggeado) {
        usuarioRepository.insertLoggeado(loggeado)
    }
    suspend fun saveUsuario(usuario: Usuario) {
        usuarioRepository.insertUsuario(usuario)
    }

    fun saveLoggeadoFromUi(loggeado: Loggeado) {
        // Usamos la corrutina para llamar a la función suspend
        viewModelScope.launch {
            saveLoggeado(loggeado)  // Esto ahora se llama dentro de una corrutina
        }
    }
    fun saveUserFromUi(usuario: Usuario) {
        // Usamos la corrutina para llamar a la función suspend
        viewModelScope.launch {
            saveUsuario(usuario)  // Esto ahora se llama dentro de una corrutina
        }
    }
    suspend fun isUsernameTaken(username: String): Boolean {
        return usuarioRepository.getLoggeadoById(username)!=null
    }
    fun isUsernameTakenFromUi(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isTaken = isUsernameTaken(username)
            onResult(isTaken) // Devolvemos el resultado a través del callback
        }
    }


    private fun validateInput(loggeado: Loggeado = registerUiState.loggeado): Boolean {
        return loggeado.nombre.isNotBlank() && loggeado.correo.isNotBlank() && loggeado.contraseña.isNotBlank()
    }
}

data class RegisterUiState(
    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),
    val isEntryValid: Boolean = false
)
