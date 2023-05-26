package com.example.calculator.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.calculator.R
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.service.model.HistoricModel
import com.example.calculator.service.repository.HistoricDatabase
import com.example.calculator.view.adapter.HistoricAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.objecthunter.exp4j.ExpressionBuilder
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression: TextView

    //
    private var currentResult: String = ""

    private lateinit var db: HistoricDatabase
    private lateinit var historicLiveData: LiveData<List<HistoricModel>>
    private lateinit var adapter: HistoricAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView: RecyclerView = binding.rvHistoric
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = HistoricAdapter(emptyList()) // Adaptador inicialmente vazio
        recyclerView.adapter = adapter

        setListeners()

        db = Room.databaseBuilder(
            applicationContext,
            HistoricDatabase::class.java,
            "historic-db"
        ).build()

        expression = binding.tvExpression

        historicLiveData = db.historicDao().getAll()
        historicLiveData.observe(this) { historicList ->
            adapter.updateData(historicList)
            recyclerView.scrollToPosition(adapter.itemCount - 1)

            if (historicList.isNotEmpty()) {
                binding.btnHistoric.visibility = View.VISIBLE
            } else {
                binding.btnHistoric.visibility = View.INVISIBLE
            }
        }

    }


    override fun onClick(v: View) {
        when (v.id) {
            // Numbers
            R.id.btn_zero, R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four,
            R.id.btn_five, R.id.btn_six, R.id.btn_seven, R.id.btn_height, R.id.btn_nine,
            R.id.btn_dot -> {
                onDigit(v as Button)
                handlerPressed(v.id)
            }
            // Operators
            R.id.btn_add, R.id.btn_decrease, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_percentage -> {
                onOperator(v as Button)
                handlerPressed(v.id)
            }

            R.id.btn_ac -> {
                onClear()
                handlerPressed(v.id)
            }

            R.id.btn_back_space -> {
                onBackSpace()
                handlerPressed(v.id)
            }

            R.id.btn_equal -> {
                onEqual()
                handlerPressed(v.id)
                if (expression.text.isNotEmpty()) {
                    expression.text = ""
                }
            }

            R.id.btn_historic -> {
                lifecycleScope.launch {
                    withContext(Dispatchers.Default) {
                        db.historicDao().deleteAll()
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity, getString(R.string.historic_deleted),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun handlerPressed(viewId: Int) {
        val button = findViewById<Button>(viewId)
        var background =
            ContextCompat.getDrawable(applicationContext, R.drawable.custom_button_press)
        when (viewId) {
            R.id.btn_ac -> {
                background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.shape_button_ac_pressed
                )
            }

            R.id.btn_back_space -> {
                background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.shape_back_space_pressed
                )
            }

            R.id.btn_equal -> {
                background = ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.shape_button_equal_pressed
                )
            }
        }

        button.setBackgroundDrawable(background)

        button.postDelayed({
            button.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.custom_button_not_press)
            if (button == binding.btnEqual) {
                button.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.shape_button_equal)
            }
        }, 300)
    }

    private fun setListeners() {
        binding.btnZero.setOnClickListener(this)
        binding.btnOne.setOnClickListener(this)
        binding.btnTwo.setOnClickListener(this)
        binding.btnThree.setOnClickListener(this)
        binding.btnFour.setOnClickListener(this)
        binding.btnFive.setOnClickListener(this)
        binding.btnSix.setOnClickListener(this)
        binding.btnSeven.setOnClickListener(this)
        binding.btnHeight.setOnClickListener(this)
        binding.btnNine.setOnClickListener(this)
        binding.btnDot.setOnClickListener(this)
        // Operators
        binding.btnAdd.setOnClickListener(this)
        binding.btnDecrease.setOnClickListener(this)
        binding.btnMultiply.setOnClickListener(this)
        binding.btnDivide.setOnClickListener(this)
        binding.btnPercentage.setOnClickListener(this)
        binding.btnAc.setOnClickListener(this)
        binding.btnBackSpace.setOnClickListener(this)
        binding.btnEqual.setOnClickListener(this)
        binding.btnHistoric.setOnClickListener(this)
    }

    private fun onDigit(v: Button) {
        expression = binding.tvExpression
        if (stateError) {
            expression.text = v.text
            stateError = false
        } else {
            expression.append(v.text)
        }
        lastNumeric = true
        onEqual()
    }

    private fun onClear() {
        binding.tvExpression.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        currentResult = ""
    }

    private fun onOperator(v: Button) {
        if (!stateError && lastNumeric) {
            //
            if (currentResult.isNotEmpty()) {
                binding.tvExpression.text = currentResult + v.text

            } else {
                binding.tvExpression.append(v.text)
            }
        }
        lastDot = false
        lastNumeric = false
        onEqual()
    }

    private fun onBackSpace() {
        val currentExpression = binding.tvExpression.text.toString()
        if (currentExpression.isNotEmpty()) {
            binding.tvExpression.text = currentExpression.dropLast(1)
        }

        try {
            val lastChar = currentExpression.last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            Log.e(getString(R.string.char_error), e.toString())
            binding.tvResult.text = getString(R.string.generic_error)
            stateError = true
            lastNumeric = false
            return

        }
    }

    private fun onEqual() {
        if (!lastNumeric || stateError) return

        val expressionText = binding.tvExpression.text.toString().trim()
        if (expressionText.isBlank()) {
            binding.tvResult.text = "0"
            return
        }

        val expression = when {
            expressionText.contains("/0") -> {
                binding.tvResult.text = getString(R.string.divison_error)
                stateError = true
                lastNumeric = false
                return
            }

            expressionText.contains("÷") -> {
                val modifiedInput = expressionText.replace("÷", "/")
                ExpressionBuilder(modifiedInput).build()
            }

            expressionText.contains("x") -> {
                val modifiedInput = expressionText.replace("x", "*")
                ExpressionBuilder(modifiedInput).build()
            }

            expressionText.contains("%") -> {
                val modifiedInput = expressionText.replace("%", "/100*")
                ExpressionBuilder(modifiedInput).build()
            }

            else -> ExpressionBuilder(expressionText).build()
        }

        try {
            val result = expression.evaluate()

            val roundedResult = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                BigDecimal(result).setScale(2, RoundingMode.HALF_UP).toString()
            }
            //
            currentResult = roundedResult

            binding.tvResult.visibility = View.VISIBLE
            binding.tvResult.text = roundedResult
            lifecycleScope.launch(Dispatchers.IO) {
                val calculation = HistoricModel(
                    expression = expressionText,
                    result = roundedResult
                )
                db.historicDao().insert(calculation)
            }
        } catch (e: ArithmeticException) {
            Log.e(getString(R.string.evaluate_error), getString(R.string.arithmetic_error))
            binding.tvResult.text = getString(R.string.arithmetic_error)
            stateError = true
            lastNumeric = false
        }
    }
}