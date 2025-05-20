package com.example.superahorro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.UsuarioRepository
import kotlinx.coroutines.launch

class EditProfileViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    suspend fun isUsernameValid(username: String): Boolean {
        return usuarioRepository.getLoggeadoById(username)==null
    }
    fun isUsernameValidFromUi(username: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isTaken = isUsernameValid(username)
            onResult(isTaken) // Devolvemos el resultado a través del callback
        }
    }
    suspend fun updateLog(log: Loggeado) {
        return usuarioRepository.updateLoggeado(log)
    }
    fun updateLogFromUi(log: Loggeado) {
        viewModelScope.launch {
            updateLog(log)
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




}
data class EditUiState(
    val loggeado: Loggeado = Loggeado("", "", "", "", listOf(), listOf(), listOf(), listOf()),
    val isEntryValid: Boolean = false
)