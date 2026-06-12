package com.pajasoft.vetcare.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pajasoft.vetcare.ui.adopcion.AdopcionDetailScreen
import com.pajasoft.vetcare.ui.adopcion.AdopcionListScreen
import com.pajasoft.vetcare.ui.auth.LoginScreen
import com.pajasoft.vetcare.ui.auth.RegisterScreen
import com.pajasoft.vetcare.ui.citas.CitaFormScreen
import com.pajasoft.vetcare.ui.perfil.PerfilScreen
import com.pajasoft.vetcare.ui.servicios.HorariosScreen
import com.pajasoft.vetcare.ui.sucursales.SucursalDetailScreen
import com.pajasoft.vetcare.ui.sucursales.SucursalesScreen

@Composable
fun VetNavGraph(startLoggedIn: Boolean) {
    val nav = rememberNavController()
    val start = if (startLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(navController = nav, startDestination = start) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onGoRegister = { nav.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { nav.popBackStack() },
                onRegistered = { nav.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            SucursalesScreen(
                onSucursalClick = { nav.navigate(Routes.sucursalDetail(it)) },
                onPerfil = { nav.navigate(Routes.PERFIL) },
                onLogout = {
                    nav.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Routes.SUCURSAL_DETAIL,
            arguments = listOf(navArgument("sucursalId") { type = NavType.StringType })
        ) { entry ->
            val sucursalId = entry.arguments?.getString("sucursalId").orEmpty()
            SucursalDetailScreen(
                sucursalId = sucursalId,
                onBack = { nav.popBackStack() },
                onServicioClick = { servicioId, servicioNombre ->
                    nav.navigate(Routes.horarios(servicioId, servicioNombre, sucursalId))
                },
                onAdopcion = { nav.navigate(Routes.ADOPCION_LIST) }
            )
        }

        composable(
            Routes.HORARIOS,
            arguments = listOf(
                navArgument("servicioId") { type = NavType.StringType },
                navArgument("servicio") { type = NavType.StringType },
                navArgument("sucursalId") { type = NavType.StringType },
            )
        ) { entry ->
            val servicioId = entry.arguments?.getString("servicioId").orEmpty()
            val servicio = Uri.decode(entry.arguments?.getString("servicio").orEmpty())
            val sucursalId = entry.arguments?.getString("sucursalId").orEmpty()
            HorariosScreen(
                servicioId = servicioId,
                servicio = servicio,
                onBack = { nav.popBackStack() },
                onContinue = { fecha, hora, horarioId ->
                    nav.navigate(Routes.citaForm(sucursalId, servicioId, servicio, fecha, hora, horarioId))
                }
            )
        }


        composable(
            Routes.CITA_FORM,
            arguments = listOf(
                navArgument("sucursalId") { type = NavType.StringType },
                navArgument("servicioId") { type = NavType.StringType },
                navArgument("servicio") { type = NavType.StringType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType },
                navArgument("horarioId") { type = NavType.StringType },
            )
        ) { entry ->
            CitaFormScreen(
                sucursalId = entry.arguments?.getString("sucursalId").orEmpty(),
                servicioId = entry.arguments?.getString("servicioId").orEmpty(),
                servicio = Uri.decode(entry.arguments?.getString("servicio").orEmpty()),
                fecha = Uri.decode(entry.arguments?.getString("fecha").orEmpty()),
                hora = Uri.decode(entry.arguments?.getString("hora").orEmpty()),
                horarioId = entry.arguments?.getString("horarioId").orEmpty(),
                onBack = { nav.popBackStack() },
                onDone = {
                    nav.navigate(Routes.PERFIL) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(Routes.ADOPCION_LIST) {
            AdopcionListScreen(
                onBack = { nav.popBackStack() },
                onMascotaClick = { nav.navigate(Routes.adopcionDetail(it)) }
            )
        }

        composable(
            Routes.ADOPCION_DETAIL,
            arguments = listOf(navArgument("mascotaId") { type = NavType.StringType })
        ) { entry ->
            AdopcionDetailScreen(
                mascotaId = entry.arguments?.getString("mascotaId").orEmpty(),
                onBack = { nav.popBackStack() },
                onAgendada = {
                    nav.navigate(Routes.PERFIL) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }

        composable(Routes.PERFIL) {
            PerfilScreen(
                onBack = { nav.popBackStack() },
                onInicio = {
                    nav.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } }
                },
                onLogout = {
                    nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }
    }
}
