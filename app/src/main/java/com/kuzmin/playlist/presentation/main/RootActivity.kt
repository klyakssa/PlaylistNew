package com.kuzmin.playlist.presentation.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivityRootBinding
import com.kuzmin.playlist.presentation.main.models.OnBackButtonListener
import org.koin.android.ext.android.inject


class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    val onBackPressedListener : OnBackButtonListener by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this /* lifecycle owner */, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val backStackCount = navController.currentBackStack.value?.size ?: 0

                if (backStackCount > 0) {
                    val currentFragment: Fragment? = navHostFragment!!.childFragmentManager.fragments[0]
                    if (currentFragment is OnBackButtonListener) {
                        val actionResult: Boolean = currentFragment!!.onBackPressed()
                        if (actionResult) {
                            return
                        }
                    }
                }
//                val state = onBackPressedListener.onBackPressed()
//                when (state) {
//                    is onBackPressedState.player -> {
//                        if (state.animate) {
//                            animateBottomNavigationView(View.VISIBLE)
//                        }
//                        navController.navigateUp()
//                    }
//                    is onBackPressedState.createPlaylist -> {
//                        if (state.animate) {
//                            animateBottomNavigationView(View.VISIBLE)
//                        }
//                        navController.navigateUp()
//                    }
//                }
            }
        })

    }

    fun animateBottomNavigationView(gone: Int) {
        binding.bottomNavigationView.visibility = gone
    }

}