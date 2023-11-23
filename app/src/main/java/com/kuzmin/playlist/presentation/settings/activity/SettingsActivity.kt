package com.kuzmin.playlist.presentation.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivitySettingsBinding
import com.kuzmin.playlist.presentation.application.App
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(this, SettingsViewModel.getViewModelFactory((application as App)))[SettingsViewModel::class.java]
    }
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.switch1.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme(checked)
        }

        binding.sharingProgram.setOnClickListener{
            startActivity(viewModel.shareApp())
        }

        binding.supportButton.setOnClickListener{
            startActivity(viewModel.openSupport())
        }
        binding.termsButton.setOnClickListener{
            startActivity(viewModel.openTerms())
        }
        binding.switch1.isChecked = viewModel.getTheme()
    }
}