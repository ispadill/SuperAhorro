package com.example.superahorro

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.dimensionResource
//import com.example.superahorro.ui.StartOrderScreen
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.inventory.ui.item.TablaEditScreen
import com.example.inventory.ui.item.ViewTableScreen
import com.example.superahorro.Datos.BaseDeDatos
import com.example.superahorro.ui.EditProfileScreen
import com.example.superahorro.ui.FavoritosScreen
import com.example.superahorro.ui.LoginScreen
import com.example.superahorro.ui.PantallaBusqueda
import com.example.superahorro.ui.PantallaInicio
import com.example.superahorro.ui.PantallaPerfilUsuario
import com.example.superahorro.ui.ProfileScreen
import com.example.superahorro.ui.RegisterScreen

/**
 * Enumeración que representa las diferentes pantallas de la aplicación.
 */
enum class SuperAhorroScreen() {
    Login,
    Register,
    Main,
    ViewTable,
    CreateTable,
    OtherProfile,
    Search,
    Profile,
    EditProfile,
    Favorites,
    EditTabla;

    companion object {
        const val USER_ID_KEY = "userId"
        const val TABLE_ID_KEY="tablaId"
        fun otherProfileRoute(userId: String? = null): String {
            return if (userId != null) "OtherProfile/$userId" else OtherProfile.name
        }

        fun detallesTablaRoute(tablaId: Int? = null): String {
            return if (tablaId != null) "${ViewTable.name}/$tablaId" else ViewTable.name
        }
    }

}

/**
 * Punto principal de entrada de la aplicación.
 *
 * Configura la navegación entre pantallas y la estructura básica de la interfaz.
 *
 * @param navController Controlador de navegación para gestionar el flujo entre pantallas
 */
@Composable
fun SuperAhorroApp(
    //viewModel: SuperAhorroViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = when {
        backStackEntry?.destination?.route == null -> SuperAhorroScreen.Main
        backStackEntry!!.destination.route!!.startsWith(SuperAhorroScreen.OtherProfile.name) -> SuperAhorroScreen.OtherProfile
        else -> try {
            SuperAhorroScreen.valueOf(backStackEntry!!.destination.route!!)
        } catch (e: IllegalArgumentException) {
            SuperAhorroScreen.Main
        }
    }


//    Scaffold(
//    ) { innerPadding ->
        //val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = SuperAhorroScreen.Login.name,

            modifier = Modifier
        ) {
            composable(route = SuperAhorroScreen.Main.name) {
                PantallaInicio(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onCreateTableClicked = {
//                        navController.navigate(SuperAhorroScreen.CreateTable.name)
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onViewTableClicked = { tablaId ->
                        navController.navigate(SuperAhorroScreen.detallesTablaRoute(tablaId))
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Favorites.name)

                    },
                    navHostController = navController
                )
            }
            composable(route = SuperAhorroScreen.Login.name) {
                val context = LocalContext.current
                LoginScreen(
                    onAceptarClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onRegistrarseClicked = {
                        navController.navigate(SuperAhorroScreen.Register.name)
                    }
                )
            }
            composable(route = SuperAhorroScreen.Register.name) {
                val context = LocalContext.current
                RegisterScreen(
                    onRegistrarClicked = {
                        navController.navigate(SuperAhorroScreen.Login.name)

                    },
                    onBackClicked = { navController.navigate(SuperAhorroScreen.Login.name) }
                )
            }
            composable(
                route = "${SuperAhorroScreen.ViewTable.name}/{${SuperAhorroScreen.TABLE_ID_KEY}}",
                arguments = listOf(navArgument(SuperAhorroScreen.TABLE_ID_KEY) {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val tablaId =
                    backStackEntry.arguments?.getInt(SuperAhorroScreen.TABLE_ID_KEY) ?: run {
                        navController.navigateUp()
                        return@composable
                    }
                ViewTableScreen(
                    tablaId = tablaId,
                    onReturnClicked = { navController.navigateUp() },
                    navigateToEditTabla = { navController.navigate("${SuperAhorroScreen.EditTabla.name}/$it") }
                )
            }
            composable(route = SuperAhorroScreen.EditTabla.name) {
                val context = LocalContext.current
                TablaEditScreen(
                    navigateBack = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                )
            }
//            composable(route = SuperAhorroScreen.CreateTable.name) {
//                val context = LocalContext.current
//                CreateTableScreen(
//                    onReturnClicked = {
//                        navController.navigate(SuperAhorroScreen.Main.name)
//                    },
//                    onCreateClicked = {
//                        navController.navigate(SuperAhorroScreen.ViewTable.name)
//                    }
//                )
//            }
            composable(
                route = "${SuperAhorroScreen.OtherProfile.name}/{${SuperAhorroScreen.USER_ID_KEY}}",
                arguments = listOf(navArgument(SuperAhorroScreen.USER_ID_KEY) {
                    type = NavType.StringType
                    defaultValue = ""
                })
            ) {
                val userId =
                    backStackEntry?.arguments?.getString(SuperAhorroScreen.USER_ID_KEY) ?: ""
                val context = LocalContext.current
                PantallaPerfilUsuario(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onViewTableClicked = {
                        navController.navigate(SuperAhorroScreen.ViewTable.name)
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Favorites.name)
                    },
                    usuarioId = userId,
                    onBackButtonClicked = { navController.navigate(SuperAhorroScreen.Search.name) },
                )
            }
            composable(route = SuperAhorroScreen.Search.name) {
                val context = LocalContext.current
                PantallaBusqueda(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Favorites.name)
                    },
                    onViewUserClicked = { userId ->
                        navController.navigate(SuperAhorroScreen.otherProfileRoute(userId))
                    },
                    navHostController = navController
                )
            }
            composable(route = SuperAhorroScreen.Profile.name) {
                val context = LocalContext.current
                ProfileScreen(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
//                    onCreateTableClicked = {
//                        navController.navigate(SuperAhorroScreen.CreateTable.name)
//                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Favorites.name)

                    },
                    onEditProfileClicked = {
                        navController.navigate(SuperAhorroScreen.EditProfile.name)
                    }
                )
            }
            composable(route = SuperAhorroScreen.EditProfile.name) {
                val context = LocalContext.current
                EditProfileScreen(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onAcceptChangesClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name) {
                            popUpTo(SuperAhorroScreen.Profile.name) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(route = SuperAhorroScreen.Favorites.name) {
                FavoritosScreen(
                    onHomeButtonClicked = {
                        navController.navigate(SuperAhorroScreen.Main.name)
                    },
                    onSearchClicked = {
                        navController.navigate(SuperAhorroScreen.Search.name)
                    },
                    onProfileClicked = {
                        navController.navigate(SuperAhorroScreen.Profile.name)
                    },
                    onFavoritesClicked = {
                        navController.navigate(SuperAhorroScreen.Favorites.name)
                    },
                    onViewTableClicked = {
                        navController.navigate(SuperAhorroScreen.ViewTable.name)
                    },
                    navHostController = navController
                )
            }
        }
}