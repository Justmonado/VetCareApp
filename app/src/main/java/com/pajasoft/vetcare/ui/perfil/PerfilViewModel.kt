package com.pajasoft.vetcare.ui.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.Cita
import com.pajasoft.vetcare.Data.models.Usuario
import com.pajasoft.vetcare.Data.repository.Resource
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class PerfilUiState(
    val loading: Boolean = true,
    val usuario: Usuario? = null,
    val citas: List<Cita> = emptyList(),
    val error: String? = null,
    val mensaje: String? = null
)

class PerfilViewModel(private val repo: VetRepository) : ViewModel() {
    private val _state = MutableStateFlow(PerfilUiState())
    val state: StateFlow<PerfilUiState> = _state

    fun cargar() {
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            val perfil = repo.getProfile()
            val citas = repo.getMisCitas()
            _state.value = PerfilUiState(
                loading = false,
                usuario = (perfil as? Resource.Success)?.data,
                citas = (citas as? Resource.Success)?.data ?: emptyList(),
                error = (citas as? Resource.Error)?.message ?: (perfil as? Resource.Error)?.message
            )
        }
    }

    fun cancelar(citaId: String) {
        viewModelScope.launch {
            when (val r = repo.cancelarCita(citaId)) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        citas = _state.value.citas.filterNot { it.id == citaId },
                        mensaje = "Cita cancelada"
                    )
                }
                is Resource.Error -> _state.value = _state.value.copy(mensaje = r.message)
                else -> {}
            }
        }
    }

    fun consumirMensaje() { _state.value = _state.value.copy(mensaje = null) }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch { repo.logout(); onDone() }
    }
}