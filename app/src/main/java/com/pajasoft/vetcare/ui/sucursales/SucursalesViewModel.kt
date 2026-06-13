package com.pajasoft.vetcare.ui.sucursales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pajasoft.vetcare.Data.models.Servicio
import com.pajasoft.vetcare.Data.models.Sucursal
import com.pajasoft.vetcare.Data.repository.Resource
import com.pajasoft.vetcare.Data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SucursalesUiState(
    val loading: Boolean = true,
    val sucursales: List<Sucursal> = emptyList(),
    val error: String? = null
)

data class DetailUiState(
    val loading: Boolean = true,
    val sucursal: Sucursal? = null,
    val servicios: List<Servicio> = emptyList(),
    val error: String? = null
)

class SucursalesViewModel(private val repo: VetRepository) : ViewModel() {

    private val _list = MutableStateFlow(SucursalesUiState())
    val list: StateFlow<SucursalesUiState> = _list

    private val _detail = MutableStateFlow(DetailUiState())
    val detail: StateFlow<DetailUiState> = _detail

    fun loadSucursales() {
        _list.value = SucursalesUiState(loading = true)
        viewModelScope.launch {
            when (val r = repo.getSucursales()) {
                is Resource.Success -> _list.value = SucursalesUiState(loading = false, sucursales = r.data)
                is Resource.Error -> _list.value = SucursalesUiState(loading = false, error = r.message)
                else -> {}
            }
        }
    }

    fun loadDetail(sucursalId: String) {
        _detail.value = DetailUiState(loading = true)
        viewModelScope.launch {
            val sucRes = repo.getSucursal(sucursalId)
            val servRes = repo.getServiciosDeSucursal(sucursalId)
            val sucursal = (sucRes as? Resource.Success)?.data
            val servicios = (servRes as? Resource.Success)?.data ?: emptyList()
            val error = (sucRes as? Resource.Error)?.message
            _detail.value = DetailUiState(
                loading = false,
                sucursal = sucursal,
                servicios = servicios,
                error = if (sucursal == null) error ?: "No se pudo cargar la sucursal" else null
            )
        }
    }
}
