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
import com.budget.tracker.adapters.IncomesRecyclerViewAdapter
import com.budget.tracker.api.CommonResponse
import com.budget.tracker.api.IncomesResponse
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.models.Income
import com.budget.tracker.requests.AddIncomeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_expences.view.*
import kotlinx.android.synthetic.main.new_expense_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val CATEGORY_NAME = "CATEGORY_NAME"
private const val CATEGORY_ID = "CATEGORY_ID"

class IncomesFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var categoryName: String? = null
    private var categoryId: Int? = null
    private var dataList: ArrayList<Income> = ArrayList()
    private lateinit var baseContext: Context
    private lateinit var adapterIncomes: IncomesRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryName = it.getString(CATEGORY_NAME)
            categoryId = it.getInt(CATEGORY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_incomes, container, false)

        activity!!.nav_view.setCheckedItem(R.id.sidebar_incomes)

        view.fab.setOnClickListener { view ->
            showDialog()
        }

        recyclerView = view.findViewById(R.id.recycler_view_incomes)
        adapterIncomes = IncomesRecyclerViewAdapter(
            baseContext,
            dataList,
            onClickListener = { income -> removeIncome(income) })
        recyclerView.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = adapterIncomes

        getIncomes()

        return view
    }

    fun getIncomes() {
        dataList.clear()

        RetrofitClient(baseContext).instance.getIncomes()
            .enqueue(object : Callback<IncomesResponse> {
                override fun onFailure(call: Call<IncomesResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<IncomesResponse>, response: Response<IncomesResponse>) {
                    if (response.code() == 200) {
                        val incomesCollection: Array<Income> = response.body()!!.payload

                        for (income: Income in incomesCollection) {
                            if (income.categoryName.equals(categoryName, true)) {
                                dataList.add(income)
                            }
                        }
                    }
                    adapterIncomes.notifyDataSetChanged()
                }
            })
    }

    fun showDialog() {
        val newIncomeDialogView = LayoutInflater.from(baseContext).inflate(R.layout.new_income_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(newIncomeDialogView)
            .setTitle("Nowy przychód")

        val  mAlertDialog = mBuilder.show()

        newIncomeDialogView.dialogIncomeAddButton.setOnClickListener {

            val incomeValue = newIncomeDialogView.incomeValue.text.toString()
            val incomeCurrency = newIncomeDialogView.incomeCurrency.text.toString()

            if (incomeValue.isEmpty()) {
                newIncomeDialogView.incomeValue.requestFocus()
                return@setOnClickListener
            }

            if (incomeCurrency.isEmpty()) {
                newIncomeDialogView.incomeCurrency.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            addNewIncome(incomeValue, incomeCurrency)
        }

        newIncomeDialogView.dialogIncomeCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    fun addNewIncome(value: String, currency: String) {
        var currencyCode = 1

        if(currency.equals("USD", true)) {
            currencyCode = 2
        }

        RetrofitClient(baseContext).instance.addNewIncome(AddIncomeRequest(categoryId!!.toInt(), value.toInt(), currencyCode))
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, "Dodano przychód", Toast.LENGTH_LONG).show()
                        dataList.clear()
                        getIncomes()
                    }
                }
            })
    }

    fun removeIncome(income: Income) {
        RetrofitClient(baseContext).instance.removeExpense(income.id.toString())
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, "Usunięto przychód", Toast.LENGTH_LONG).show()
                        dataList.clear()
                        getIncomes()
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
        fun newInstance(categoryName: String, categoryId: Int) =
            IncomesFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_NAME, categoryName)
                    putInt(CATEGORY_ID, categoryId)
                }
            }
    }
}