package com.example.calculator.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.databinding.FragmentSimpleCalculatorBinding

class SimpleCalculatorFragment : Fragment() {

    private var _binding: FragmentSimpleCalculatorBinding? = null


    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        val viewModel = ViewModelProvider(this).get(SimpleCalculatorViewModel::class.java)

        _binding = FragmentSimpleCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}