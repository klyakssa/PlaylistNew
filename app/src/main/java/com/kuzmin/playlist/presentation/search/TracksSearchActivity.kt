package com.kuzmin.playlist.presentation.search

import android.content.Context
import android.content.Intent
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
import com.google.gson.Gson
import com.kuzmin.playlist.domain.model.Preferences
import com.kuzmin.playlist.presentation.audioplayer.PlayerActivity
import com.kuzmin.playlist.R
import com.kuzmin.playlist.domain.model.TrackDto
import com.kuzmin.playlist.creator.Creator
import com.kuzmin.playlist.databinding.ActivitySearchBinding
import com.kuzmin.playlist.domain.preferencesSearchHistory.iteractors.PreferencesSearchHistoryIteractor
import com.kuzmin.playlist.domain.searchTracksByName.consumer.Consumer
import com.kuzmin.playlist.domain.searchTracksByName.consumer.ConsumerData
import kotlin.collections.ArrayList

class TracksSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    //private lateinit var searchHistory: SearchHistory
    private lateinit var searchHistory: PreferencesSearchHistoryIteractor
    private val getTracksUseCase = Creator.provideGetTracksListUseCase()

    private val handler = Handler(Looper.getMainLooper())

    private var tracksRunnable: Runnable? = null

    private var searchRunnableInput = Runnable {
        if (binding.inputEditText.text.isNotEmpty()) {
            binding.placeholderImage.visibility = View.GONE
            binding.textPlaceholderMessage.visibility = View.GONE
            binding.tracksList.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.updateButton.visibility = View.GONE
            getTracksUseCase.execute(
                binding.inputEditText.text.toString(),
                consumer = object : Consumer<ArrayList<TrackDto>>{
                    override fun consume(data: ConsumerData<ArrayList<TrackDto>>) {
                        val currentRunnable = tracksRunnable
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable)
                        }
                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is ConsumerData.Error -> showPlaceholder(data.message)
                                is ConsumerData.Data -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.tracksList.visibility = View.VISIBLE
                                    tracksList.addAll(data.value)
                                    tracksAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                        tracksRunnable = newDetailsRunnable
                        handler.post(newDetailsRunnable)
                    }
                }
            )
        }
    }

    private var isClickAllowed = true


    private val tracksList = ArrayList<TrackDto>()
    private val tracksAdapter = TracksListAdapter{_,it ->
        if (clickDebounce()) {
            tracksListHistory.remove(it)
            tracksListHistory.add(0, it)
            if (tracksListHistory.size > 10) {
                tracksListHistory.subList(10, tracksListHistory.size).clear()
            }
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(Preferences.TRACK_TO_ARRIVE.pref, Gson().toJson(it));
            startActivity(playerIntent)
        }
    }

    private val tracksListHistory = ArrayList<TrackDto>()
    private val tracksAdapterHistory = TracksListAdapter{ adapter, it ->
        if (clickDebounce()) {
            tracksListHistory.remove(it)
            tracksListHistory.add(0, it)
            adapter.notifyDataSetChanged()
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(Preferences.TRACK_TO_ARRIVE.pref, Gson().toJson(it));
            startActivity(playerIntent)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchHistory = Creator.providePreferencesSearchHistoryInteraction(getSharedPreferences(
            Preferences.PLAYLIST_PREFERENCES.pref, MODE_PRIVATE
        ))

        showSearchHistory(binding.inputEditText.text.toString())

        binding.textHistory.text = getString(R.string.searchMessage)

        tracksAdapter.data = tracksList
        binding.tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = tracksAdapter

        tracksListHistory.addAll(searchHistory.getHistory())
        tracksAdapterHistory.data = tracksListHistory
        binding.tracksListHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tracksListHistory.adapter = tracksAdapterHistory

        binding.updateButton.setOnClickListener {
            //getTracks()
            searchDebounce()
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
                showSearchHistory(binding.inputEditText.text.toString())
                tracksAdapterHistory.notifyDataSetChanged()
        }

        binding.clearHistoryButton.setOnClickListener {
            tracksListHistory.clear()
            tracksAdapterHistory.notifyDataSetChanged()
            searchHistory.clearHistory()
            showSearchHistory(binding.inputEditText.text.toString())
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            tracksList.clear()
            tracksAdapter.notifyDataSetChanged()
            closeKeyboard()
            //showPlaceholder(true,"")TODO
            showSearchHistory(binding.inputEditText.text.toString())
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
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
                binding.clearIcon.visibility = clearButtonVisibility(s)
                showSearchHistory(s.toString())
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

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
        handler.removeCallbacks(searchRunnableInput)
        handler.postDelayed(searchRunnableInput, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showSearchHistory(text: String) {
        binding.tracksListHistory.visibility = if (binding.inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tracksList.visibility = if (binding.inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.GONE  else View.VISIBLE
        binding.textHistory.visibility = if (binding.inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.clearHistoryButton.visibility = if (binding.inputEditText.hasFocus() && text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.updateButton.visibility = View.GONE
        binding.textPlaceholderMessage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        if (binding.inputEditText.hasFocus() && text?.isEmpty() == true && tracksListHistory.isNotEmpty()) {
            binding.tracksList.recycledViewPool.clear();
            tracksAdapter.notifyDataSetChanged();
        }else{
            binding.tracksListHistory.recycledViewPool.clear();
            tracksAdapterHistory.notifyDataSetChanged();
        }
    }


    private fun showPlaceholder(text: String) {
        binding.progressBar.visibility = View.GONE
        if(text.isNotEmpty()) {
            if (text.contains("Empty")) {
                binding.updateButton.visibility = View.GONE
                binding.textPlaceholderMessage.visibility = View.VISIBLE
                binding.placeholderImage.visibility = View.VISIBLE
                tracksList.clear()
                tracksAdapter.notifyDataSetChanged()
                binding.textPlaceholderMessage.text = getString(R.string.nothing_found)
                binding.placeholderImage.setBackgroundResource(R.drawable.ic_nothing)

            } else {
                binding.updateButton.visibility = View.VISIBLE
                binding.textPlaceholderMessage.visibility = View.VISIBLE
                binding.placeholderImage.visibility = View.VISIBLE
                tracksList.clear()
                tracksAdapter.notifyDataSetChanged()
                binding.textPlaceholderMessage.text = getString(R.string.something_went_wrong)
                binding.placeholderImage.setBackgroundResource(R.drawable.ic_noethernet)
            }
        }else{
            binding.placeholderImage.visibility = View.GONE
            binding.textPlaceholderMessage.visibility = View.GONE
            binding.updateButton.visibility = View.GONE
        }
    }

//    private fun getTracks() {
//        if (binding.inputEditText.text.isNotEmpty()) {
//
//            binding.placeholderImage.visibility = View.GONE
//            binding.textPlaceholderMessage.visibility = View.GONE
//            binding.tracksList.visibility = View.GONE
//            binding.progressBar.visibility = View.VISIBLE
//            binding.updateButton.visibility = View.GONE
//
//            itunesService.search(binding.inputEditText.text.toString()).enqueue(object :
//                Callback<TracksResponse> {
//                override fun onResponse(call: Call<TracksResponse>,
//                                        response: Response<TracksResponse>
//                ) {
//                    binding.progressBar.visibility = View.GONE
//                    if (response.code() == 200) {
//                        tracksList.clear()
//                        if (response.body()?.results?.isNotEmpty() == true) {
//                            binding.tracksList.visibility = View.VISIBLE
//                            tracksList.addAll(response.body()?.results!!)
//                            tracksAdapter.notifyDataSetChanged()
//                        }
//                        if (tracksList.isEmpty()) {
//                            showPlaceholder( true , getString(R.string.nothing_found))
//                        }else {
//                            showPlaceholder( true , "")
//                        }
//                    } else {
//                        showPlaceholder(false, getString(R.string.something_went_wrong))
//                    }
//                }
//
//                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
//                    binding.progressBar.visibility = View.GONE
//                    showPlaceholder(false, getString(R.string.something_went_wrong))
//                }
//
//            })
//        }
//    }

    override fun onStop() {
        searchHistory.saveHistory(tracksListHistory)
        super.onStop()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH,binding.inputEditText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val countValue = savedInstanceState.getString(SEARCH,"")
        binding.inputEditText.setText(countValue)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        val currentRunnable = searchRunnableInput
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }
        super.onDestroy()
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