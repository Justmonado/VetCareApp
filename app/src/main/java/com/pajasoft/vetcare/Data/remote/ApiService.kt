package com.pajasoft.vetcare.Data.remote

import com.pajasoft.vetcare.Data.models.Cita
import com.pajasoft.vetcare.Data.models.CitaRequest
import com.pajasoft.vetcare.Data.models.Horario
import com.pajasoft.vetcare.Data.models.LoginRequest
import com.pajasoft.vetcare.Data.models.LoginResponse
import com.pajasoft.vetcare.Data.models.Mascota
import com.pajasoft.vetcare.Data.models.MessageResponse
import com.pajasoft.vetcare.Data.models.RegisterRequest
import com.pajasoft.vetcare.Data.models.Servicio
import com.pajasoft.vetcare.Data.models.Sucursal
import com.pajasoft.vetcare.Data.models.Usuario
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest) : MessageResponse

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest) : LoginResponse

    @GET("user/profile")
    suspend fun getProfile() : Usuario

    @GET("sucursales")
    suspend fun getSucursales(): List<Sucursal>

    @GET("sucursales/{id}")
    suspend fun getSucursal(@Path("id") id : String) : Sucursal

    @GET("servicios/sucursal/{sucursalId}")
    suspend fun getServiciosDeSucursal(@Path("sucursalId") sucursalId: String): List<Servicio>

    @GET("servicios/{id}/horarios")
    suspend fun getHorarios(@Path("id") id: String): List<Horario>

    @HTTP(method = "DELETE", path = "servicios/{id}/horarios", hasBody = true)
    suspend fun eliminarHorario(
        @Path("id") servicioId: String,
        @Body body: Map<String, String>
    ): MessageResponse

    @GET("adopcion")
    suspend fun getMascotas(): List<Mascota>

    @GET("adopcion/{id}")
    suspend fun getMascota(@Path("id") id: String): Mascota

    @POST("adopcion/agendar/{mascotaId}")
    suspend fun agendarAdopcion(@Path("mascotaId") mascotaId: String): MessageResponse

    @POST("citas")
    suspend fun crearCita(@Body body: CitaRequest): MessageResponse

    @GET("citas/miscitas")
    suspend fun getMisCitas(): List<Cita>

    @DELETE("citas/{id}")
    suspend fun cancelarCita(@Path("id") id: String): MessageResponse
}