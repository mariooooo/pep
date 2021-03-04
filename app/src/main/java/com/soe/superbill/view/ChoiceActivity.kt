package com.soe.superbill.view


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.yarolegovich.lovelydialog.LovelyTextInputDialog
import kotlinx.android.synthetic.main.choice.*


class ChoiceActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice)
        initToolbar(getString(R.string.app_name),false)

        val isLogin:Boolean = pref.getBoolean(Globals.APP_PREFERENCES_IS_LOGIN, false)
        if(isLogin){
            finish()
            startActivity(Intent(this, ListAccountsActivity::class.java))
        }

        sign_in.setOnClickListener{
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
        sign_up.setOnClickListener{
            finish()
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}