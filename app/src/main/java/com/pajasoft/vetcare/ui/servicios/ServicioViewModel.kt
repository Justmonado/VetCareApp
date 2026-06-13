package com.pajasoft.vetcare.ui.servicios


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.Horario
import com.pajasoft.vetcare.Data.repository.Resource
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HorariosUiState(
    val loading: Boolean = true,
    val horarios: List<Horario> = emptyList(),
    val error: String? = null
)

class ServicioViewModel(private val repo: VetRepository) : ViewModel() {
    private val _state = MutableStateFlow(HorariosUiState())
    val state: StateFlow<HorariosUiState> = _state

    fun loadHorarios(servicioId: String) {
        _state.value = HorariosUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.getHorarios(servicioId)) {
                is Resource.Success -> _state.value = HorariosUiState(loading = false, horarios = r.data)
                is Resource.Error -> _state.value = HorariosUiState(loading = false, error = r.message)
                else -> {}
            }
        }
    }
}