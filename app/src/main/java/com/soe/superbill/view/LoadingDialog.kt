package com.soe.superbill.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.soe.superbill.R

class LoadingDialog{
    lateinit var dialog: Dialog

    fun show(context: Context): Dialog {
        return show(context, null)
    }

    fun show(context: Context, title:CharSequence?): Dialog {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.loading, null)
        dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }
}