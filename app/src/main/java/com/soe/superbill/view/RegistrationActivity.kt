package com.soe.superbill.view


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.mvvm.viewModel.RegistrationActivityViewModel
import com.soe.superbill.mvvm.Status
import com.soe.superbill.data.model.Error
import com.soe.superbill.data.model.Registration
import com.soe.superbill.data.model.Universal
import com.soe.superbill.data.model.Wrapper
import kotlinx.android.synthetic.main.registration.*

class RegistrationActivity : BaseActivity() {

    private lateinit var registrationActivityViewModel: RegistrationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        initToolbar(getString(R.string.toolbar_title_registration), true)

        val factory = layoutInflater

        val t: View = factory.inflate(R.layout.toolbar, null)

        val tool = t.findViewById(R.id.toolbar) as Toolbar

        registrationActivityViewModel = ViewModelProvider(this).get(RegistrationActivityViewModel::class.java)
        observeGetPosts()

        btn_save.setOnClickListener {
            if (!checkBox.isChecked) {
                Toast.makeText(this, getString(R.string.error_agreement), Toast.LENGTH_LONG).show()
            } else {
                val formatedMobile: String = input_mobile.text!!.substring(3).replace("[()-]".toRegex(), "")
                val reg = Registration(input_name.text.toString(),
                        input_surname.text.toString(),
                        input_login.text.toString(),
                        input_password.text.toString(),
                        input_password_repeat.text.toString(),
                        formatedMobile
                )
                registrationActivityViewModel.registration(Globals.convertToMap(reg))
            }
        }

        input_mobile.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event -> false })

        read_more.setOnClickListener { details.cycleTextViewExpansion() }
    }

    private fun observeGetPosts() {
        registrationActivityViewModel.simpleLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> viewOneLoading()
                Status.SUCCESS -> {
                    Toast.makeText(applicationContext, "${it.data!!.message}", Toast.LENGTH_LONG).show()
                    if (it.data.confirmation == "phone") {
                        finish()
                        startActivity(Intent(this, ConfirmRecoveryActivity::class.java).apply {
                            putExtra(Globals.ACTION, Globals.ACTION_CONFIRM)
                            if(input_login.text.isEmpty()) {
                                putExtra(Globals.ARGUMENT_PHONE_NUMBER, input_mobile.text!!.substring(3).replace("[()-]".toRegex(), ""))
                            }
                            else{
                                putExtra(Globals.ARGUMENT_PHONE_NUMBER, input_login.text!!.toString())
                            }
                        })
                    } else {
                        finish()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
                Status.ERROR -> viewOneError(it.error)
            }
        })
    }


    private fun buttonOneClickListener() {
    }


    private fun viewOneLoading() {
        loadingDialog.show(this)
    }

    private fun viewOneSuccess(data: Universal?) {
        loadingDialog.dialog.dismiss()
        val res: Universal? = data
        res?.let {
            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun viewOneError(error: Error?) {
        loadingDialog.dialog.dismiss()
        error?.let {
            Toast.makeText(applicationContext, error.err, Toast.LENGTH_LONG).show()
        }
    }

    private fun viewTwoLoading() {}

    private fun viewTwoSuccess(data: Wrapper?) {}

    private fun viewTwoError(error: Wrapper?) {
        error?.let {
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            finish()
            startActivity(Intent(this, ChoiceActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this, ChoiceActivity::class.java))
    }
}