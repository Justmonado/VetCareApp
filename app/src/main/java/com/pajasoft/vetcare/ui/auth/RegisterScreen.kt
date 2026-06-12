package com.pajasoft.vetcare.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pajasoft.vetcare.Data.models.RegisterRequest
import com.pajasoft.vetcare.VetViewModelFactory
import com.pajasoft.vetcare.ui.components.PrimaryButton
import com.pajasoft.vetcare.ui.components.VetTextField
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    vm: AuthViewModel = viewModel(factory = VetViewModelFactory())
) {
    val state by vm.state.collectAsState()
    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }      // yyyy-MM-dd
    var correo by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) {
            snackbar.showSnackbar("Cuenta creada. Inicia sesión.")
            onRegistered()
        }
    }
    LaunchedEffect(state.error) {
        state.error?.let { snackbar.showSnackbar(it); vm.clearError() }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Crear cuenta") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Text("Completa tus datos para registrarte", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))

            VetTextField(nombre, { nombre = it }, "Nombre"); Spacer(Modifier.height(12.dp))
            VetTextField(apellido, { apellido = it }, "Apellido"); Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                label = { Text("Fecha de nacimiento (yyyy-MM-dd)") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        val c = Calendar.getInstance()
                        android.app.DatePickerDialog(
                            context,
                            { _, y, m, d -> fecha = "%04d-%02d-%02d".format(y, m + 1, d) },
                            c.get(Calendar.YEAR) - 20, 0, 1
                        ).show()
                    }) { Icon(Icons.Default.DateRange, contentDescription = "Elegir fecha") }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            VetTextField(correo, { correo = it }, "Correo electrónico", keyboardType = KeyboardType.Email)
            Spacer(Modifier.height(12.dp))
            VetTextField(usuario, { usuario = it }, "Nombre de usuario"); Spacer(Modifier.height(12.dp))
            VetTextField(contrasena, { contrasena = it }, "Contraseña", isPassword = true)
            Spacer(Modifier.height(24.dp))

            PrimaryButton("Registrarme", enabled = !state.loading) {
                vm.register(
                    RegisterRequest(
                        nombre = nombre.trim(),
                        apellido = apellido.trim(),
                        fechaNacimiento = fecha,
                        correo = correo.trim(),
                        nombreUsuario = usuario.trim(),
                        contrasena = contrasena
                    )
                )
            }
            if (state.loading) {
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}
