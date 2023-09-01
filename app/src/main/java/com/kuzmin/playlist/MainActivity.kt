package com.kuzmin.playlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.kuzmin.playlist.LibraryActivity
import com.kuzmin.playlist.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSearch = findViewById<Button>(R.id.search)

        val btnSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))

            }
        }
        btnSearch.setOnClickListener(btnSearchClickListener)

        val btnLibrary = findViewById<Button>(R.id.library)

        btnLibrary.setOnClickListener {

            val libraryIntent = Intent(this, LibraryActivity::class.java)
            startActivity(libraryIntent)

        }
        val btnSettings = findViewById<Button>(R.id.settings)

        btnSettings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }


    }
}