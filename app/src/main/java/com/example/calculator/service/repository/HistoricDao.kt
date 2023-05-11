package com.example.calculator.service.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.calculator.service.model.HistoricModel


@Dao
interface HistoricDao {

    @Insert
    suspend fun insert (historicModel: HistoricModel)

    @Update
    suspend fun update (historicModel: HistoricModel)

    @Query(value = "SELECT * FROM `table-of-historic`")
    fun fetchAllHistoric(): kotlinx.coroutines.flow.Flow<List<HistoricModel>>


}