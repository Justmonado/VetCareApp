package com.pajasoft.vetcare.Data.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val nombre: String,
    val apellido : String,
    val fechaNacimiento: String,
    val correo: String,
    val nombreUsuario: String,
    @SerializedName("contraseña") val contrasena : String
)

data class LoginRequest (
    val nombreUsuario: String,
    @SerializedName("contraseña") val contrasena : String
)

data class LoginResponse(val token:String)

data class MessageResponse (val message: String?)

data class Usuario(
    @SerializedName("_id") val id: String,
    val nombre : String,
    val apellido: String,
    val fechaNacimiento: String,
    val correo: String,
    val nombreUsuario: String
)

data class Sucursal(
    @SerializedName("_id") val id: String,
    val nombre : String,
    val direccion : String,
    val horario : String,
    val photoUrl: String,
    val servicios: List<String> = emptyList()
)

data class Horario(
    @SerializedName("_id") val id : String = "",
    val fecha : String,
    val hora : String
)

data class Servicio(
    @SerializedName("_id") val id : String,
    val sucursalId : String,
    val nombre : String,
    val horarios : List<Horario> = emptyList(),
    val disponible : Boolean = true
)

data class Mascota(
    @SerializedName("_id") val id : String,
    val nombre : String,
    val especie : String,
    val raza : String,
    val sexo : String,
    val edad : Int,
    val descripcion : String,
    val photoUrl: String
)

data class CitaRequest(
    val nombreMascota : String,
    val nombrePersona : String,
    val fecha : String,
    val hora : String,
    val servicio: String,
    val sucursalId: String
)

data class Cita(
    @SerializedName("_id") val id: String,
    val nombreMascota: String,
    val nombrePersona: String,
    val fecha: String,
    val hora: String,
    val servicio: String,
    val sucursalId: String
)