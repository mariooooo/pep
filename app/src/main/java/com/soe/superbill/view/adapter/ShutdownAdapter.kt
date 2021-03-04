package com.soe.superbill.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.florent37.expansionpanel.ExpansionLayout
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection
import com.soe.superbill.Globals

import com.soe.superbill.R
import com.soe.superbill.data.model.GeneralInformation
import kotlinx.android.synthetic.main.expansion_panel_recycler_cell.view.*

class ShutdownAdapter(c: Context, type: Int) : RecyclerView.Adapter<ShutdownAdapter.RecyclerHolder>() {
    private val list: MutableList<GeneralInformation.Shutdown> = ArrayList<GeneralInformation.Shutdown>()
    private val expansionsCollection = ExpansionLayoutCollection()
    private val context: Context = c
    private val type = type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        return RecyclerHolder.buildFor(parent)
    }

    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        holder.bind(list[position])
        val d: GeneralInformation.Shutdown = list[position]
        expansionsCollection.add(holder.expansionLayout)
        holder.scheduled_time_shutdown.text = d.begin


        holder.value_b.text = d.begin
        holder.value_e.text = d.end

        if (type == Globals.EMERGENCY_SHUTDOWN) {
            holder.label_e.text = context.getString(R.string.plan_avar)
            holder.ll_f_e.visibility = View.VISIBLE
            holder.value_f_e.text = d.end_fact
            holder.line_f_e.visibility = View.VISIBLE
        }
        holder.value_r.text = d.text
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setItems(items: List<GeneralInformation.Shutdown>?) {
        list.addAll(items!!)
        notifyDataSetChanged()
    }

    class RecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expansionLayout: ExpansionLayout = itemView.expansionLayout
        val scheduled_time_shutdown: TextView = itemView.scheduled_time_shutdown
        val value_b = itemView.value_b
        val label_e = itemView.label_e
        val value_e = itemView.value_e
        val ll_f_e = itemView.ll_f_e
        val value_f_e = itemView.value_f_e
        val line_f_e = itemView.line_f_e
        val value_r = itemView.value_r
        fun bind(`object`: Any?) {
            expansionLayout.collapse(false)
        }

        companion object {
            private val LAYOUT: Int = R.layout.expansion_panel_recycler_cell
            fun buildFor(viewGroup: ViewGroup): RecyclerHolder {
                return RecyclerHolder(LayoutInflater.from(viewGroup.context).inflate(LAYOUT, viewGroup, false))
            }
        }
    }

    init {
        expansionsCollection.openOnlyOne(true)
    }
}

