package com.budget.tracker.fragments

import android.annotation.SuppressLint
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
import com.budget.tracker.requests.EditIncomeRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.edit_income_dialog.view.*
import kotlinx.android.synthetic.main.fragment_expences.view.*
import kotlinx.android.synthetic.main.new_income_dialog.view.*
import kotlinx.android.synthetic.main.new_income_dialog.view.dialogIncomeCancelButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

        view.fab.setOnClickListener {
            showDialog()
        }

        recyclerView = view.findViewById(R.id.recycler_view_incomes)
        adapterIncomes = IncomesRecyclerViewAdapter(
            baseContext,
            dataList,
            onClickListener = { income -> removeIncome(income) },
            onEditListener = { incomeId -> showEditDialog(incomeId) })
        recyclerView.layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapterIncomes

        getIncomes()

        return view
    }

    @SuppressLint("SimpleDateFormat")
    fun getIncomes() {
        dataList.clear()

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
                            if (income.categoryName.equals(categoryName, true)) {
                                dataList.add(income)
                            }
                        }
                    }
                    adapterIncomes.notifyDataSetChanged()
                }
            })
    }

    @SuppressLint("InflateParams")
    fun showDialog() {
        val newIncomeDialogView =
            LayoutInflater.from(baseContext).inflate(R.layout.new_income_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(newIncomeDialogView)
            .setTitle(getString(R.string.add_new_income))

        val mAlertDialog = mBuilder.show()

        newIncomeDialogView.dialogIncomeAddButton.setOnClickListener {

            val incomeValue = newIncomeDialogView.incomeValue.text.toString()
            val incomeCurrency = newIncomeDialogView.currencySpinner.selectedItemPosition + 1

            if (incomeValue.isEmpty()) {
                newIncomeDialogView.incomeValue.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            addNewIncome(incomeValue, incomeCurrency)
        }

        newIncomeDialogView.dialogIncomeCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    @SuppressLint("InflateParams")
    fun showEditDialog(incomeId: Int) {
        val editIncomeDialogView =
            LayoutInflater.from(baseContext).inflate(R.layout.edit_income_dialog, null)

        val mBuilder = AlertDialog.Builder(baseContext)
            .setView(editIncomeDialogView)
            .setTitle(getString(R.string.edit_income))

        val mAlertDialog = mBuilder.show()

        editIncomeDialogView.dialogIncomeEditButton.setOnClickListener {

            val incomeNewValue = editIncomeDialogView.incomeNewValue.text.toString()

            if (incomeNewValue.isEmpty()) {
                editIncomeDialogView.incomeNewValue.requestFocus()
                return@setOnClickListener
            }

            mAlertDialog.dismiss()
            editIncome(incomeId, incomeNewValue)
        }

        editIncomeDialogView.dialogIncomeCancelButton.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun addNewIncome(value: String, currencyId: Int) {
        RetrofitClient(baseContext).instance.addNewIncome(
            AddIncomeRequest(
                categoryId!!.toInt(),
                value.toFloat(),
                currencyId
            )
        ).enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.income_added), Toast.LENGTH_LONG).show()
                        dataList.clear()
                        getIncomes()
                    }
                }
            })
    }

    private fun editIncome(incomeId: Int, value: String) {
        RetrofitClient(baseContext).instance.editIncome(
            EditIncomeRequest(
                incomeId,
                value.toFloat()
            )
        )
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.income_updated), Toast.LENGTH_LONG)
                            .show()
                        dataList.clear()
                        getIncomes()
                    }
                }
            })
    }

    private fun removeIncome(income: Income) {
        RetrofitClient(baseContext).instance.removeIncome(income.id.toString())
            .enqueue(object : Callback<CommonResponse> {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<CommonResponse>,
                    response: Response<CommonResponse>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(baseContext, getString(R.string.income_deleted), Toast.LENGTH_LONG).show()
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
        fun newInstance(categoryName: String, categoryId: Int) =
            IncomesFragment().apply {
                arguments = Bundle().apply {
                    putString(CATEGORY_NAME, categoryName)
                    putInt(CATEGORY_ID, categoryId)
                }
            }
    }
}