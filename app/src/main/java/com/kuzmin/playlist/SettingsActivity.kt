package com.kuzmin.playlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.kuzmin.playlist.LibraryActivity
import com.kuzmin.playlist.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<TextView>(R.id.back)
        btnBack.setOnClickListener {
            finish()
        }
        val message = "https://practicum.yandex.ru/profile/android-developer/"
        val sharing = findViewById<LinearLayout>(R.id.sharing)
        sharing.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        val helping = findViewById<LinearLayout>(R.id.helping)
        helping.setOnClickListener{
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("denis.spb.2004@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
                putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
                type = "text/plain"
            }

            startActivity(sendIntent)
        }


        val terms = findViewById<LinearLayout>(R.id.terms)
        terms.setOnClickListener{
            val webIntent: Intent = Uri.parse("https://yandex.ru/legal/practicum_offer/").let { webpage ->
                Intent(Intent.ACTION_VIEW, webpage)
            }
            startActivity(webIntent)
        }

    }
}