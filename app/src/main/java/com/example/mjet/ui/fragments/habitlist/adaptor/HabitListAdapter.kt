package com.example.mjet.ui.fragments.habitlist.adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mjet.R
import com.example.mjet.data.models.Habit
import com.example.mjet.ui.fragments.habitlist.HabitListDirections
import com.example.mjet.utils.Calculations
import com.maltaisn.icondialog.pack.IconPack
import kotlinx.android.synthetic.main.recycler_habit_item.view.*

class HabitListAdapter(val iconDialogIconPack: IconPack?) :RecyclerView.Adapter<HabitListAdapter.MyViewHolder>(){
    var habilist= emptyList<Habit>()
    val TAG="HabitListAdapter"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HabitListAdapter.MyViewHolder {
       return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_habit_item,parent,false ))
    }

   inner class  MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        init {
            itemView.cv_cardView.setOnClickListener {
                val position=adapterPosition
                Log.d(TAG,"Item clicked at $position")
                Log.d(TAG,"ID ${habilist[position].id}")
                val action=HabitListDirections.actionHabitListToUpdateHabitItem(habilist[position])
                itemView.findNavController().navigate(action)
            }
        }

    }

    override fun onBindViewHolder(holder: HabitListAdapter.MyViewHolder, position: Int) {


        val currentHabit=habilist[position]
        holder.itemView.iv_habit_icon.setImageResource(currentHabit.imageId)
        holder.itemView.tv_item_description.text = currentHabit.habit_description
        holder.itemView.tv_timeElapsed.text =
            Calculations.calculateTimeBetweenDates(currentHabit.habit_startTime)
        holder.itemView.tv_item_createdTimeStamp.text = "Since: ${currentHabit.habit_startTime}"
        holder.itemView.tv_item_title.text = "${currentHabit.habit_title}"
        currentHabit.icon.let {
            val icon = it?.let { it1 -> this.iconDialogIconPack?.getIcon(it1) }
            val drawable= icon?.drawable
            holder.itemView.iv_habit_new_icon.setImageDrawable(drawable)
        }

    }
    fun setData(habits: List<Habit>) {
        this.habilist = habits
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
       return habilist.size
    }
}