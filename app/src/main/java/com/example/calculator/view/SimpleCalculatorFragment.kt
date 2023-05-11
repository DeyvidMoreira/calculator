package com.example.calculator.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.databinding.FragmentSimpleCalculatorBinding
import com.example.calculator.service.model.HistoricModel
import com.example.calculator.service.repository.HistoricDao
import com.example.calculator.service.repository.HistoricDatabase
import com.example.calculator.view.adapter.HistoricAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class SimpleCalculatorFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentSimpleCalculatorBinding? = null

    private val binding get() = _binding!!

    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression: Expression

    //Local Data
    private lateinit var database: HistoricDatabase
    private lateinit var historicDao: HistoricDao

    private lateinit var historicAdapter: HistoricAdapter
    private lateinit var recyclerView: RecyclerView
    private val historicList = mutableListOf<HistoricModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, saveInstanceState: Bundle?
    ): View {
        _binding = FragmentSimpleCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvHistoric
        recyclerView.layoutManager = LinearLayoutManager(context)
        historicAdapter = HistoricAdapter(historicList)
        recyclerView.adapter = historicAdapter

        //Starting Local Data
        database = HistoricDatabase.getInstance(requireContext())
        historicDao = database.historicDao()

        setListeners()

        return root
    }

    override fun onClick(v: View) {
        when (v.id) {
            //Numbers
            R.id.btn_zero -> {
                onDigit(binding.btnZero)
            }
            R.id.btn_one -> {
                onDigit(binding.btnOne)
            }
            R.id.btn_two -> {
                onDigit(binding.btnTwo)
            }
            R.id.btn_three -> {
                onDigit(binding.btnThree)
            }
            R.id.btn_four -> {
                onDigit(binding.btnFour)
            }
            R.id.btn_five -> {
                onDigit(binding.btnFive)
            }
            R.id.btn_six -> {
                onDigit(binding.btnSix)
            }
            R.id.btn_seven -> {
                onDigit(binding.btnSeven)
            }
            R.id.btn_height -> {
                onDigit(binding.btnHeight)
            }
            R.id.btn_nine -> {
                onDigit(binding.btnNine)
            }
            R.id.btn_dot -> {
                onDigit(binding.btnDot)
            }

            //Operators
            R.id.btn_add -> {
                onOperator(binding.btnAdd)
            }
            R.id.btn_decrease -> {
                onOperator(binding.btnDecrease)
            }
            R.id.btn_multiply -> {
                onOperator(binding.btnMultiply)
            }
            R.id.btn_divide -> {
                onOperator(binding.btnDivide)
            }
            R.id.btn_percentage -> {
                onOperator(binding.btnPercentage)
            }
            R.id.btn_ac -> {
                onClear()
            }
            R.id.btn_back_space -> {
                onBackSpace()
            }
            R.id.btn_equal -> {
                onEqual()
                CoroutineScope(Dispatchers.IO).launch {
                    saveHistoric(
                        binding.tvExpression.text.toString(),
                        binding.tvResult.text.toString()
                    )
                }
            }
        }
    }

    private fun addHistoricItem(expression: String, result: String) {
        val historicItem = HistoricModel(expression = expression, result = result)
        historicList.add(historicItem)
        historicAdapter.notifyDataSetChanged()
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
        //Operator
        binding.btnAdd.setOnClickListener(this)
        binding.btnDecrease.setOnClickListener(this)
        binding.btnMultiply.setOnClickListener(this)
        binding.btnDivide.setOnClickListener(this)
        binding.btnPercentage.setOnClickListener(this)
        binding.btnAc.setOnClickListener(this)
        binding.btnBackSpace.setOnClickListener(this)
        binding.btnEqual.setOnClickListener(this)
    }

    private fun onDigit(v: View) {
        if (stateError) {
            binding.tvExpression.text = (v as Button).text
            stateError = false
        } else {
            binding.tvExpression.append((v as Button).text)
            //TODO COLOCAR O SALVAMENTO DA EXPRESSÃO AQUI!
        }
        lastNumeric = true
    }

    private fun onClear() {
        binding.tvExpression.text = ""
        binding.tvResult.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.tvResult.visibility = View.INVISIBLE
    }

    private fun onOperator(v: View) {
        if (!stateError && lastNumeric) {
            binding.tvExpression.append((v as Button).text)
            //TODO SALVAR O OPERADOR
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    private fun onBackSpace() {
        binding.tvExpression.text = binding.tvExpression.text.toString().dropLast(1)

        try {
            val lastChar = binding.tvExpression.text.toString().last()

            if (lastChar.isDigit()) {
                onEqual()
            }
        } catch (e: Exception) {
            binding.tvResult.text = ""
            binding.tvResult.visibility = View.INVISIBLE
            Log.e("last char error", e.toString())
        }

    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val txt = binding.tvExpression.text.toString()

            if (txt.isNotBlank()) {
                if (txt.contains("/0")) {
                    binding.tvResult.text = getString(R.string.error)
                    stateError = true
                    lastNumeric = false
                } else {
                    if (txt.contains("÷")) {
                        val modifiedInput = txt.replace("÷", "/")
                        expression = ExpressionBuilder(modifiedInput).build()
                        try {
                            val result = expression.evaluate()
                            val longResult = result.toLong()
                            binding.tvResult.visibility = View.VISIBLE

                            if (result == longResult.toDouble()) {
                                binding.tvResult.text = longResult.toString()
                            } else {
                                binding.tvResult.text = result.toString()
                            }
                        } catch (e: ArithmeticException) {
                            Log.e(
                                getString(R.string.evaluate_error),
                                getString(R.string.error_arithmetic)
                            )
                            binding.tvResult.text = getString(R.string.error)
                            stateError = true
                            lastNumeric = false
                        }
                        addHistoricItem(txt, binding.tvResult.text.toString())
                    } else if (txt.contains("x")) {
                        val modifiedInput = txt.replace("x", "*")
                        expression = ExpressionBuilder(modifiedInput).build()
                        try {
                            val result = expression.evaluate()
                            val longResult = result.toLong()
                            binding.tvResult.visibility = View.VISIBLE

                            if (result == longResult.toDouble()) {
                                binding.tvResult.text = longResult.toString()
                            } else {
                                binding.tvResult.text = result.toString()
                            }
                        } catch (e: ArithmeticException) {
                            Log.e(
                                getString(R.string.evaluate_error),
                                getString(R.string.error_arithmetic)
                            )
                            binding.tvResult.text = getString(R.string.error)
                            stateError = true
                            lastNumeric = false
                        }
                    } else {
                        expression = ExpressionBuilder(txt).build()
                        try {
                            val result = expression.evaluate()
                            val longResult = result.toLong()
                            binding.tvResult.visibility = View.VISIBLE

                            if (result == longResult.toDouble()) {
                                binding.tvResult.text = longResult.toString()
                            } else {
                                binding.tvResult.text = result.toString()
                            }
                        } catch (e: ArithmeticException) {
                            Log.e(
                                getString(R.string.evaluate_error),
                                getString(R.string.error_arithmetic)
                            )
                            binding.tvResult.text = getString(R.string.error)
                            stateError = true
                            lastNumeric = false
                        }
                    }
                }
            } else {
                binding.tvResult.text = "0"
            }
        }
    }

    private suspend fun saveHistoric(expression: String, result: String): Boolean {
        return withContext(Dispatchers.Main) {
            if (expression.isEmpty() || expression.isBlank()) {
                return@withContext false
            }
            if (result.isEmpty() || result.isBlank()) {
                return@withContext false
            }
            addHistoricItem(expression, result)
            true
        }
    }

}