package com.kuzmin.playlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.kuzmin.playlist.LibraryActivity
import com.kuzmin.playlist.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val btnBack = findViewById<TextView>(R.id.back)

        btnBack.setOnClickListener {
            this.finish()
        }
    }
}