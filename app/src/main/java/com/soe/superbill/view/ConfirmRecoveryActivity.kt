package com.soe.superbill.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.mvvm.Status
import com.soe.superbill.mvvm.viewModel.ConfirmRecoveryViewModel
import kotlinx.android.synthetic.main.confirm_recovery.*

class ConfirmRecoveryActivity : BaseActivity() {
    private lateinit var confirmRecoveryViewModel: ConfirmRecoveryViewModel
    lateinit var action: String
    lateinit var phone_number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_recovery)

        action = intent.getStringExtra(Globals.ACTION)!!
        phone_number = intent.getStringExtra(Globals.ARGUMENT_PHONE_NUMBER)!!

        if (action == Globals.ACTION_CONFIRM) {
            initToolbar(getString(R.string.activity_confirm_title), true)
            confirm_change_instruction.text = getString(R.string.confirmation_instruction)
            btnConfirmationChange.text = getString(R.string.btn_confirm)
            wrapperNewPassword.visibility = View.GONE
            wrapperConfirmPassword.visibility = View.GONE
        } else if (action == Globals.ACTION_RECOVERY) {
            initToolbar(getString(R.string.activity_change_title), true)
            confirm_change_instruction.text = getString(R.string.change_instruction)
            btnConfirmationChange.text = getString(R.string.btn_change)
        }

        confirmRecoveryViewModel = ViewModelProvider(this).get(ConfirmRecoveryViewModel::class.java)
        observeConfirmOrRecovery()

        btnConfirmationChange.setOnClickListener {
//            TODO(ADD VALIDATION)
            if (verification_code.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.verification_code_empty), Toast.LENGTH_LONG).show()
            } else if (verification_code.text.toString().length < 8) {
                Toast.makeText(this, getString(R.string.verification_code_min), Toast.LENGTH_LONG).show()
            } else if (action == Globals.ACTION_RECOVERY && new_password.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.new_password_empty), Toast.LENGTH_LONG).show()
            } else if (action == Globals.ACTION_RECOVERY && repeat_new_password.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.repeat_new_password_empty), Toast.LENGTH_LONG).show()
            } else {
                confirmRecoveryViewModel.confirmOrRecovery(phone_number,
                        verification_code.text.toString(),
                        new_password.text.toString(),
                        repeat_new_password.text.toString()
                )
            }
        }
    }

    private fun observeConfirmOrRecovery() {
        confirmRecoveryViewModel.confirmOrRecoveryLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    loadingDialog.show(this)
                }
                Status.SUCCESS -> {
                    loadingDialog.dialog.dismiss()
                    Toast.makeText(applicationContext, it.data!!.message, Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                Status.ERROR -> {
                    loadingDialog.dialog.dismiss()
                    Toast.makeText(applicationContext, it.error!!.err, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}