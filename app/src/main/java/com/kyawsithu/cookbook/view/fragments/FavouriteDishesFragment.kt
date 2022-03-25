package com.kyawsithu.cookbook.view.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.databinding.FragmentFavouriteDishesBinding
import com.kyawsithu.cookbook.view.adapters.CookBookAdapter
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory
import com.kyawsithu.cookbook.viewmodel.DashboardViewModel

class FavouriteDishesFragment : Fragment() {

    private var binding: FragmentFavouriteDishesBinding? = null

    private val mCookBookViewModel: CookBookViewModel by viewModels {
        CookBookViewModelFactory((requireActivity().application as CookBookApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteDishesBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        mCookBookViewModel.favouriteDishes.observe(viewLifecycleOwner){
            dishes ->
            dishes.let{

                binding!!.rvFavoriteDishesList.layoutManager =
                        GridLayoutManager(requireActivity(),2)

                val adapter = CookBookAdapter(this)
                binding!!.rvFavoriteDishesList.adapter = adapter

                if(it.isNotEmpty()){
                    binding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    binding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                    adapter.dishesList(it)
                }else{
                    binding!!.rvFavoriteDishesList.visibility = View.GONE
                    binding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}