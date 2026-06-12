package com.pajasoft.vetcare.ui.sucursales

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox


@Composable
fun SucursalDetailScreen(
    sucursalId: String,
    onBack: () -> Unit,
    onServicioClick: (servicioId: String, servicioNombre: String) -> Unit,
    onAdopcion: () -> Unit,
    vm: SucursalesViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.detail.collectAsState()
    LaunchedEffect(sucursalId) { vm.loadDetail(sucursalId) }

    when {
        state.loading -> LoadingBox()
        state.error != null -> ErrorBox(state.error!!, onRetry = { vm.loadDetail(sucursalId) })
        else -> {
            val suc = state.sucursal!!
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    // Banner con degradado
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.padding(8.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                        }
                        Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                            Text(suc.nombre, color = Color.White, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("📍 ${suc.direccion}", color = Color.White)
                            Text("🕘 ${suc.horario}", color = Color.White)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Servicios disponibles",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Text(
                        "Selecciona un servicio para ver horarios",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(suc.servicios) { nombre ->
                    val servicio = state.servicios.find { it.nombre == nombre }
                    ServicioRow(nombre = nombre) {
                        val SERVICIO_ADOPCION = "Adopcion"
                        if (nombre == SERVICIO_ADOPCION) {
                            onAdopcion()
                        } else if (servicio != null) {
                            onServicioClick(servicio.id, servicio.nombre)
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}

private data class ServicioVisual(val icon: ImageVector, val subtitulo: String)

private fun visual(nombre: String): ServicioVisual = when (nombre) {
    "Consulta Medica" -> ServicioVisual(Icons.Default.MedicalServices, "Revisión y diagnóstico médico")
    "Estetica" -> ServicioVisual(Icons.Default.ContentCut, "Baño, corte y spa para mascotas")
    "Hotel" -> ServicioVisual(Icons.Default.Hotel, "Hospedaje y cuidado por días")
    "Adopcion" -> ServicioVisual(Icons.Default.Pets, "Perros y gatos en busca de hogar")
    else -> ServicioVisual(Icons.Default.Pets, nombre)
}

@Composable
private fun ServicioRow(nombre: String, onClick: () -> Unit) {
    val v = visual(nombre)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(v.icon, null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(nombre, fontWeight = FontWeight.Bold)
                Text(v.subtitulo, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
