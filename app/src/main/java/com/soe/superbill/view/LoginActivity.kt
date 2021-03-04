package com.soe.superbill.view

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.data.model.Error
import com.soe.superbill.data.model.Universal
import com.soe.superbill.mvvm.Status
import com.soe.superbill.mvvm.viewModel.LoginActivityViewModel
import com.soe.superbill.view.dialog.ForgotDialog
//import kotlinx.android.synthetic.main.dialog_forgot.*
import kotlinx.android.synthetic.main.login.*

class LoginActivity : BaseActivity() {

    private lateinit var loginActivityViewModel: LoginActivityViewModel
    val alert = ForgotDialog()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        initToolbar(getString(R.string.app_name), true)

        val remember_me:Boolean = pref.getBoolean(Globals.APP_PREFERENCES_REMEMBER_ME, false)
        if(remember_me) {
            input_login.setText(pref.getString(Globals.APP_PREFERENCES_LOGIN, ""))
            input_password.setText(pref.getString(Globals.APP_PREFERENCES_PASSWORD, ""))
            switchCompat.isChecked = true
        }

        loginActivityViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        observeGetPosts()

        btn_login.setOnClickListener {
            loginActivityViewModel.login(input_login.text.toString(), input_password.text.toString())
        }

        switchCompat.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val editor = pref.edit()
            editor.putBoolean(Globals.APP_PREFERENCES_REMEMBER_ME, isChecked)
            editor.apply()
        })

        observeRecovery()
        forgot_password.setOnClickListener {
            alert.showDialog(this)
        }


    }

    private fun observeGetPosts() {
        loginActivityViewModel.simpleLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> viewOneLoading()
                Status.SUCCESS -> viewOneSuccess(it.data)
                Status.ERROR -> viewOneError(it.error)
            }
        })
    }

    private fun observeRecovery() {
        loginActivityViewModel.restoreLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> viewOneLoading()
                Status.SUCCESS -> {
                    loadingDialog.dialog.dismiss()
                    Toast.makeText(applicationContext, "${it.data!!.message}", Toast.LENGTH_LONG).show()
                    if(it.data.confirmation == "telephone"){
                        alert.close()
                        startActivity(Intent(this, ConfirmRecoveryActivity::class.java).apply {
                            putExtra(Globals.ACTION, Globals.ACTION_RECOVERY)
                            putExtra(Globals.ARGUMENT_PHONE_NUMBER, alert.dialog!!.findViewById<EditText>(R.id.input_login_forgot).text.toString())
                        })
                    }
                    else{
                        alert.close()
                    }
                }
                Status.ERROR -> viewOneError(it.error)
            }
        })
    }

    private fun viewOneLoading() {
        // Пошла загрузка, меняем состояние вьюх
//        Toast.makeText(applicationContext, "loading", Toast.LENGTH_LONG).show()
//        val load = LoadingDialog.show(supportFragmentManager)
        loadingDialog.show(this)
    }

    private fun viewOneSuccess(data: Universal?) {
        loadingDialog.dialog.dismiss()
        val res: Universal? = data
        res?.let {
            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_LONG).show()
            val editor = pref.edit()
            editor.putString(Globals.APP_PREFERENCES_LOGIN, input_login.text.toString())
            editor.putString(Globals.APP_PREFERENCES_PASSWORD, input_password.text.toString())
            editor.putBoolean(Globals.APP_PREFERENCES_IS_LOGIN, true)
            editor.apply()
            finish()
            startActivity(Intent(this, ListAccountsActivity::class.java))
        }
    }

    private fun viewOneError(error: Error?) {
        loadingDialog.dialog.dismiss()
        // Показываем ошибку
        error?.let {
            Toast.makeText(applicationContext, error.err, Toast.LENGTH_LONG).show()
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

    fun onActionButtonClick(v: View) {
        when (v.id) {
            R.id.restore -> {
                println("all right")
                val input_login_forgot = alert.dialog!!.findViewById<EditText>(R.id.input_login_forgot)
                if (input_login_forgot.text.toString().isEmpty()) {
                    /*val builder = AlertDialog.Builder(this)
                    builder.setTitle("title")
                    builder.setMessage("message")
                            .setPositiveButton("OK") { dialog, id ->
                            }
                    val alert = builder.create()
                    alert.show()*/
                    Toast.makeText(this, "Поле для email або номера телефона порожнє", Toast.LENGTH_LONG).show()
                } else {
                    loginActivityViewModel.restore(input_login_forgot.text.toString())
                }
            }
            R.id.cancel_forgot -> {
                alert.close()
            }
        }
    }
}