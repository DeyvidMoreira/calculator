package com.example.calculator.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.service.model.HistoricModel

class HistoricAdapter(private val historicList: List<HistoricModel>) :
    RecyclerView.Adapter<HistoricAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_historic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historicItem = historicList[position]
        holder.expressionTextView.text = historicItem.expression
        holder.equalSimble.text = historicItem.equalSimble
        holder.resultTextView.text = historicItem.result
    }

    override fun getItemCount(): Int {
        return historicList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expressionTextView: TextView = itemView.findViewById(R.id.tv_historic_expression)
        val equalSimble: TextView = itemView.findViewById(R.id.tv_historic_equal)
        val resultTextView: TextView = itemView.findViewById(R.id.tv_historic_result)
    }
}