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
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
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
import com.kuzmin.playlist.presentation.search.model.TracksState
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import kotlin.collections.ArrayList

class TracksSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: TracksSearchViewModel

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var textWatcher: TextWatcher

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
        viewModel = ViewModelProvider(this, TracksSearchViewModel.getViewModelFactory())[TracksSearchViewModel::class.java]

        //showSearchHistory(binding.inputEditText.text.toString())
        showHistoryContent()

        binding.textHistory.text = getString(R.string.searchMessage)

        tracksAdapter.data = tracksList
        binding.tracksList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = tracksAdapter

        tracksListHistory.addAll(viewModel.getHistory())
        tracksAdapterHistory.data = tracksListHistory
        binding.tracksListHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.tracksListHistory.adapter = tracksAdapterHistory

        binding.updateButton.setOnClickListener {
            //getTracks()
            viewModel.searchDebounce(binding.inputEditText.text.toString())
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
                showHistoryContent()
        }

        binding.clearHistoryButton.setOnClickListener {
            tracksListHistory.clear()
            tracksAdapterHistory.notifyDataSetChanged()
            viewModel.clearHistory()
            showHistoryContent()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            tracksList.clear()
            tracksAdapter.notifyDataSetChanged()
            closeKeyboard()
            showHistoryContent()
        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(binding.inputEditText.text.toString())
                true
            }
            false
        }
        val btnBack = findViewById<TextView>(R.id.back)
        btnBack.setOnClickListener {
            finish()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                showHistoryContent()
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        textWatcher.let { binding.inputEditText.addTextChangedListener(it) }

        viewModel.observeState().observe(this) {
            render(it)
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Content -> showContent(state.movies)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.tracksList.visibility = View.GONE
        binding.tracksListHistory.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.textPlaceholderMessage.visibility = View.GONE
        binding.textHistory.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        binding.tracksList.visibility = View.GONE
        binding.tracksListHistory.visibility = View.GONE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderImage.setBackgroundResource(R.drawable.ic_noethernet)
        binding.textPlaceholderMessage.visibility = View.VISIBLE
        binding.textPlaceholderMessage.text = errorMessage
        binding.textHistory.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
        binding.updateButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        tracksList.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showEmpty(emptyMessage: String) {
        binding.tracksList.visibility = View.GONE
        binding.tracksListHistory.visibility = View.GONE
        binding.placeholderImage.visibility = View.VISIBLE
        binding.placeholderImage.setBackgroundResource(R.drawable.ic_nothing)
        binding.textPlaceholderMessage.visibility = View.VISIBLE
        binding.textPlaceholderMessage.text = emptyMessage
        binding.textHistory.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
        binding.updateButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        tracksList.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<TrackDto>) {
        binding.tracksList.visibility = View.VISIBLE
        binding.tracksListHistory.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.textPlaceholderMessage.visibility = View.GONE
        binding.textHistory.visibility = View.GONE
        binding.clearHistoryButton.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        tracksList.clear()
        tracksList.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showHistoryContent() {
        binding.tracksListHistory.visibility = if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.tracksList.visibility = if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty() && tracksListHistory.isNotEmpty()) View.GONE  else View.VISIBLE
        binding.textHistory.visibility = if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.clearHistoryButton.visibility = if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty() && tracksListHistory.isNotEmpty()) View.VISIBLE else View.GONE
        binding.updateButton.visibility = View.GONE
        binding.textPlaceholderMessage.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        if (binding.inputEditText.hasFocus() && binding.inputEditText.text?.isEmpty() == true && tracksListHistory.isNotEmpty()) {
            binding.tracksList.recycledViewPool.clear();
            tracksAdapter.notifyDataSetChanged();
        }else{
            binding.tracksListHistory.recycledViewPool.clear();
            tracksAdapterHistory.notifyDataSetChanged();
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onStop() {
        viewModel.saveHistory(tracksListHistory)
        super.onStop()
    }

    override fun onDestroy() {
        textWatcher.let { binding.inputEditText.removeTextChangedListener(it) }
        super.onDestroy()
    }

    private fun closeKeyboard(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private companion object {
        const val SEARCH = "SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}