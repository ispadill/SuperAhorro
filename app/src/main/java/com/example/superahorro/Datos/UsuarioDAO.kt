package com.example.superahorro.Datos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.superahorro.Datos.Usuario

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios ORDER BY RANDOM() LIMIT 1")
    suspend fun getUsuarioAleatorio(): Usuario?

    @Query("SELECT * FROM usuarios ")
    suspend fun getAllUsuarios(): List<Usuario>

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun countUsuarios(): Int

    @Query("DELETE FROM usuarios")
    suspend fun deleteAll()
}