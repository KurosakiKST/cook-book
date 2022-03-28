package com.kyawsithu.cookbook.view.activities

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.kyawsithu.cookbook.R
import com.kyawsithu.cookbook.databinding.ActivityMainBinding
import com.kyawsithu.cookbook.model.notification.NotifyWorker
import com.kyawsithu.cookbook.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity()
{

    private lateinit var binding : ActivityMainBinding

    private lateinit var mNavController : NavController

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView : BottomNavigationView = binding.navView

        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes, R.id.navigation_favourite_dishes, R.id.navigation_random_dish
                 )
                                                     )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        navView.setupWithNavController(mNavController)

        if(intent.hasExtra(Constants.NOTIFICATION_ID)){
            val notificationID = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
            binding.navView.selectedItemId = R.id.navigation_random_dish
        }

        startWork()
    }

    private fun createConstraints() = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(true)
            .build()

    private fun createWorkRequest() =
            PeriodicWorkRequestBuilder<NotifyWorker>(1, TimeUnit.DAYS)
                    .setConstraints(createConstraints())
                    .build()

    private fun startWork(){
        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                    "CookBook Notify Work",
                    ExistingPeriodicWorkPolicy.KEEP,
                    createWorkRequest()
                                          )
    }

    override fun onSupportNavigateUp() : Boolean
    {
        return NavigationUI.navigateUp(mNavController, null)
    }

    fun hideBottomNavigationView(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavigationView(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
        binding.navView.visibility = View.VISIBLE
    }
}