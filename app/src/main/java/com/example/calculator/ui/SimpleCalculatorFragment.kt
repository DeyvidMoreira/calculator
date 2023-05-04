package com.example.calculator.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.calculator.R
import com.example.calculator.databinding.FragmentSimpleCalculatorBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class SimpleCalculatorFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSimpleCalculatorBinding? = null

    private val binding get() = _binding!!

    private var lastNumeric = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression: Expression


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {


        _binding = FragmentSimpleCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setListeners()


        return root
    }

    override fun onClick(v: View) {

        when (v.id) {
            //Numbers
            R.id.btn_zero -> { onDigit(binding.btnZero) }
            R.id.btn_one -> { onDigit(binding.btnOne) }
            R.id.btn_two -> { onDigit(binding.btnTwo) }
            R.id.btn_three -> { onDigit(binding.btnThree) }
            R.id.btn_four -> { onDigit(binding.btnFour) }
            R.id.btn_five -> { onDigit(binding.btnFive) }
            R.id.btn_six -> { onDigit(binding.btnSix) }
            R.id.btn_seven -> { onDigit(binding.btnSeven) }
            R.id.btn_height -> { onDigit(binding.btnHeight) }
            R.id.btn_nine -> { onDigit(binding.btnNine) }
            R.id.btn_dot -> { onDigit(binding.btnDot) }

            //Operators
            R.id.btn_add -> { onOperator(binding.btnAdd) }
            R.id.btn_decrease -> { onOperator(binding.btnDecrease) }
            R.id.btn_multiply -> { onOperator(binding.btnMultiply) }
            R.id.btn_divide -> { onOperator(binding.btnDivide) }
            R.id.btn_percentage -> { onOperator(binding.btnPercentage) }
            R.id.btn_ac -> { onClear() }
            R.id.btn_back_space -> { onBackSpace() }
            R.id.btn_equal -> {
                onEqual()
                binding.tvExpression.text = binding.tvResult.text.toString().drop(1)
            }
        }
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
        binding.tvResult.visibility = View.INVISIBLE
    }

    private fun onOperator(v: View) {

        if (!stateError && lastNumeric) {
            binding.tvExpression.append((v as Button).text)
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
                    Log.e("evaluate error", "Error Arithmetic")
                    binding.tvResult.text = "Error"
                    stateError = true
                    lastNumeric = false
                }

            } else {
                binding.tvResult.text = "0"
            }

        }


    }


}


