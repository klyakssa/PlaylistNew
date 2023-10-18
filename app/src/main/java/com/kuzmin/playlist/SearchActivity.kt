package com.kuzmin.playlist

import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kuzmin.playlist.iTunesAPI.TracksResponse
import com.kuzmin.playlist.iTunesAPI.itunesApi
import com.kuzmin.playlist.trackList.Track
import com.kuzmin.playlist.trackList.TracksListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class SearchActivity : AppCompatActivity() {

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeholderImage: TextView
    private lateinit var placeholderMessage: TextView
    private lateinit var updateButton: Button
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var textHistory: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var recyclerViewTracksHistory: RecyclerView
    private lateinit var searchHistory: SearchHistory
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { getTracks()}

    private var isClickAllowed = true


    private val itunesBaseUrl = "https://itunes.apple.com"


    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("YYYY-MM-dd'T'hh:mm:ss").create()))
        .build()

    private val itunesService = retrofit.create(itunesApi::class.java)

    private val tracksList = ArrayList<Track>()
    private val tracksAdapter = TracksListAdapter{_,it ->
        if (clickDebounce()) {
            tracksListHistory.remove(it)
            tracksListHistory.add(0, it)
            if (tracksListHistory.size > 10) {
                tracksListHistory.subList(10, tracksListHistory.size).clear()
            }
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(Const.TRACK_TO_ARRIVE.const, Gson().toJson(it));
            startActivity(playerIntent)
        }
    }

    private val tracksListHistory = ArrayList<Track>()
    private val tracksAdapterHistory = TracksListAdapter{ adapter,it ->
        if (clickDebounce()) {
            tracksListHistory.remove(it)
            tracksListHistory.add(0, it)
            adapter.notifyDataSetChanged()
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(Const.TRACK_TO_ARRIVE.const, Gson().toJson(it));
            startActivity(playerIntent)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPrefs = getSharedPreferences(Const.PLAYLIST_PREFERENCES.const, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)

        inputEditText = findViewById<EditText>(R.id.inputEditText)

        clearButton = findViewById<ImageView>(R.id.clearIcon)
        placeholderImage = findViewById<TextView>(R.id.placeholderImage)
        placeholderMessage = findViewById<TextView>(R.id.textPlaceholderMessage)
        updateButton = findViewById<Button>(R.id.updateButton)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        placeholderImage.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        updateButton.visibility = View.GONE

        recyclerViewTracks = findViewById<RecyclerView>(R.id.tracksList)

        recyclerViewTracksHistory = findViewById<RecyclerView>(R.id.tracksListHistory)
        textHistory = findViewById<TextView>(R.id.textHistory)
        clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)

        showSearchHistory(inputEditText.text.toString())

        textHistory.text = getString(R.string.searchMessage)

        tracksAdapter.data = tracksList
        recyclerViewTracks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTracks.adapter = tracksAdapter

        tracksListHistory.addAll(searchHistory.readPreferences())
        tracksAdapterHistory.data = tracksListHistory
        recyclerViewTracksHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewTracksHistory.adapter = tracksAdapterHistory

        updateButton.setOnClickListener {
            getTracks()
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
                showSearchHistory(inputEditText.text.toString())
                tracksAdapterHistory.notifyDataSetChanged()

        }

        clearHistoryButton.setOnClickListener {
            tracksListHistory.clear()
            tracksAdapterHistory.notifyDataSetChanged()
            searchHistory.clearHistory()
            showSearchHistory(inputEditText.text.toString())
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            tracksList.clear()
            tracksAdapter.notifyDataSetChanged()
            closeKeyboard()
            showPlaceholder(true,"")
            showSearchHistory(inputEditText.text.toString())
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchDebounce()
                true
            }
            false
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
                showSearchHistory(s.toString())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showSearchHistory(text: String) {
        recyclerViewTracksHistory.visibility = if (inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        recyclerViewTracks.visibility = if (inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.GONE  else View.VISIBLE
        textHistory.visibility = if (inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        clearHistoryButton.visibility = if (inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        updateButton.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        if (inputEditText.hasFocus() && text?.isEmpty() == true && tracksListHistory.isNotEmpty()) {
            recyclerViewTracks.recycledViewPool.clear();
            tracksAdapter.notifyDataSetChanged();
        }else{
            recyclerViewTracksHistory.recycledViewPool.clear();
            tracksAdapterHistory.notifyDataSetChanged();
        }
    }


    private fun showPlaceholder(isEthernet: Boolean, text: String) {
        if(text.isNotEmpty()) {
            if (isEthernet) {
                updateButton.visibility = View.GONE
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.visibility = View.VISIBLE
                tracksList.clear()
                tracksAdapter.notifyDataSetChanged()
                placeholderMessage.text = text
                placeholderImage.setBackgroundResource(R.drawable.ic_nothing)

            } else {
                updateButton.visibility = View.VISIBLE
                placeholderMessage.visibility = View.VISIBLE
                placeholderImage.visibility = View.VISIBLE
                tracksList.clear()
                tracksAdapter.notifyDataSetChanged()
                placeholderMessage.text = text
                placeholderImage.setBackgroundResource(R.drawable.ic_noethernet)
            }
        }else{
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }

    private fun getTracks() {
        if (inputEditText.text.isNotEmpty()) {

            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            recyclerViewTracks.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            updateButton.visibility = View.GONE

            itunesService.search(inputEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        tracksList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            recyclerViewTracks.visibility = View.VISIBLE
                            tracksList.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (tracksList.isEmpty()) {
                            showPlaceholder( true , getString(R.string.nothing_found))
                        }else {
                            showPlaceholder( true , "")
                        }
                    } else {
                        showPlaceholder(false, getString(R.string.something_went_wrong))
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showPlaceholder(false, getString(R.string.something_went_wrong))
                }

            })
        }
    }

    override fun onStop() {
        searchHistory.savePreferences(tracksListHistory)
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH,inputEditText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val countValue = savedInstanceState.getString(SEARCH,"")
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
        const val SEARCH = "SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}