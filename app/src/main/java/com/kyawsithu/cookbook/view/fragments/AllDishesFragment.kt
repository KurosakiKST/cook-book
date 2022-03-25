package com.kyawsithu.cookbook.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.FragmentAllDishesBinding
import com.kyawsithu.cookbook.view.activities.AddUpdateDishesActivity
import com.kyawsithu.cookbook.view.activities.MainActivity
import com.kyawsithu.cookbook.view.adapters.CookBookAdapter
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory
import com.kyawsithu.cookbook.viewmodel.HomeViewModel

class AllDishesFragment : Fragment()
{

    private var allDishesBinding : FragmentAllDishesBinding? = null

    private lateinit var mAllDishesBinding : FragmentAllDishesBinding

    private val mCookBookViewModel : CookBookViewModel by viewModels {
        CookBookViewModelFactory((requireActivity().application as CookBookApplication).repository)
    }

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?) : View?
    {
        mAllDishesBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return mAllDishesBinding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        mAllDishesBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val cookBookAdapter = CookBookAdapter(this@AllDishesFragment)

        mAllDishesBinding.rvDishesList.adapter = cookBookAdapter

        mCookBookViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if(it.isNotEmpty()){
                    mAllDishesBinding.rvDishesList.visibility = View.VISIBLE
                    mAllDishesBinding.tvNoDishesAddedYet.visibility = View.GONE

                    cookBookAdapter.dishesList(it)
                }else{
                    mAllDishesBinding.rvDishesList.visibility = View.GONE
                    mAllDishesBinding.tvNoDishesAddedYet.visibility = View.VISIBLE

                }
            }
        }
    }

    fun dishDetails() {
        findNavController().navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails())
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume()
    {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }

    override fun onCreateOptionsMenu(menu : Menu, inflater : MenuInflater)
    {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.action_add_dish ->
            {
                startActivity(Intent(requireActivity(), AddUpdateDishesActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        allDishesBinding = null
    }
}