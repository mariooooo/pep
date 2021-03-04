package com.soe.superbill.view

import android.R
import android.content.SharedPreferences
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.soe.superbill.Globals
import kotlinx.android.synthetic.main.toolbar.view.*

abstract class BaseActivity : AppCompatActivity() {

    val loadingDialog = LoadingDialog()
    lateinit var a : String
    lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences(Globals.APP_PREFERENCES, MODE_PRIVATE)
    }

    open fun initToolbar(title: String, arrow: Boolean = false, menu: Boolean = false) {
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(com.soe.superbill.R.layout.toolbar)
        supportActionBar!!.elevation = 0f
        supportActionBar!!.customView.title.text = title
        when(arrow){
            true -> {
                val array: TypedArray = theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
                val actionBarSize = array.getDimensionPixelSize(0, 0)
                array.recycle()
                supportActionBar!!.customView.title.setPadding(0,0, actionBarSize,0)
                supportActionBar!!.setDisplayShowHomeEnabled(true)
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}