package com.kyawsithu.cookbook.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.ActivityAddUpdateDishesBinding
import com.kyawsithu.cookbook.databinding.DialogCustomImageSelectionBinding

class AddUpdateDishesActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var addUpdateDishesBinding: ActivityAddUpdateDishesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUpdateDishesBinding = ActivityAddUpdateDishesBinding.inflate(layoutInflater)
        setContentView(addUpdateDishesBinding.root)

        setupActionBar()
        addUpdateDishesBinding.ivAddDishImage.setOnClickListener(this)

    }

    private fun setupActionBar() {
        setSupportActionBar(addUpdateDishesBinding.toolbarAddUpdateDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addUpdateDishesBinding.toolbarAddUpdateDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                    return
                }
            }
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Toast.makeText(this, "Camera", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {
            Toast.makeText(this, "Gallery", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }
}