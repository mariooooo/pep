package com.soe.superbill.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.data.model.Add
import com.soe.superbill.data.model.AddInfo
import com.soe.superbill.mvvm.Status
import com.soe.superbill.mvvm.viewModel.AddActivityViewModel
import com.soe.superbill.view.adapter.AddSpinnerAdapter
import kotlinx.android.synthetic.main.add_account.*
import kotlinx.android.synthetic.main.add_account.load

class AddActivity : BaseActivity(){

    private lateinit var addActivityViewModel: AddActivityViewModel
    lateinit var regionsList:List<AddInfo.Region>
    var regionId:String = ""
    var osrId:String = ""
    var providerId:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_account)
        initToolbar(getString(R.string.toolbar_title_add),true)

        addActivityViewModel = ViewModelProvider(this).get(AddActivityViewModel::class.java)
        observeGetRegions()
        addActivityViewModel.getRegions()
        observeAdd()

        btn_enter.setOnClickListener {
            val viber = if (checkBox_viber.isChecked) {
                "t"
            } else {
                "f"
            }
            val telegram = if (checkBox_telegram.isChecked) {
                "t"
            } else {
                "f"
            }
            val email = if (checkBox_email.isChecked) {
                "t"
            } else {
                "f"
            }

            val add = Add(input_ls.text.toString(),
                    input_eic.text.toString(),
                    regionId,
                    viber,
                    telegram,
                    "f",
                    email,
                    osrId,
                    providerId
            )
            addActivityViewModel.add(Globals.convertToMap(add))
        }

    }

    private fun observeGetRegions() {
        addActivityViewModel.regionsLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    println("Loading...")
                    load.visibility = View.VISIBLE
                    scroll.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    load.visibility = View.GONE
                    scroll.visibility = View.VISIBLE
                    regionsList = it.data!!.res
                    spinner.setSpinnerAdapter(AddSpinnerAdapter(spinner))
                    spinner.setOnSpinnerItemSelectedListener<AddInfo.Region> { index, text->
                        regionId = text.id.toString()
                    }
                    spinner.setItems(regionsList)

                    spinner_osr.setSpinnerAdapter(AddSpinnerAdapter(spinner_osr))
                    spinner_osr.setOnSpinnerItemSelectedListener<AddInfo.Distribution>{ index, text->
                        osrId = text.id.toString()
                    }
                    spinner_osr.setItems(it.data!!.list_co)
                    spinner_osr.selectItemByIndex(it.data!!.list_co.indexOfFirst { it.id == 1 })

                    spinner_provider.setSpinnerAdapter(AddSpinnerAdapter(spinner_provider))
                    spinner_provider.setOnSpinnerItemSelectedListener<AddInfo.Provider>{index, text->
                        providerId = text.id.toString()
                    }
                    spinner_provider.setItems(it.data!!.list_cp)
                    spinner_provider.selectItemByIndex(it.data!!.list_cp.indexOfFirst { it.id == 2 })
                }
                Status.ERROR -> {
                    load.visibility = View.GONE
                    scroll.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, it.error!!.err, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun observeAdd() {
        addActivityViewModel.addLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    println("Loading...")
                    loadingDialog.show(this)
                }
                Status.SUCCESS -> {
                    setResult(Globals.ADD_ACCOUNT_SUCCESS)
                    finish()
                    Toast.makeText(applicationContext, it.data!!.message, Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    loadingDialog.dialog.dismiss()
                    Toast.makeText(applicationContext, it.error!!.err, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}