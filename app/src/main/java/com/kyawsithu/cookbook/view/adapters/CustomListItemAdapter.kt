package com.kyawsithu.cookbook.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kyawsithu.cookbook.databinding.ItemCustomListBinding
import com.kyawsithu.cookbook.view.activities.AddUpdateDishesActivity

class CustomListItemAdapter(
        private val activity : Activity,
        private val listItems : List<String>,
        private val selection : String)
    : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>()
{
    class ViewHolder(view : ItemCustomListBinding) : RecyclerView.ViewHolder(view.root)
    {
        val tvText = view.tvText
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder
    {
        val binding : ItemCustomListBinding = ItemCustomListBinding
                .inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int)
    {
        val item = listItems[position]
        holder.tvText.text = item

        holder.itemView.setOnClickListener{
            if(activity is AddUpdateDishesActivity){
                activity.selectedListItem(item, selection)
            }
        }

    }

    override fun getItemCount() : Int
    {
        return listItems.size
    }

}