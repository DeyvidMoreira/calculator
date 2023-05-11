package com.example.calculator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calculator.databinding.FragmentScientificCalculatorBinding
class ScientificCalculatorFragment : Fragment() {

    private var _binding: FragmentScientificCalculatorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {

        _binding = FragmentScientificCalculatorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

