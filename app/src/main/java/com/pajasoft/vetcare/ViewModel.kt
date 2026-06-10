package com.pajasoft.vetcare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pajasoft.vetcare.Data.repository.VetRepository
import com.pajasoft.vetcare1.ui.auth.AuthViewModel
import com.pajasoft.vetcare1.ui.sucursales.SucursalesViewModel
import kotlin.jvm.java

class VetViewModelFactory(
    private val repo: VetRepository = ServiceLocator.repository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repo) as T
        modelClass.isAssignableFrom(SucursalesViewModel::class.java) -> SucursalesViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}