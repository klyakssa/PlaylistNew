package com.kuzmin.playlist.presentation.main

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

    }

    fun animateBottomNavigationView(gone: Int) {
        binding.bottomNavigationView.visibility = gone
    }

    fun animateBottomNavigationViewE(gone: Int) {
        binding.bottomNavigationView.visibility = gone
    }
}