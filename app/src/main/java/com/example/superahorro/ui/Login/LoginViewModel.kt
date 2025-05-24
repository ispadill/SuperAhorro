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

class LoginViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    suspend fun isUsernameValid(username: String): Boolean {
        return usuarioRepository.getLoggeadoById(username)==null
    }
    fun isUsernameValidFromUi(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isTaken = isUsernameValid(username)
            onResult(isTaken) // Devolvemos el resultado a través del callback
        }
    }
    suspend fun devLogeado(username: String): Loggeado? {
        return usuarioRepository.getLoggeadoById(username)
    }
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

data class LoginUiState(
    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),
    val isEntryValid: Boolean = false
)