package com.kuzmin.playlist.presentation.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.viewPager.adapter = LibraryViewPagerAdapter(supportFragmentManager,
            lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}