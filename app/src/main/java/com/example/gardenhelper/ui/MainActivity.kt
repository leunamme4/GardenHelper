package com.example.gardenhelper.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gardenhelper.R
import com.example.gardenhelper.data.work_manager.WorkManagerScheduler
import com.example.gardenhelper.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.core.component.inject

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "channel_id"

    private lateinit var binding: ActivityMainBinding

    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    val workManagerScheduler: WorkManagerScheduler by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_garden_list); // change to whichever id should be default
        }

        // notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
        if (existingChannel == null) {
            createNotificationChannel()
        }

        workManagerScheduler.scheduleDailyNetworkRequest()
    }

    private fun createNotificationChannel() {
        val name = "objectsActions"
        val descriptionText = "Notify about your actions with the object"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    fun hideNavigationBar() {
        binding.navView.isVisible = false
    }

    fun showNavigationBar() {
        binding.navView.isVisible = true
    }
}