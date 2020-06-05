package com.budget.tracker.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
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


class HomeFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var baseContext: Context
    private var incomes_eur: Double = 0.0
    private var incomes_usd: Double = 0.0
    private var incomes_pln: Double = 0.0
    private var expenses_eur: Double = 0.0
    private var expenses_usd: Double = 0.0
    private var expenses_pln: Double = 0.0


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


        getExpenses();
        getIncomes();

        return view
    }

    private fun getExpenses() {
        RetrofitClient(baseContext).instance.getExpenses()
            .enqueue(object : Callback<ExpensesResponse> {
                override fun onFailure(call: Call<ExpensesResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ExpensesResponse>, response: Response<ExpensesResponse>) {
                    if (response.code() == 200) {
                        val expensesCollection: Array<Expense> = response.body()!!.payload

                        for (expense: Expense in expensesCollection) {
                            if (expense.currency.shortName.equals("EUR", true)) {
                                expenses_eur += expense.amount

                            }else if(expense.currency.shortName.equals("USD", true))
                            {
                                expenses_usd += expense.amount
                            }else{
                                expenses_pln += expense.amount
                            }
                        }
                        view?.expenses_eur_value?.text = expenses_eur.toString()
                        view?.expenses_usd_value?.text = expenses_usd.toString()
                        view?.expenses_pln_value?.text = expenses_pln.toString()
                    }
                }
            })
    }

  private  fun getIncomes() {
        RetrofitClient(baseContext).instance.getIncomes()
            .enqueue(object : Callback<IncomesResponse> {
                override fun onFailure(call: Call<IncomesResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<IncomesResponse>, response: Response<IncomesResponse>) {
                    if (response.code() == 200) {
                        val incomesCollection: Array<Income> = response.body()!!.payload

                        for (income: Income in incomesCollection) {
                            if (income.currency.shortName.equals("EUR", true)) {
                                incomes_eur += income.amount
                            }else if(income.currency.shortName.equals("USD", true))
                            {
                                incomes_usd += income.amount
                            }else{
                                incomes_pln += income.amount
                            }
                        }
                        view?.incomes_eur_value?.text = incomes_eur.toString()
                        view?.incomes_usd_value?.text = incomes_usd.toString()
                        view?.incomes_pln_value?.text = incomes_pln.toString()
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
        fun newInstance() = HomeFragment()
    }
}
