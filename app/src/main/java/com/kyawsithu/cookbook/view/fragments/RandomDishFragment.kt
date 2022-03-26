package com.kyawsithu.cookbook.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.databinding.FragmentRandomDishBinding
import com.kyawsithu.cookbook.model.entities.CookBook
import com.kyawsithu.cookbook.model.entities.RandomDish
import com.kyawsithu.cookbook.utils.Constants
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory
import com.kyawsithu.cookbook.viewmodel.NotificationsViewModel
import com.kyawsithu.cookbook.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment()
{

    private var binding : FragmentRandomDishBinding? = null

    private lateinit var mRandomDishViewModel : RandomDishViewModel

    private var mProgressDialog: Dialog? = null

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View
    {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding !!.root
    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideCustomProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromAPI()

        randomDishViewModelObserver()

        binding !!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromAPI()
        }
    }

    private fun randomDishViewModelObserver()
    {
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner
                                                       ) { randomDishResponse ->
            randomDishResponse?.let {
                Log.i("Random Dish", "$randomDishResponse.recipes[0]")

                if(binding!!.srlRandomDish.isRefreshing){
                    binding!!.srlRandomDish.isRefreshing = false
                }

                setRandomDishResponseInUI(randomDishResponse.recipes[0])
            }
        }
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner
                                                           ) { dataError ->
            dataError?.let {
                Log.e("Random Dish Error", "$dataError")

                if(binding!!.srlRandomDish.isRefreshing){
                    binding!!.srlRandomDish.isRefreshing = false
                }
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner) { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random dish loading", "$loadRandomDish")

                if(loadRandomDish && !binding!!.srlRandomDish.isRefreshing){
                    showCustomProgressDialog()
                }else{
                    hideCustomProgressDialog()
                }
            }
        }

    }

    private fun setRandomDishResponseInUI(recipe : RandomDish.Recipe)
    {
        Glide.with(requireActivity())
                .load(recipe.image)
                .centerCrop()
                .into(binding !!.ivDishImage)

        binding !!.tvTitle.text = recipe.title

        var dishType : String = "Other"
        if (recipe.dishTypes.isNotEmpty())
        {
            dishType = recipe.dishTypes[0]
            binding !!.tvType.text = dishType
        }

        binding !!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients)
        {
            if (ingredients.isEmpty())
            {
                ingredients = value.original
            }
            else
            {
                ingredients = ingredients + ", \n" + value.original
            }
        }
        binding !!.tvIngredients.text = ingredients

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            binding !!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT)
        }
        else
        {
            @Suppress("DEPRECATION")
            binding !!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        binding !!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
                                     )
                                                  )

        var addedToFavourites = false

        binding !!.tvCookingTime.text =
                resources.getString(
                    R.string.lbl_estimate_cooking_time,
                    recipe.readyInMinutes.toString()
                                   )

        binding !!.ivFavoriteDish.setOnClickListener {

            if (addedToFavourites)
            {
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favourites),
                    Toast.LENGTH_SHORT).show()
            }
            else
            {
                val randomDishDetails = CookBook(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                                                )

                val mCookBookViewModel : CookBookViewModel by viewModels {
                    CookBookViewModelFactory((requireActivity().application as CookBookApplication).repository)
                }
                mCookBookViewModel.insert(randomDishDetails)

                addedToFavourites = true

                binding !!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                                             )
                                                          )
                Toast.makeText(requireActivity(),
                    resources.getString(R.string.msg_added_to_favourites),
                    Toast.LENGTH_SHORT).show()
            }

        }

        val mCookBookView : CookBookViewModel by viewModels {
            CookBookViewModelFactory((requireActivity().application as CookBookApplication).repository)
        }

    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding = null
    }
}