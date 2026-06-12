package com.pajasoft.vetcare.ui.sucursales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pajasoft.vetcare.Data.models.Sucursal
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucursalesScreen(
    onSucursalClick: (String) -> Unit,
    onPerfil: () -> Unit,
    onLogout: () -> Unit,
    vm: SucursalesViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.list.collectAsState()
    LaunchedEffect(Unit) { vm.loadSucursales() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuestras sucursales", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Salir", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true, onClick = {},
                    icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false, onClick = onPerfil,
                    icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        when {
            state.loading -> LoadingBox(Modifier.padding(padding))
            state.error != null -> ErrorBox(state.error!!, onRetry = { vm.loadSucursales() }, modifier = Modifier.padding(padding))
            else -> LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.sucursales) { suc -> SucursalCard(suc) { onSucursalClick(suc.id) } }
            }
        }
    }
}

@Composable
private fun SucursalCard(suc: Sucursal, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box {
            AsyncImage(
                model = suc.photoUrl,
                contentDescription = suc.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
            ) {
                Text(
                    "Abierto", color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text(suc.nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(6.dp))
            MetaRow(Icons.Default.LocationOn, suc.direccion)
            Spacer(Modifier.height(4.dp))
            MetaRow(Icons.Default.Schedule, suc.horario)
            Spacer(Modifier.height(12.dp))
            FlowChips(suc.servicios)
        }
    }
}

@Composable
private fun MetaRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowChips(servicios: List<String>) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        servicios.forEach { s ->
            AssistChip(onClick = {}, label = { Text(s) })
        }
    }
}
