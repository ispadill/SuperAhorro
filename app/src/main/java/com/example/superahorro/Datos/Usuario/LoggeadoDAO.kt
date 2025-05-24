package com.example.superahorro.Datos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.superahorro.Datos.Loggeado

@Dao
interface LoggeadoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(loggeado: com.example.superahorro.Datos.Loggeado)

    @Update
    suspend fun update(loggeado: Loggeado)

    @Delete
    suspend fun delete(loggeado: Loggeado)

    @Query("SELECT * FROM loggeados WHERE id = :Id")
    suspend fun getLoggeadoById(Id: String): Loggeado?

    // Obtener todos los usuarios loggeados
    @Query("SELECT * FROM loggeados WHERE id LIKE :usuarioId")
    suspend fun getUsuarioPorId(usuarioId: String): Loggeado

    @Query("SELECT * FROM loggeados WHERE id IN (:usuariosIds)")
    suspend fun getUsuariosPorIds(usuariosIds: List<String>): List<Loggeado>

    @Query("SELECT * FROM loggeados WHERE tipo == :Tipo")
    suspend fun getAllUsuariosLoggeados(Tipo:String="LOGEADO"): List<Loggeado>

    @Query("DELETE FROM loggeados")
    suspend fun deleteAll()
}