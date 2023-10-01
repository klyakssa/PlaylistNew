package com.kuzmin.playlist

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kuzmin.playlist.Const.CONST
import com.kuzmin.playlist.LibraryActivity
import com.kuzmin.playlist.R

class SettingsActivity : AppCompatActivity() {



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<TextView>(R.id.back)
        btnBack.setOnClickListener {
            finish()
        }
        val message = getString(R.string.messageProfile)
        val sharing = findViewById<LinearLayout>(R.id.sharingProgram)
        sharing.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = getString(R.string.type)
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val helping = findViewById<LinearLayout>(R.id.supportButton)
        helping.setOnClickListener{
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


        val terms = findViewById<LinearLayout>(R.id.termsButton)
        terms.setOnClickListener{
            val webIntent: Intent = Uri.parse(getString(R.string.termsLink)).let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)
        }
        val sharedPrefs = getSharedPreferences(CONST.PLAYLIST_PREFERENCES.const, MODE_PRIVATE)

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.switch1)
        var theme = sharedPrefs.getBoolean(CONST.DARK_THEME_KEY.const, false)

        if(theme){
            themeSwitcher.isChecked = true
        }

        var listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                theme = sharedPrefs.getBoolean(CONST.DARK_THEME_KEY.const, false)
                (applicationContext as App).switchTheme(theme)
        }

        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)


        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            sharedPrefs.edit()
                .putBoolean(CONST.DARK_THEME_KEY.const, checked)
                .apply()
            //(applicationContext as App).switchTheme(checked)
        }
    }


}