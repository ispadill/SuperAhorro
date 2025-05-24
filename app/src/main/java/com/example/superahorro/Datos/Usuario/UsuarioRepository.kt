package com.example.superahorro.Datos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.superahorro.Datos.Loggeado
import com.example.superahorro.Datos.Usuario
import kotlin.math.log

class UsuarioRepository(private val usuarioDao: UsuarioDAO, private val loggeadoDao: LoggeadoDAO, private val anonimosDao: AnonimosDAO) {
    suspend fun insertUsuario(usuario: Usuario) = usuarioDao.insert(usuario)
    suspend fun getLoggeado(id: String): Loggeado? = loggeadoDao.getLoggeadoById(id)

    suspend fun insertLoggeado(loggeado: Loggeado)=loggeadoDao.insert(loggeado)

    suspend fun updateLoggeado(loggeado: Loggeado)=loggeadoDao.update(loggeado)

    suspend fun delete(loggeado: Loggeado)=loggeadoDao.delete(loggeado)

    suspend fun getLoggeadoById(Id: String): Loggeado?= loggeadoDao.getLoggeadoById(Id)

    suspend fun getUsuarioPorId(usuarioId: String): Loggeado=loggeadoDao.getUsuarioPorId(usuarioId)

    suspend fun getUsuariosPorIds(usuariosIds: List<String>): List<Loggeado> = loggeadoDao.getUsuariosPorIds(usuariosIds)

    suspend fun getAllUsuariosLoggeados(Tipo:String="LOGEADO"): List<Loggeado> = loggeadoDao.getAllUsuariosLoggeados(Tipo)

    suspend fun deleteAll()=usuarioDao.deleteAll()

    suspend fun insertAnonimo(anonimo: Anonimos)=anonimosDao.insert(anonimo)

    suspend fun updateAnonimo(anonimo: Anonimos)=anonimosDao.update(anonimo)

    suspend fun deleteAnonimo(anonimo: Anonimos)=anonimosDao.delete(anonimo)

    suspend fun deleteAllAnonimo()=anonimosDao.deleteAll()

    suspend fun deleteAllLoggeado()=loggeadoDao.deleteAll()
}