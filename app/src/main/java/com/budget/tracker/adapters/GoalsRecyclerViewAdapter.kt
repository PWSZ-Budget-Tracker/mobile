package com.budget.tracker.adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.budget.tracker.R
import com.budget.tracker.models.Goal
import java.util.*

class GoalsRecyclerViewAdapter(
    private var context: Context, private var dataList: ArrayList<Goal>,
    private val onRemoveListener: (Int) -> Unit,
    private val onAddToGoalListener: (Int) -> Unit,
    private val onRemoveFromGoalListener: (Int) -> Unit,
    private val onEditListener: (Goal) -> Unit
) : RecyclerView.Adapter<GoalsRecyclerViewAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.goal_item,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text = dataList[position].name
        holder.amount.text = dataList[position].amount.toString()
        holder.totalAmount.text = dataList[position].goalAmount.toString()
        holder.currency.text = dataList[position].currency.shortName
        holder.progress.progress =
            ((dataList[position].amount * 100) / dataList[position].goalAmount).toInt()

        holder.removeGoal.setOnClickListener {
            onRemoveListener.invoke(dataList[position].id)
        }
        holder.addToGoal.setOnClickListener {
            onAddToGoalListener.invoke(dataList[position].id)
        }
        holder.removeFromGoal.setOnClickListener {
            onRemoveFromGoalListener.invoke(dataList[position].id)
        }
        holder.editGoal.setOnClickListener {
            onEditListener.invoke(dataList[position])
        }

    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var name: TextView = itemView!!.findViewById(R.id.goal_name)
        var amount: TextView = itemView!!.findViewById(R.id.goal_amount)
        var totalAmount: TextView = itemView!!.findViewById(R.id.total_goal_amount)
        var currency: TextView = itemView!!.findViewById(R.id.goal_currency)
        var progress: ProgressBar = itemView!!.findViewById(R.id.goal_progress)
        var addToGoal: Button = itemView!!.findViewById(R.id.add_to_goal)
        var removeFromGoal: Button = itemView!!.findViewById(R.id.remove_from_goal)
        var editGoal: Button = itemView!!.findViewById(R.id.edit_goal)
        var removeGoal: Button = itemView!!.findViewById(R.id.remove_goal)
    }
}