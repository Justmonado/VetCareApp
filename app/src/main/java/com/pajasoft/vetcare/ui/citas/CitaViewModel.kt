package com.pajasoft.vetcare.ui.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.CitaRequest
import com.pajasoft.vetcare.Data.repository.Resource
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CitaUiState(
    val loading: Boolean = false,
    val nombrePersonaSugerido: String = "",
    val error: String? = null,
    val success: Boolean = false
)

class CitaViewModel(private val repo: VetRepository) : ViewModel() {
    private val _state = MutableStateFlow(CitaUiState())
    val state: StateFlow<CitaUiState> = _state

    /** RF-14: nombre de la persona tomado de la cuenta autenticada. */
    fun cargarNombreUsuario() {
        viewModelScope.launch {
            val r = repo.getProfile()
            if (r is Resource.Success) {
                _state.value = _state.value.copy(
                    nombrePersonaSugerido = "${r.data.nombre} ${r.data.apellido}".trim()
                )
            }
        }
    }

    fun crearCita(req: CitaRequest, servicioId: String = "", horarioId: String = "") {
        if (req.nombreMascota.isBlank() || req.nombrePersona.isBlank()) {
            _state.value = _state.value.copy(error = "Completa el nombre de la mascota y de la persona")
            return
        }
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            when (val r = repo.crearCita(req)) {
                is Resource.Success -> {
                    // La cita ya quedó registrada: liberamos/eliminamos el horario
                    // para que no vuelva a aparecer. Si esto falla, la cita sigue siendo válida.
                    if (servicioId.isNotBlank() && horarioId.isNotBlank()) {
                        repo.eliminarHorario(servicioId, horarioId)
                    }
                    _state.value = _state.value.copy(loading = false, success = true)
                }
                is Resource.Error -> _state.value = _state.value.copy(loading = false, error = r.message)
                else -> {}
            }
        }
    }
}