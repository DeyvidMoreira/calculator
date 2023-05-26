package com.example.calculator.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calculator.service.constants.CalculatorConstants

@Entity(tableName = CalculatorConstants.DATABASE.TABLE_NAME)
data class HistoricModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,
    val result: String
)