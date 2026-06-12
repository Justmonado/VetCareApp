package com.pajasoft.vetcare.ui.citas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pajasoft.vetcare.Data.models.CitaRequest
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.PrimaryButton
import com.pajasoft.vetcare.ui.components.VetTextField
import com.pajasoft.vetcare.ui.components.formatFecha


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaFormScreen(
    sucursalId: String,
    servicioId: String,
    servicio: String,
    fecha: String,
    hora: String,
    horarioId: String,
    onBack: () -> Unit,
    onDone: () -> Unit,
    vm: CitaViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var nombreMascota by remember { mutableStateOf("") }
    var nombrePersona by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { vm.cargarNombreUsuario() }
    LaunchedEffect(state.nombrePersonaSugerido) {
        if (nombrePersona.isBlank() && state.nombrePersonaSugerido.isNotBlank()) {
            nombrePersona = state.nombrePersonaSugerido
        }
    }
    LaunchedEffect(state.success) {
        if (state.success) { snackbar.showSnackbar("Cita registrada correctamente"); onDone() }
    }
    LaunchedEffect(state.error) { state.error?.let { snackbar.showSnackbar(it) } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            // Datos tomados automáticamente (RF-13)
            ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Detalle de la cita", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    InfoRow(Icons.Default.MedicalServices, "Servicio", servicio)
                    InfoRow(Icons.Default.CalendarMonth, "Fecha", formatFecha(fecha))
                    InfoRow(Icons.Default.Schedule, "Hora", hora)
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Datos del formulario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            VetTextField(nombreMascota, { nombreMascota = it }, "Nombre de la mascota")
            Spacer(Modifier.height(12.dp))
            VetTextField(nombrePersona, { nombrePersona = it }, "Nombre de la persona")
            Spacer(Modifier.height(28.dp))
            PrimaryButton("Confirmar cita", enabled = !state.loading) {
                vm.crearCita(
                    CitaRequest(
                        nombreMascota = nombreMascota.trim(),
                        nombrePersona = nombrePersona.trim(),
                        fecha = fecha,
                        hora = hora,
                        servicio = servicio,
                        sucursalId = sucursalId
                    ),
                    servicioId = servicioId,
                    horarioId = horarioId
                )
            }
            if (state.loading) {
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(10.dp))
        Text("$label: ", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}