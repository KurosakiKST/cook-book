package com.kyawsithu.cookbook.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.ActivityAddUpdateDishesBinding

class AddUpdateDishesActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var addUpdateDishesBinding: ActivityAddUpdateDishesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUpdateDishesBinding = ActivityAddUpdateDishesBinding.inflate(layoutInflater)
        setContentView(addUpdateDishesBinding.root)

        setupActionBar()
        addUpdateDishesBinding.ivAddDishImage.setOnClickListener(this)

    }

    private fun setupActionBar(){
        setSupportActionBar(addUpdateDishesBinding.toolbarAddUpdateDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addUpdateDishesBinding.toolbarAddUpdateDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        if(view != null){
            when(view.id){
                R.id.iv_add_dish_image -> {
                    Toast.makeText(this, "Image View Add", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }
}