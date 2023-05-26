package com.example.calculator.service.repository


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator.service.constants.CalculatorConstants
import com.example.calculator.service.model.HistoricModel

@Database(entities = [HistoricModel::class], version = CalculatorConstants.DATABASE.VERSION_DATABASE)
abstract class HistoricDatabase : RoomDatabase() {

    abstract fun historicDao(): HistoricDao


}