package com.example.superahorro

import android.app.Application
import com.example.superahorro.data.AppContainer
import com.example.superahorro.data.AppDataContainer

/**
 * Clase de aplicación principal para la aplicación SuperAhorro
 */
class SuperAhorroApplication : Application() {
    /**
     * AppContainer instancia utilizada por el resto de clases para obtener dependencias
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}