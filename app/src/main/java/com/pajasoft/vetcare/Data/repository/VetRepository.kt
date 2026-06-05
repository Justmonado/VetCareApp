package com.pajasoft.vetcare.Data.repository

import com.pajasoft.vetcare.Data.local.TokenManager
import com.pajasoft.vetcare.Data.models.CitaRequest
import com.pajasoft.vetcare.Data.models.LoginRequest
import com.pajasoft.vetcare.Data.models.RegisterRequest
import com.pajasoft.vetcare.Data.remote.ApiService
import retrofit2.HttpException

class VetRepository(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {

    private fun errorMessage(e: Throwable): String = when (e) {
        is HttpException -> {
            val body = try { e.response()?.errorBody()?.string() } catch (_: Exception) { null }
            parseMessage(body) ?: "Error ${e.code()}"
        }
        else -> e.localizedMessage ?: "Error de conexión. ¿Está el servidor encendido?"
    }

    private fun parseMessage(body: String?): String? {
        if (body.isNullOrBlank()) return null
        val regex = "\"message\"\\s*:\\s*\"([^\"]*)\"".toRegex()
        return regex.find(body)?.groupValues?.getOrNull(1)
    }

    private suspend fun <T> safe(block: suspend () -> T): Resource<T> = try {
        Resource.Success(block())
    } catch (e: Exception) {
        Resource.Error(errorMessage(e))
    }

    // ---- Auth ----
    suspend fun register(req: RegisterRequest) = safe { api.register(req).message ?: "Registrado" }

    suspend fun login(req: LoginRequest): Resource<String> = try {
        val token = api.login(req).token
        tokenManager.saveToken(token)
        Resource.Success(token)
    } catch (e: Exception) {
        Resource.Error(errorMessage(e))
    }

    suspend fun logout() = tokenManager.clear()

    fun isLoggedIn() = !tokenManager.cachedToken.isNullOrBlank()

    suspend fun getProfile() = safe { api.getProfile() }

    suspend fun getSucursales() = safe { api.getSucursales() }
    suspend fun getSucursal(id: String) = safe { api.getSucursal(id) }

    suspend fun getServiciosDeSucursal(sucursalId: String) = safe { api.getServiciosDeSucursal(sucursalId) }
    suspend fun getHorarios(servicioId: String) = safe { api.getHorarios(servicioId) }
    suspend fun eliminarHorario(servicioId: String, horarioId: String) =
        safe { api.eliminarHorario(servicioId, mapOf("horarioId" to horarioId)).message ?: "Horario eliminado" }

    suspend fun getMascotas() = safe { api.getMascotas() }
    suspend fun getMascota(id: String) = safe { api.getMascota(id) }
    suspend fun agendarAdopcion(mascotaId: String) = safe { api.agendarAdopcion(mascotaId).message ?: "Cita creada" }

    suspend fun crearCita(req: CitaRequest) = safe { api.crearCita(req).message ?: "Cita registrada" }
    suspend fun getMisCitas() = safe { api.getMisCitas() }
    suspend fun cancelarCita(id: String) = safe { api.cancelarCita(id).message ?: "Cita cancelada" }
}



