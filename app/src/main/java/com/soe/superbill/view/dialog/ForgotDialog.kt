package com.soe.superbill.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.soe.superbill.R

class ForgotDialog {
    var dialog: Dialog? = null
    fun showDialog(context: Context) {
        dialog = Dialog(context)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.dialog_forgot)
        val input_login_forgot = dialog!!.findViewById<EditText>(R.id.input_login_forgot)
        input_login_forgot.setTypeface(ResourcesCompat.getFont(context, R.font.gothic))
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.setLayout(width, height)
        val title_dialog = dialog!!.findViewById<TextView>(R.id.title_dialog)
        title_dialog.setTypeface(ResourcesCompat.getFont(context, R.font.gothic))
        val text_dialog = dialog!!.findViewById<TextView>(R.id.text_dialog)
        text_dialog.setTypeface(ResourcesCompat.getFont(context, R.font.gothic))
        dialog!!.show()
    }

    fun close() {
        dialog!!.dismiss()
    }
}