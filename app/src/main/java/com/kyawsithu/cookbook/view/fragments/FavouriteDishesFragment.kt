package com.kyawsithu.cookbook.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.kyawsithu.cookbook.application.CookBookApplication
import com.kyawsithu.cookbook.databinding.FragmentFavouriteDishesBinding
import com.kyawsithu.cookbook.viewmodel.CookBookViewModel
import com.kyawsithu.cookbook.viewmodel.CookBookViewModelFactory
import com.kyawsithu.cookbook.viewmodel.DashboardViewModel

class FavouriteDishesFragment : Fragment() {

    private var _binding: FragmentFavouriteDishesBinding? = null

    private val mCookBookViewModel: CookBookViewModel by viewModels {
        CookBookViewModelFactory((requireActivity().application as CookBookApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentFavouriteDishesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        mCookBookViewModel.favouriteDishes.observe(viewLifecycleOwner){
            dishes ->
            dishes.let{
                if(it.isNotEmpty()){
                    for(dish in it){
                        Log.i("FavDish", "${dish.id} :: ${dish.title}")
                    }
                }else{
                    Log.i("FavDish", "Empty dish")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}