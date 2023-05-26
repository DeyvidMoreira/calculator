package com.example.calculator.service.repository


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.calculator.service.model.HistoricModel

@Dao
interface HistoricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(historic:HistoricModel)

    @Update
    fun update(historic: HistoricModel): Int

    @Query("SELECT * FROM calculations")
    fun getAll(): LiveData<List<HistoricModel>>

    @Query("DELETE FROM calculations")
    suspend fun deleteAll()
}