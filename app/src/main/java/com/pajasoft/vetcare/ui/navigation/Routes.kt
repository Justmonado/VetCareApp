package com.pajasoft.vetcare1.ui.navigation

import android.net.Uri



object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val PERFIL = "perfil"
    const val ADOPCION_LIST = "adopcion"

    const val SUCURSAL_DETAIL = "sucursal/{sucursalId}"
    fun sucursalDetail(id: String) = "sucursal/$id"

    const val HORARIOS = "horarios/{servicioId}/{servicio}/{sucursalId}"
    fun horarios(servicioId: String, servicio: String, sucursalId: String) =
        "horarios/$servicioId/${Uri.encode(servicio)}/$sucursalId"

    const val CITA_FORM = "cita/{sucursalId}/{servicioId}/{servicio}/{fecha}/{hora}/{horarioId}"
    fun citaForm(sucursalId: String, servicioId: String, servicio: String, fecha: String, hora: String, horarioId: String) =
        "cita/$sucursalId/$servicioId/${Uri.encode(servicio)}/${Uri.encode(fecha)}/${Uri.encode(hora)}/$horarioId"

    const val ADOPCION_DETAIL = "adopcionDetail/{mascotaId}"
    fun adopcionDetail(id: String) = "adopcionDetail/$id"
}