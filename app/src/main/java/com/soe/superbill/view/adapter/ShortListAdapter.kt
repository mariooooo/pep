package com.soe.superbill.view.adapter

//import com.soe.superbill.data.model.ShortListItem
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.data.model.ShortList
import com.soe.superbill.view.AccountDetailActivity
import com.soe.superbill.view.AddActivity
import com.soe.superbill.view.ListAccountsActivity
import kotlinx.android.synthetic.main.account_list_button.view.*
import kotlinx.android.synthetic.main.account_list_item.view.*


class ShortListAdapter(var context: Context, var shortList: ArrayList<ShortList.Item>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_FOOTER = 0
    private val TYPE_ITEM = 1

    fun updateShortList(newShortList: List<ShortList.Item>) {
        shortList.clear()
        shortList.addAll(newShortList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.account_list_item, parent, false)
                ItemViewHolder(itemView)
            }
            TYPE_FOOTER -> {
                val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.account_list_button, parent, false)
                FooterViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (position == shortList.size) {
            return TYPE_FOOTER
        }
        return TYPE_ITEM
    }


    override fun getItemCount(): Int {
        return shortList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            holder.btn_add.setOnClickListener { view ->
                (context as ListAccountsActivity).startActivityForResult(Intent(context, AddActivity::class.java), Globals.ADD_ACCOUNT)
            }
        } else if (holder is ItemViewHolder) {
            holder.bind(shortList[position])

            holder.itemView.setOnClickListener { view ->
                (context as ListAccountsActivity).startActivityForResult(Intent(context, AccountDetailActivity::class.java).apply {
                    putExtra(Globals.ARGUMENT_A, shortList[position].a)
                }, Globals.GENERAL_INFO)
            }
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val item = view
        private val ls = view.ls
        private val fio = view.fio
        private val address = view.address
        fun bind(item: ShortList.Item) {
            ls.text = item.ls
            fio.text = item.fio
            address.text = item.address
        }
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val btn_add = view.btn_add
    }
}