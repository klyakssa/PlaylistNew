package com.kuzmin.playlist.presentation.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivitySearchBinding
import com.kuzmin.playlist.presentation.main.RootActivity
import com.kuzmin.playlist.presentation.models.Track
import com.kuzmin.playlist.presentation.search.model.TracksState
import com.kuzmin.playlist.presentation.search.view_model.TracksSearchViewModel
import com.kuzmin.playlist.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList

class TracksSearchFragment : Fragment() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by viewModel<TracksSearchViewModel>()


    private lateinit var textWatcher: TextWatcher

    private lateinit var onPlayerDebounce: (Track) -> Unit


    private val tracksList = ArrayList<Track>()
    private val tracksAdapter = TracksListAdapter{_,it ->
        if (viewModel.clickDebounce()) {
            viewModel.clickOnMainTrack(it)
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onPlayerDebounce(it)
        }
    }

    private val tracksListHistory = ArrayList<Track>()
    private val tracksAdapterHistory = TracksListAdapter{ adapter, it ->
        if (viewModel.clickDebounce()) {
            viewModel.clickOnHistoryTrack(it)
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onPlayerDebounce(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onPlayerDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope,  Dispatchers.Main) { track ->
            findNavController().navigate(R.id.action_searchFragment_to_playerActivity, bundleOf(
                TRACK_TO_ARRIVE to Gson().toJson(track)))
        }

        tracksAdapter.data = tracksList
        binding.tracksList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksList.adapter = tracksAdapter

        tracksAdapterHistory.data = tracksListHistory
        binding.tracksListHistory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tracksListHistory.adapter = tracksAdapterHistory

        binding.updateButton.setOnClickListener {
            viewModel.searchDebounce(binding.inputEditText.text.toString())
        }

        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.showHistory(
                binding.inputEditText.text.toString(),
                binding.inputEditText.hasFocus()
            )
        }

        binding.clearHistoryButton.setOnClickListener {
            tracksListHistory.clear()
            tracksAdapterHistory.notifyDataSetChanged()
            viewModel.clearHistory()
            viewModel.saveHistory()
        }

        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            tracksList.clear()
            tracksAdapter.notifyDataSetChanged()
            closeKeyboard()
            viewModel.showHistory(
                binding.inputEditText.text.toString(),
                binding.inputEditText.hasFocus()
            )

        }

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(binding.inputEditText.text.toString())
                true
            }
            false
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                viewModel.showHistory(
                    changedText = s?.toString() ?: "",
                    binding.inputEditText.hasFocus()
                )

                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        textWatcher.let { binding.inputEditText.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Start -> showStart()
            is TracksState.Content -> showContent(state.tracks)
            is TracksState.Empty -> showEmpty(state.message)
            is TracksState.Error -> showError(state.errorMessage)
            is TracksState.Loading -> showLoading()
            is TracksState.ShowHistory -> showHistoryContent(state.tracks)
        }
    }

    private fun showStart() {
        with(binding) {
            tracksList.visibility = View.GONE
            tracksListHistory.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            textPlaceholderMessage.visibility = View.GONE
            textHistory.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
        }

    }
    private fun showLoading() {
        with(binding){
            tracksList.visibility = View.GONE
            tracksListHistory.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            textPlaceholderMessage.visibility = View.GONE
            textHistory.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        with(binding){
            tracksList.visibility = View.GONE
            tracksListHistory.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            placeholderImage.setBackgroundResource(R.drawable.ic_noethernet)
            textPlaceholderMessage.visibility = View.VISIBLE
            textPlaceholderMessage.text = errorMessage
            textHistory.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            updateButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        tracksList.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showEmpty(emptyMessage: String) {
        with(binding){
            tracksList.visibility = View.GONE
            tracksListHistory.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            placeholderImage.setBackgroundResource(R.drawable.ic_nothing)
            textPlaceholderMessage.visibility = View.VISIBLE
            textPlaceholderMessage.text = emptyMessage
            textHistory.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
        tracksList.clear()
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showContent(tracks: List<Track>) {
        with(binding){
            tracksList.visibility = View.VISIBLE
            tracksListHistory.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            textPlaceholderMessage.visibility = View.GONE
            textHistory.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
        tracksList.clear()
        tracksList.addAll(tracks)
        tracksAdapter.notifyDataSetChanged()
    }

    private fun showHistoryContent(tracks: List<Track>) {
        with(binding){
            tracksListHistory.visibility = View.VISIBLE
            tracksList.visibility =View.GONE
            textHistory.visibility = View.VISIBLE
            clearHistoryButton.visibility =View.VISIBLE
            updateButton.visibility = View.GONE
            textPlaceholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            if (inputEditText.hasFocus() && inputEditText.text?.isEmpty() == true && tracksListHistory.isNotEmpty()) {
                tracksList.recycledViewPool.clear();
                tracksAdapter.notifyDataSetChanged();
            }else{
                tracksListHistory.recycledViewPool.clear();
                tracksAdapterHistory.notifyDataSetChanged();
            }
        }
        tracksListHistory.clear()
        tracksListHistory.addAll(tracks)
        tracksAdapterHistory.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        textWatcher.let { binding.inputEditText.removeTextChangedListener(it) }
        super.onDestroyView()
    }

    private fun closeKeyboard(){
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(requireActivity().window.decorView.windowToken, 0)
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
        private const val CLICK_DEBOUNCE_DELAY = 300L
        const val TRACK_TO_ARRIVE = "track"
    }
}