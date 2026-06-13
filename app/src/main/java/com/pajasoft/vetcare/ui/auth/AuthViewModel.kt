package com.pajasoft.vetcare.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.LoginRequest
import com.pajasoft.vetcare.Data.models.RegisterRequest
import com.pajasoft.vetcare.Data.repository.Resource
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading : Boolean = false,
    val error : String? = null,
    val success : Boolean = false
)

class AuthViewModel (private val repo: VetRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun login(usuario: String, contrasena: String) {
        if (usuario.isBlank() || contrasena.isBlank()) {
            _state.value = AuthUiState(error = "Completa usuario y contraseña")
            return
        }
        _state.value = AuthUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.login(LoginRequest(usuario.trim(), contrasena))) {
                is Resource.Success -> _state.value = AuthUiState(success = true)
                is Resource.Error -> _state.value = AuthUiState(error = r.message)
                else -> {}
            }
        }

    }
    fun register(req: RegisterRequest) {
        if (req.nombre.isBlank() || req.apellido.isBlank() || req.correo.isBlank() ||
            req.nombreUsuario.isBlank() || req.contrasena.isBlank() || req.fechaNacimiento.isBlank()
        ) {
            _state.value = AuthUiState(error = "Todos los campos son obligatorios")
            return
        }
        _state.value = AuthUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.register(req)) {
                is Resource.Success -> _state.value = AuthUiState(success = true)
                is Resource.Error -> _state.value = AuthUiState(error = r.message)
                else -> {}
            }
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}