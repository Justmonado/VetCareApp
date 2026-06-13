package com.pajasoft.vetcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pajasoft.vetcare.Data.repository.VetRepository
import com.pajasoft.vetcare.ui.adopcion.AdopcionViewModel
import com.pajasoft.vetcare.ui.auth.AuthViewModel
import com.pajasoft.vetcare.ui.citas.CitaViewModel
import com.pajasoft.vetcare.ui.perfil.PerfilViewModel
import com.pajasoft.vetcare.ui.servicios.ServicioViewModel
import com.pajasoft.vetcare.ui.sucursales.SucursalesViewModel
import kotlin.jvm.java

class VetViewModelFactory(
    private val repo: VetRepository = ServiceLocator.repository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repo) as T
        modelClass.isAssignableFrom(SucursalesViewModel::class.java) -> SucursalesViewModel(repo) as T
        modelClass.isAssignableFrom(ServicioViewModel::class.java) -> ServicioViewModel(repo) as T
        modelClass.isAssignableFrom(CitaViewModel::class.java) -> CitaViewModel(repo) as T
        modelClass.isAssignableFrom(AdopcionViewModel::class.java) -> AdopcionViewModel(repo) as T
        modelClass.isAssignableFrom(PerfilViewModel::class.java) -> PerfilViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}