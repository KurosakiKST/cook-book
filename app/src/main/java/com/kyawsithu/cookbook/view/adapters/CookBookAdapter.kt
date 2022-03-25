package com.kyawsithu.cookbook.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyawsithu.cookbook.databinding.ItemDishLayoutBinding
import com.kyawsithu.cookbook.model.entities.CookBook
import com.kyawsithu.cookbook.view.fragments.AllDishesFragment

class CookBookAdapter(private val fragment : Fragment) : RecyclerView.Adapter<CookBookAdapter.ViewHolder>()
{

    private var dishes : List<CookBook> = listOf()

    class ViewHolder(view : ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root)
    {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder
    {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int)
    {
        val dish = dishes[position]
        Glide.with(fragment)
                .load(dish.image)
                .into(holder.ivDishImage)
        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener {
            if(fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
        }
    }

    override fun getItemCount() : Int
    {
        return dishes.size
    }

    fun dishesList(list: List<CookBook>){
        dishes = list
        notifyDataSetChanged()
    }

}