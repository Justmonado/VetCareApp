package com.pajasoft.vetcare.ui.adopcion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.pajasoft.vetcare.Data.models.Mascota
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.ErrorBox
import com.pajasoft.vetcare.ui.components.LoadingBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdopcionListScreen(
    onBack: () -> Unit,
    onMascotaClick: (String) -> Unit,
    vm: AdopcionViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.list.collectAsState()
    LaunchedEffect(Unit) { vm.loadMascotas() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mascotas en adopción") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                }
            )
        }
    ) { padding ->
        when {
            state.loading -> LoadingBox(Modifier.padding(padding))
            state.error != null -> ErrorBox(state.error!!, onRetry = { vm.loadMascotas() }, modifier = Modifier.padding(padding))
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.mascotas) { m -> MascotaCard(m) { onMascotaClick(m.id) } }
            }
        }
    }
}

@Composable
private fun MascotaCard(m: Mascota, onClick: () -> Unit) {
    ElevatedCard(onClick = onClick, shape = RoundedCornerShape(18.dp)) {
        AsyncImage(
            model = m.photoUrl,
            contentDescription = m.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(140.dp)
        )
        Column(Modifier.padding(12.dp)) {
            Text(m.nombre, fontWeight = FontWeight.Bold)
            Text(m.raza, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}