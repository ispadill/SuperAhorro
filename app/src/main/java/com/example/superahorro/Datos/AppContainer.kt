package com.example.superahorro.data

import android.content.Context
import com.example.superahorro.Datos.BaseDeDatos
import com.example.superahorro.Datos.TablaRepository
import com.example.superahorro.Datos.UsuarioRepository
import com.example.superahorro.Datos.PlantillaRepository
import com.example.superahorro.ui.PantallaInicioViewModel

/**
 * Interface de contenedor de aplicación que proporciona instancias de repositorios
 */
interface AppContainer {
    val tablaRepository: TablaRepository
    val usuarioRepository: UsuarioRepository
    val plantillaRepository: PlantillaRepository
}

/**
 * Implementación de [AppContainer] que proporciona instancias de repositorios
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Base de datos para la aplicación
     */
    private val database = BaseDeDatos.getDatabase(context)

    /**
     * Implementación de TablaRepository que utiliza TablaDAO
     */
    override val tablaRepository: TablaRepository by lazy {
        TablaRepository(database.tablaDao())
    }

    /**
     * Implementación de UsuarioRepository que utiliza UsuarioDAO y LoggeadoDAO
     */
    override val usuarioRepository: UsuarioRepository by lazy {
        UsuarioRepository(database.usuarioDao(), database.loggeadoDao(),database.anonimosDao())
    }

    /**
     * Implementación de PlantillaRepository que utiliza PlantillaDAO
     */
    override val plantillaRepository: PlantillaRepository by lazy {
        PlantillaRepository(database.plantillaDao())
    }

}