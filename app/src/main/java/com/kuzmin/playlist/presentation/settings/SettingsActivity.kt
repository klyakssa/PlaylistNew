package com.kuzmin.playlist.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuzmin.playlist.R
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.databinding.ActivitySettingsBinding
import com.kuzmin.playlist.domain.iterators.preferences.PreferencesIteractor
import com.kuzmin.playlist.domain.model.Preferences

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var workWithPreferences: PreferencesIteractor

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }
        workWithPreferences = Creator.providePreferencesInteraction(getSharedPreferences(
            Preferences.PLAYLIST_PREFERENCES.pref, MODE_PRIVATE
        ))
        binding.switch1.setOnCheckedChangeListener { switcher, checked ->
            workWithPreferences.setThemeToPreferences(checked)
        }

        val message = getString(R.string.messageProfile)
        binding.sharingProgram.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = getString(R.string.type)
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.supportButton.setOnClickListener{
            Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse(getString(R.string.mailto))
                putExtra(Intent.EXTRA_EMAIL, getString(R.string.emailMy))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subjectEmail))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.textEmail))
                type = getString(R.string.type)
                startActivity(this)
            }
        }
        binding.termsButton.setOnClickListener{
            val webIntent: Intent = Uri.parse(getString(R.string.termsLink)).let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)
        }
        binding.switch1.isChecked = workWithPreferences.getThemeFromPreferences()
    }
}