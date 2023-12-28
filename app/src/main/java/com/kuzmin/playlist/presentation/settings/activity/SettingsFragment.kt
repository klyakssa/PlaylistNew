package com.kuzmin.playlist.presentation.settings.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kuzmin.playlist.databinding.ActivitySettingsBinding
import com.kuzmin.playlist.presentation.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {


    private val viewModel by viewModel<SettingsViewModel>()

    private lateinit var binding: ActivitySettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
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