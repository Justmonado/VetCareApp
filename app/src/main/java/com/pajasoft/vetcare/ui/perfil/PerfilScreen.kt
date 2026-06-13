package com.pajasoft.vetcare.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pajasoft.vetcare.Data.models.Cita
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TextButton
import com.pajasoft.vetcare.ui.components.formatFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    onBack: () -> Unit,
    onInicio: () -> Unit,
    onLogout: () -> Unit,
    vm: PerfilViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.state.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    LaunchedEffect(Unit) { vm.cargar() }
    LaunchedEffect(state.mensaje) { state.mensaje?.let { snackbar.showSnackbar(it); vm.consumirMensaje() } }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { TopAppBar(title = { Text("Mi perfil", fontWeight = FontWeight.Bold) }) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = false, onClick = onInicio,
                    icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") })
                NavigationBarItem(selected = true, onClick = {},
                    icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        when {
            state.loading -> LoadingBox(Modifier.padding(padding))
            state.error != null && state.usuario == null ->
                ErrorBox(state.error!!, onRetry = { vm.cargar() }, modifier = Modifier.padding(padding))
            else -> LazyColumn(
                Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(64.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(34.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            val u = state.usuario
                            Text(
                                if (u != null) "${u.nombre} ${u.apellido}" else "Usuario",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(u?.correo ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(u?.let { "@${it.nombreUsuario}" } ?: "",
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Mis citas", style = MaterialTheme.typography.titleMedium)
                }

                if (state.citas.isEmpty()) {
                    item {
                        Text("Aún no tienes citas agendadas.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    items(state.citas) { cita -> CitaCard(cita) { citaACancelar = cita } }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                        Text("Cerrar sesión")
                    }
                }
            }
        }
    }

    // Diálogo de confirmación de cancelación
    citaACancelar?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = { Text("¿Seguro que deseas cancelar tu cita de ${cita.servicio}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(cita.id); citaACancelar = null
                }) { Text("Cancelar cita", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { citaACancelar = null }) { Text("Volver") }
            }
        )
    }
}

@Composable
private fun CitaCard(cita: Cita, onCancelar: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(cita.servicio, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Mascota: ${cita.nombreMascota}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${formatFecha(cita.fecha)} · ${cita.hora}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onCancelar,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Text("Cancelar cita") }
        }
    }
}