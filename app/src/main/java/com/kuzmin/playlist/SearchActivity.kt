package com.kuzmin.playlist

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kuzmin.playlist.R

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        inputEditText = findViewById<EditText>(R.id.inputEditText)
        clearButton = findViewById<ImageView>(R.id.clearIcon)


        clearButton.setOnClickListener {
            inputEditText.setText("")
            closeKeyboard()
        }

        val btnBack = findViewById<TextView>(R.id.back)
        btnBack.setOnClickListener {
            finish()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(PRODUCT_AMOUNT,inputEditText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Вторым параметром мы передаём значение по умолчанию
        val countValue = savedInstanceState.getString(PRODUCT_AMOUNT,"")
        inputEditText.setText(countValue)
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun closeKeyboard(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private companion object {
        const val PRODUCT_AMOUNT = "PRODUCT_AMOUNT"
    }
}