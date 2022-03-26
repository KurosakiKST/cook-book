package com.kyawsithu.cookbook.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kyawsithu.cookbook.databinding.FragmentRandomDishBinding
import com.kyawsithu.cookbook.viewmodel.NotificationsViewModel
import com.kyawsithu.cookbook.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment()
{

    private var binding : FragmentRandomDishBinding? = null

    private lateinit var mRandomDishViewModel : RandomDishViewModel

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View
    {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding !!.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomDishViewModelObserver()
    }

    private fun randomDishViewModelObserver()
    {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner
                                                       ) { randomDishResponse ->
            randomDishResponse?.let {
                Log.i("Random Dish", "$randomDishResponse.recipes[0]")

            }
        }
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner
                                                           ) { dataError ->
            dataError?.let {
                Log.i("Random Dish Error", "$dataError")
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner) { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random dish loading", "$loadRandomDish")
            }
        }

    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding = null
    }
}