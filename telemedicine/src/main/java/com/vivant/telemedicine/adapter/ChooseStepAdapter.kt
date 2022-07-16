package com.vivant.telemedicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.vivant.telemedicine.R
import com.vivant.telemedicine.common.Utilities
import com.vivant.telemedicine.databinding.ItemChooseStepBinding
import com.vivant.telemedicine.model.ChooseStepModel

class ChooseStepAdapter(val context: Context,val listener: OnStepClickListener) : RecyclerView.Adapter<ChooseStepAdapter.ChooseStepHolder>() {

    private var selectedPos = 0
    private var stepsList: MutableList<ChooseStepModel> = mutableListOf()

    override fun getItemCount(): Int = stepsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ChooseStepHolder =
        ChooseStepHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_choose_step,parent,false))

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ChooseStepHolder, position: Int) {
        val step = stepsList[position]

        holder.imgStep.setImageResource(step.imgStep)
        holder.txtStep.text = step.titleStep
/*        if ( step.isSelected ) {
            holder.layoutStep.setBackgroundColor(ContextCompat.getColor(context, R.color.inactive))
        } else {
            holder.layoutStep.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
        }*/

        if (selectedPos >= position) {
            listener.onStepSelection(position, "")
            holder.txtStep.setTextColor(ContextCompat.getColor(context,R.color.vivant_gunmetal))
            ImageViewCompat.setImageTintList(holder.imgArrow,ColorStateList.valueOf(ContextCompat.getColor(context,R.color.vivant_gunmetal)))
            holder.imgStep.setImageResource(step.imgStep)
        } else {
            holder.txtStep.setTextColor(ContextCompat.getColor(context,R.color.vivantInactive))
            ImageViewCompat.setImageTintList(holder.imgArrow,ColorStateList.valueOf(ContextCompat.getColor(context,R.color.vivantInactive)))
            holder.imgStep.setImageResource(step.imgStepDisable)
        }

        if ( stepsList.size > 0 && position == stepsList.size-1 ) {
            holder.imgArrow.visibility = View.GONE
        } else {
            holder.imgArrow.visibility = View.VISIBLE
        }

/*        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPos)
            selectedPos = position
            //notifyItemChanged(selectedPos)
            notifyDataSetChanged()
        }*/
    }

    fun updateList( list: MutableList<ChooseStepModel> ) {
        Utilities.printData("",list,true)
        stepsList.clear()
        stepsList.addAll(list)
        notifyDataSetChanged()
    }

    fun updatePosition( pos : Int ) {
        selectedPos = pos
        notifyDataSetChanged()
    }

    interface OnStepClickListener {
        fun onStepSelection(position: Int, tag: String)
    }

    class ChooseStepHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemChooseStepBinding.bind(view)
        val layoutStep = binding.layoutStep
        val imgArrow = binding.imgArrow
        val imgStep = binding.imgStep
        val txtStep = binding.txtStep

    }

}