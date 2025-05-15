package com.example.superahorro.Datos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AnonimosDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anonimo: Anonimos)

    @Update
    suspend fun update(anonimo: Anonimos)

    @Delete
    suspend fun delete(anonimo: Anonimos)

    @Query("DELETE FROM anonimos")
    suspend fun deleteAll()
}