package com.pajasoft.vetcare.ui.adopcion


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox
import com.pajasoft.vetcare.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionDetailScreen(
    mascotaId: String,
    onBack: () -> Unit,
    onAgendada: () -> Unit,
    vm: AdopcionViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.detail.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    LaunchedEffect(mascotaId) { vm.loadMascota(mascotaId) }
    LaunchedEffect(state.agendada) {
        if (state.agendada) {
            snackbar.showSnackbar("Cita de adopción agendada en el horario más próximo")
            onAgendada()
        }
    }
    LaunchedEffect(state.agendarError) { state.agendarError?.let { snackbar.showSnackbar(it) } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                }
            )
        }
    ) { padding ->
        when {
            state.loading -> LoadingBox(Modifier.padding(padding))
            state.error != null -> ErrorBox(state.error!!, onRetry = { vm.loadMascota(mascotaId) }, modifier = Modifier.padding(padding))
            else -> {
                val m = state.mascota!!
                Column(
                    Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())
                ) {
                    AsyncImage(
                        model = m.photoUrl,
                        contentDescription = m.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(260.dp)
                    )
                    Column(Modifier.padding(20.dp)) {
                        Text(m.nombre, style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(12.dp))
                        DatoRow("Especie", m.especie)
                        DatoRow("Raza", m.raza)
                        DatoRow("Sexo", m.sexo)
                        DatoRow("Edad", "${m.edad} año(s)")
                        Spacer(Modifier.height(16.dp))
                        Text("Descripción", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(m.descripcion, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(28.dp))
                        PrimaryButton("Agendar cita", enabled = !state.agendando) { vm.agendar(m.id) }
                        if (state.agendando) {
                            Spacer(Modifier.height(16.dp))
                            LinearProgressIndicator(Modifier.fillMaxWidth())
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DatoRow(label: String, value: String) {
    Row(Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("$label: ", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}