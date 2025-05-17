package com.example.superahorro.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.inventory.ui.item.TablaEntryViewModel
import com.example.superahorro.SuperAhorroApplication
import com.example.superahorro.ui.PantallaInicioViewModel
import com.example.superahorro.ui.tabla.TablaDetailsViewModel
import com.example.superahorro.ui.tabla.TablaEditViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire SuperAhorro app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            PantallaInicioViewModel(
                superAhorroApplication().container.tablaRepository,
                superAhorroApplication().container.usuarioRepository
            )
        }

        initializer {
            TablaEditViewModel(
                this.createSavedStateHandle(),
                superAhorroApplication().container.tablaRepository
            )
        }

        initializer {
            TablaEntryViewModel(superAhorroApplication().container.tablaRepository)
        }

        initializer {
            TablaDetailsViewModel(
                this.createSavedStateHandle(),
                superAhorroApplication().container.tablaRepository
            )
        }
        initializer {
            RegisterViewModel(
                superAhorroApplication().container.usuarioRepository
            )
        }

        initializer {
            LoginViewModel(
                superAhorroApplication().container.usuarioRepository
            )
        }

    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [SuperAhorroApplication].
 */
fun CreationExtras.superAhorroApplication(): SuperAhorroApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SuperAhorroApplication)