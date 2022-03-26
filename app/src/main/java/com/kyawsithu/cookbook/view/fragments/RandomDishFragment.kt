package com.kyawsithu.cookbook.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kyawsithu.cookbook.databinding.FragmentRandomDishBinding
import com.kyawsithu.cookbook.viewmodel.NotificationsViewModel

class RandomDishFragment : Fragment()
{

    private var binding : FragmentRandomDishBinding? = null

    override fun onCreateView(
            inflater : LayoutInflater,
            container : ViewGroup?,
            savedInstanceState : Bundle?
                             ) : View
    {
        binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding !!.root
    }

    override fun onDestroy()
    {
        super.onDestroy()
        binding = null
    }
}