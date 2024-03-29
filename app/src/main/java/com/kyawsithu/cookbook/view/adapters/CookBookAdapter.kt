package com.kyawsithu.cookbook.view.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.ItemDishLayoutBinding
import com.kyawsithu.cookbook.model.entities.CookBook
import com.kyawsithu.cookbook.utils.Constants
import com.kyawsithu.cookbook.view.activities.AddUpdateDishesActivity
import com.kyawsithu.cookbook.view.fragments.AllDishesFragment
import com.kyawsithu.cookbook.view.fragments.FavouriteDishesFragment

class CookBookAdapter(private val fragment : Fragment) : RecyclerView.Adapter<CookBookAdapter.ViewHolder>()
{

    private var dishes : List<CookBook> = listOf()

    class ViewHolder(view : ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root)
    {
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
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
            if(fragment is FavouriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }
        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if(it.itemId == R.id.action_edit_dish){
                    val intent = Intent(fragment.requireActivity(), AddUpdateDishesActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)

                }else if(it.itemId == R.id.action_delete_dish){
                    if(fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                }
                true
            }
            popup.show()
        }

        if(fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        }else if(fragment is FavouriteDishesFragment){
            holder.ibMore.visibility = View.GONE
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