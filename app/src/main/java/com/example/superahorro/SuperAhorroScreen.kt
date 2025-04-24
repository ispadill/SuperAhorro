package com.example.superahorro

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
import androidx.compose.ui.Modifier
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


enum class SuperAhorroScreen() {
    Login,
    Register,
    Main,
    ViewTable,
    CreateTable,
    OtherProfile,
    Search,
    Profile,
    EditProfile
}

@Composable
fun SuperAhorroApp(
    viewModel: SuperAhorroViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = SuperAhorroScreen.valueOf(
        backStackEntry?.destination?.route ?: SuperAhorroScreen.Main.name

    )


    Scaffold(
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = SuperAhorroScreen.Main.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SuperAhorroScreen.Main.name) {
                MainScreen(
                    quantityOptions = DataSource.quantityOptions,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
            composable(route = SuperAhorroScreen.Login.name) {
                val context = LocalContext.current
                LoginScreen(
                    subtotal = uiState.price,
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setFlavor(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = SuperAhorroScreen.Register.name) {
                val context = LocalContext.current
                RegisterScreen(

                )
            }
            composable(route = SuperAhorroScreen.ViewTable.name) {
                val context = LocalContext.current
                ViewTableScreen(

                )
            }
            composable(route = SuperAhorroScreen.CreateTable.name) {
                val context = LocalContext.current
                CreateTableScreen(

                )
            }
            composable(route = SuperAhorroScreen.OtherProfile.name) {
                val context = LocalContext.current
                OtherProfileScreen(

                )
            }
            composable(route = SuperAhorroScreen.Search.name) {
                val context = LocalContext.current
                SearchScreen(

                )
            }
            composable(route = SuperAhorroScreen.Profile.name) {
                val context = LocalContext.current
                ProfileScreen(

                )
            }
            composable(route = SuperAhorroScreen.EditProfile.name) {
                val context = LocalContext.current
                EditProfileScreen(

                )
            }
        }
    }
}