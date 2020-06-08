package com.budget.tracker.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.budget.tracker.MainActivity
import com.budget.tracker.R
import com.budget.tracker.api.ExpensesResponse
import com.budget.tracker.api.IncomesResponse
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.models.Expense
import com.budget.tracker.models.Income
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var baseContext: Context
    private var incomesEur: Float = 0.0F
    private var incomesUsd: Float = 0.0F
    private var incomesPln: Float = 0.0F
    private var expensesEur: Float = 0.0F
    private var expensesUsd: Float = 0.0F
    private var expensesPln: Float = 0.0F


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_home)

        view.homeExpensesCard.setOnClickListener {
            (activity as MainActivity).switchFragment(CategoryListFragment.newInstance(0))
        }

        view.homeIncomesCard.setOnClickListener {
            (activity as MainActivity).switchFragment(CategoryListFragment.newInstance(1))
        }

        view.homeGoalsCard.setOnClickListener {
            (activity as MainActivity).switchFragment(GoalListFragment.newInstance())
        }

        getExpenses()
        getIncomes()

        return view
    }

    @SuppressLint("SimpleDateFormat")
    private fun getExpenses() {

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        RetrofitClient(baseContext).instance.getExpenses(dateFormat.format(Date()))
            .enqueue(object : Callback<ExpensesResponse> {
                override fun onFailure(call: Call<ExpensesResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ExpensesResponse>,
                    response: Response<ExpensesResponse>
                ) {
                    if (response.code() == 200) {
                        val expensesCollection: Array<Expense> = response.body()!!.payload

                        for (expense: Expense in expensesCollection) {
                            when {
                                expense.currency.shortName.equals("EUR", true) -> {
                                    expensesEur += expense.amount
                                }
                                expense.currency.shortName.equals("USD", true) -> {
                                    expensesUsd += expense.amount
                                }
                                else -> {
                                    expensesPln += expense.amount
                                }
                            }
                        }
                        view?.expenses_eur_value?.text = expensesEur.toString()
                        view?.expenses_usd_value?.text = expensesUsd.toString()
                        view?.expenses_pln_value?.text = expensesPln.toString()
                    }
                }
            })
    }

    @SuppressLint("SimpleDateFormat")
    private fun getIncomes() {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        RetrofitClient(baseContext).instance.getIncomes(dateFormat.format(Date()))
            .enqueue(object : Callback<IncomesResponse> {
                override fun onFailure(call: Call<IncomesResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<IncomesResponse>,
                    response: Response<IncomesResponse>
                ) {
                    if (response.code() == 200) {
                        val incomesCollection: Array<Income> = response.body()!!.payload

                        for (income: Income in incomesCollection) {
                            when {
                                income.currency.shortName.equals("EUR", true) -> {
                                    incomesEur += income.amount
                                }
                                income.currency.shortName.equals("USD", true) -> {
                                    incomesUsd += income.amount
                                }
                                else -> {
                                    incomesPln += income.amount
                                }
                            }
                        }
                        view?.incomes_eur_value?.text = incomesEur.toString()
                        view?.incomes_usd_value?.text = incomesUsd.toString()
                        view?.incomes_pln_value?.text = incomesPln.toString()
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
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
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
        fun newInstance() = HomeFragment()
    }
}
