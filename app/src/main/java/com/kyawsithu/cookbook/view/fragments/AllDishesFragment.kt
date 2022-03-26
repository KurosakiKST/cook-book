package com.kyawsithu.cookbook.view.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.DialogCustomListBinding
import com.kyawsithu.cookbook.databinding.FragmentAllDishesBinding
import com.kyawsithu.cookbook.model.entities.CookBook
import com.kyawsithu.cookbook.utils.Constants
import com.kyawsithu.cookbook.view.activities.AddUpdateDishesActivity
import com.kyawsithu.cookbook.view.activities.MainActivity
import com.kyawsithu.cookbook.view.adapters.CookBookAdapter
import com.kyawsithu.cookbook.view.adapters.CustomListItemAdapter
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory

class AllDishesFragment : Fragment()
{
    private lateinit var binding : FragmentAllDishesBinding

    private lateinit var cookBookAdapter : CookBookAdapter

    private lateinit var customListDialog : Dialog

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
        binding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        cookBookAdapter = CookBookAdapter(this@AllDishesFragment)
        binding.rvDishesList.adapter = cookBookAdapter

        mCookBookViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {
                if (it.isNotEmpty())
                {
                    binding.rvDishesList.visibility = View.VISIBLE
                    binding.tvNoDishesAddedYet.visibility = View.GONE

                    cookBookAdapter.dishesList(it)
                }
                else
                {
                    binding.rvDishesList.visibility = View.GONE
                    binding.tvNoDishesAddedYet.visibility = View.VISIBLE

                }
            }
        }
    }

    fun dishDetails(cookBook : CookBook)
    {
        findNavController().navigate(AllDishesFragmentDirections.actionAllDishesToDishDetails(cookBook))
        if (requireActivity() is MainActivity)
        {
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    fun deleteDish(cookBook : CookBook)
    {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, cookBook.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mCookBookViewModel.delete(cookBook)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun filterDishesListDialog()
    {
        customListDialog = Dialog(requireActivity())
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        customListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)
        val dishTypes = Constants.dishType()
        dishTypes.add(0, Constants.ALL_ITEMS)

        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(requireActivity(), this@AllDishesFragment, dishTypes, Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter
        customListDialog.show()
    }

    override fun onResume()
    {
        super.onResume()
        if (requireActivity() is MainActivity)
        {
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

            R.id.action_filter_dishes ->
            {
                filterDishesListDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun filterSelection(filterItemSelection : String)
    {
        customListDialog.dismiss()
        Log.i("Selected", filterItemSelection)

        if (filterItemSelection == Constants.ALL_ITEMS)
        {
            mCookBookViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
                dishes.let {
                    if (it.isNotEmpty())
                    {
                        binding.rvDishesList.visibility = View.VISIBLE
                        binding.tvNoDishesAddedYet.visibility = View.GONE

                        cookBookAdapter.dishesList(it)
                    }
                    else
                    {
                        binding.rvDishesList.visibility = View.GONE
                        binding.tvNoDishesAddedYet.visibility = View.VISIBLE

                    }
                }
            }
        }
        else
        {
            Log.i("FilteredList", "Get Filter List")
        }
    }
}