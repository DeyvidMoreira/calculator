package com.example.calculator.service.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.calculator.service.constants.CalculatorConstants
import java.util.IdentityHashMap


@Entity(tableName = CalculatorConstants.TABLE.TABLE_NAME)
class HistoricModel(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val expression: String = "",
    val equalSimble: String = "=",
    val result: String = ""
)