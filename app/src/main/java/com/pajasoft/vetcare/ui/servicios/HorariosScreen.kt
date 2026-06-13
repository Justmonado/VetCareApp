package com.pajasoft.vetcare.ui.servicios


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pajasoft.vetcare.Data.models.Horario
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox
import com.pajasoft.vetcare.ui.components.PrimaryButton
import com.pajasoft.vetcare.ui.components.formatFecha


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorariosScreen(
    servicioId: String,
    servicio: String,
    onBack: () -> Unit,
    onContinue: (fecha: String, hora: String, horarioId: String) -> Unit,
    vm: ServicioViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.state.collectAsState()
    var seleccion by remember { mutableStateOf<Horario?>(null) }
    LaunchedEffect(servicioId) { vm.loadHorarios(servicioId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(servicio) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (state.horarios.isNotEmpty()) {
                Surface(shadowElevation = 8.dp) {
                    Box(Modifier.padding(16.dp)) {
                        PrimaryButton("Continuar", enabled = seleccion != null) {
                            seleccion?.let { onContinue(it.fecha, it.hora, it.id) }
                        }
                    }
                }
            }
        }
    ) { padding ->
        when {
            state.loading -> LoadingBox(Modifier.padding(padding))
            state.error != null -> ErrorBox(state.error!!, onRetry = { vm.loadHorarios(servicioId) }, modifier = Modifier.padding(padding))
            state.horarios.isEmpty() -> ErrorBox("No hay horarios disponibles para este servicio.", modifier = Modifier.padding(padding))
            else -> {
                // Agrupa por fecha
                val porFecha = state.horarios.groupBy { it.fecha }
                LazyColumn(
                    Modifier.padding(padding).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text("Fechas y horarios disponibles", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                    }
                    porFecha.forEach { (fecha, horarios) ->
                        item {
                            Text(formatFecha(fecha), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(8.dp))
                            FlowHoras(horarios, seleccion) { seleccion = it }
                            Spacer(Modifier.height(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowHoras(horarios: List<Horario>, seleccion: Horario?, onSelect: (Horario) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        horarios.forEach { h ->
            val selected = seleccion?.fecha == h.fecha && seleccion?.hora == h.hora
            FilterChip(
                selected = selected,
                onClick = { onSelect(h) },
                label = { Text(h.hora) }
            )
        }
    }
}