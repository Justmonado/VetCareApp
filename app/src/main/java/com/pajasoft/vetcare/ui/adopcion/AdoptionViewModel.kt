package com.pajasoft.vetcare.ui.adopcion


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.Mascota
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdopcionListUiState(
    val loading: Boolean = true,
    val mascotas: List<Mascota> = emptyList(),
    val error: String? = null
)

data class AdopcionDetailUiState(
    val loading: Boolean = true,
    val mascota: Mascota? = null,
    val error: String? = null,
    val agendando: Boolean = false,
    val agendada: Boolean = false,
    val agendarError: String? = null
)

class AdopcionViewModel(private val repo: VetRepository) : ViewModel() {

    private val _list = MutableStateFlow(AdopcionListUiState())
    val list: StateFlow<AdopcionListUiState> = _list

    private val _detail = MutableStateFlow(AdopcionDetailUiState())
    val detail: StateFlow<AdopcionDetailUiState> = _detail

    fun loadMascotas() {
        _list.value = AdopcionListUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.getMascotas()) {
                is Resource.Success -> _list.value = AdopcionListUiState(loading = false, mascotas = r.data)
                is Resource.Error -> _list.value = AdopcionListUiState(loading = false, error = r.message)
                else -> {}
            }
        }
    }

    fun loadMascota(id: String) {
        _detail.value = AdopcionDetailUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.getMascota(id)) {
                is Resource.Success -> _detail.value = AdopcionDetailUiState(loading = false, mascota = r.data)
                is Resource.Error -> _detail.value = AdopcionDetailUiState(loading = false, error = r.message)
                else -> {}
            }
        }
    }

    /** RF-18 / RF-19: agenda sin formulario, datos automáticos. */
    fun agendar(mascotaId: String) {
        _detail.value = _detail.value.copy(agendando = true, agendarError = null)
        viewModelScope.launch {
            when (val r = repo.agendarAdopcion(mascotaId)) {
                is Resource.Success -> _detail.value = _detail.value.copy(agendando = false, agendada = true)
                is Resource.Error -> _detail.value = _detail.value.copy(agendando = false, agendarError = r.message)
                else -> {}
            }
        }
    }
}