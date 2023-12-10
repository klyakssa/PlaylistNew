package com.kuzmin.playlist.presentation.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuzmin.playlist.databinding.ActivitySettingsBinding
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {


    private val viewModel by viewModel<SettingsViewModel>()

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

        binding.supportButton.setOnClickListener {
            startActivity(viewModel.openSupport())
        }
        binding.termsButton.setOnClickListener{
            startActivity(viewModel.openTerms())
        }
        binding.switch1.isChecked = viewModel.getTheme()
    }
}