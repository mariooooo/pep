package com.soe.superbill.view.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView
import com.soe.superbill.App
import com.soe.superbill.R
import com.soe.superbill.data.model.DataInfo

class AddSpinnerAdapter(
        powerSpinnerView: PowerSpinnerView
) : RecyclerView.Adapter<AddSpinnerAdapter.AddSpinnerViewHolder>(),
        PowerSpinnerInterface<DataInfo> {

    private val spinnerItems: MutableList<DataInfo> = arrayListOf()

    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<DataInfo>? = null


    override fun onBindViewHolder(holder: AddSpinnerViewHolder, position: Int) {
        val item = this.spinnerItems[position]
        val appCompatTextView = holder.itemView.findViewById(R.id.item_default_text) as AppCompatTextView
        appCompatTextView.apply {
            text = item.name
            maxLines = Int.MAX_VALUE
            typeface = spinnerView.typeface
            gravity = spinnerView.gravity
            setTextColor(App.context!!.resources.getColor(R.color.black))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, spinnerView.textSize)
            setPadding(spinnerView.paddingLeft, spinnerView.paddingTop, spinnerView.paddingRight,
                    spinnerView.paddingBottom)
            setOnClickListener { notifyItemSelected(position) }
        }
    }

    override fun notifyItemSelected(index: Int) {
        this.spinnerView.notifyItemSelected(index, this.spinnerItems[index].name)
        this.onSpinnerItemSelectedListener?.onItemSelected(index, this.spinnerItems[index])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddSpinnerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddSpinnerViewHolder(layoutInflater.inflate(R.layout.item_default, parent, false))
    }

    override fun getItemCount(): Int = this.spinnerItems.size

    override fun setItems(itemList: List<DataInfo>) {
        this.spinnerItems.clear()
        this.spinnerItems.addAll(itemList)
        notifyDataSetChanged()
    }

    class AddSpinnerViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
