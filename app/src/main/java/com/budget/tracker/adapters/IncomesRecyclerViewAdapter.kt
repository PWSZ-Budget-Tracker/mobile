package com.budget.tracker.adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.budget.tracker.R
import com.budget.tracker.models.Expense
import com.budget.tracker.models.Income
import java.util.*

class IncomesRecyclerViewAdapter(private var context: Context, private var dataList: ArrayList<Income>, private val onClickListener: (Income) -> Unit, private val onEditListener: (Int) -> Unit): RecyclerView.Adapter<IncomesRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.income_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.incomeValue.text = dataList[position].amount.toString()
        holder.currency.text = dataList[position].currency.shortName
        holder.date.text = dataList[position].timeStamp.substring(0,10)

        holder.removeIncome.setOnClickListener { view ->
            onClickListener.invoke(dataList[position])
        }
        holder.editIncome.setOnClickListener{ view ->
            onEditListener.invoke(dataList[position].id)
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var incomeValue: TextView = itemView!!.findViewById(R.id.income_value)
        var currency: TextView = itemView!!.findViewById(R.id.income_currency)
        var date: TextView = itemView!!.findViewById(R.id.income_date)
        var removeIncome: Button = itemView!!.findViewById(R.id.remove_income)
        var editIncome: Button = itemView!!.findViewById(R.id.edit_income)
    }
}