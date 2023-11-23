package com.kuzmin.playlist.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kuzmin.playlist.databinding.ActivityMainBinding
import com.kuzmin.playlist.presentation.library.LibraryActivity
import com.kuzmin.playlist.presentation.search.TracksSearchActivity
import com.kuzmin.playlist.presentation.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnSearchClickListener: View.OnClickListener = View.OnClickListener {
            startActivity(Intent(this@MainActivity, TracksSearchActivity::class.java))
        }
        binding.search.setOnClickListener(btnSearchClickListener)
        binding.library.setOnClickListener {
            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }
        binding.settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}