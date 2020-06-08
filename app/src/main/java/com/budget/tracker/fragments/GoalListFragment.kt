package com.budget.tracker.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budget.tracker.R
import com.budget.tracker.adapters.ExpensesRecyclerViewAdapter
import com.budget.tracker.adapters.GoalsRecyclerViewAdapter
import com.budget.tracker.api.CommonResponse
import com.budget.tracker.api.ExpensesResponse
import com.budget.tracker.api.GoalsResponse
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.models.Expense
import com.budget.tracker.models.Goal
import com.budget.tracker.requests.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.expense_item.view.*
import kotlinx.android.synthetic.main.fragment_expences.view.*
import kotlinx.android.synthetic.main.goal_item.view.*
import kotlinx.android.synthetic.main.new_expense_dialog.view.*
import kotlinx.android.synthetic.main.new_goal_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class GoalListFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var goalList: ArrayList<Goal> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterGoals: GoalsRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_goal_list, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_goals)

        view.fab.setOnClickListener { view ->
            showDialog()
        }

        recyclerView = view.findViewById(R.id.recycler_view_goals)
        adapterGoals = GoalsRecyclerViewAdapter(
            baseContext,
            goalList,
            onClickListener = { goal -> removeGoal(goal) })
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterGoals

        getGoals()

        return view
    }

    fun getGoals() {
        goalList.clear()

        RetrofitClient(baseContext).instance.getGoals()
            .enqueue(object : Callback<GoalsResponse> {
                override fun onFailure(call: Call<GoalsResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<GoalsResponse>, response: Response<GoalsResponse>) {
                    if (response.code() == 200) {
                        val goalsCollection: Array<Goal> = response.body()!!.payload

                        for (goal: Goal in goalsCollection) {
                                goalList.add(goal)
                        }
                    }
                    adapterGoals.notifyDataSetChanged()
                }
            })
    }

    fun showDialog() {
        val newGoalDialogView = LayoutInflater.from(baseContext).inflate(R.layout.new_goal_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(newGoalDialogView)
            .setTitle("Nowy cel")

        val  mAlertDialog = mBuilder.show()

        newGoalDialogView.dialogAddGoal.setOnClickListener {

            val goalValue = newGoalDialogView.goalDialog.text.toString()
            val goalName = newGoalDialogView.goalDialogName.text.toString()
            val goalCurrency = newGoalDialogView.goalDialogCurrency.text.toString()

            if (goalName.isEmpty()) {
                newGoalDialogView.goal_name.requestFocus()
                return@setOnClickListener
            }

            if (goalValue.isEmpty()) {
                newGoalDialogView.goal_amount.requestFocus()
                return@setOnClickListener
            }

            if (goalCurrency.isEmpty()) {
                newGoalDialogView.goal_currency.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            addNewGoal(goalName, goalValue, goalCurrency)
        }

        newGoalDialogView.dialogCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun addNewGoal(name: String, value: String, currency: String) {
        var currencyCode = 1

        if(currency.equals("USD", true)) {
            currencyCode = 2
        }

        RetrofitClient(baseContext).instance.addNewGoal(AddGoalRequest(name, value.toDouble(), currencyCode))
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, "Dodano cel", Toast.LENGTH_LONG).show()
                        goalList.clear()
                        getGoals()
                    }
                }
            })
    }

    fun removeGoal(goal: Goal) {
        RetrofitClient(baseContext).instance.removeGoal(DeleteGoalRequest( goal.id))
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, "Usunięto cel", Toast.LENGTH_LONG).show()
                        goalList.clear()
                        getGoals()
                    }
                }
            })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseContext = context
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() = GoalListFragment()
    }
}