package com.example.calculator.service.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.calculator.service.constants.CalculatorConstants
import com.example.calculator.service.model.HistoricModel

@Database(entities = [HistoricModel::class], version = CalculatorConstants.VERSION.VERSION_DATABASE)
abstract class HistoricDatabase : RoomDatabase() {

    abstract fun historicDao(): HistoricDao

    companion object {
        private lateinit var INSTANCE: HistoricDatabase

        fun getInstance(context: Context): HistoricDatabase {


            if (!::INSTANCE.isInitialized) {
                synchronized(HistoricDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HistoricDatabase::class.java,
                        CalculatorConstants.TABLE.TABLE_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE
        }
    }


}