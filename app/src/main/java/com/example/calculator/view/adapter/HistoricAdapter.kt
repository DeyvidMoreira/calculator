package com.example.calculator.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.service.model.HistoricModel

class HistoricAdapter(var dataList: List<HistoricModel>):
    RecyclerView.Adapter<HistoricAdapter.ViewHolder>() {


        inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

            val expressionTextView: TextView = itemView.findViewById(R.id.tv_historic_expression)
            val resulTextView:TextView = itemView.findViewById(R.id.tv_historic_result)
            val equalTextView: TextView = itemView.findViewById(R.id.tv_historic_equal)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_historic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.expressionTextView.text = item.expression
        holder.resulTextView.text = item.result
        holder.equalTextView.text = "="
    }

    fun updateData(data: List<HistoricModel>){
        dataList = data
        notifyDataSetChanged()
    }


}