package com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.kuzmin.playlist.databinding.FragmentNewplaylistBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.kuzmin.playlist.R
import com.kuzmin.playlist.presentation.audioplayer.PlayerFragment
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.models.CreatePlaylistState
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.AddNewPlaylist.view_models.CreatePlaylistViewModel
import com.kuzmin.playlist.presentation.main.RootActivity
import com.kuzmin.playlist.presentation.mapper.ArtworkMapper.dpToPx
import com.kuzmin.playlist.presentation.models.Track
import com.kuzmin.playlist.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.koin.androidx.viewmodel.ext.android.viewModel


class CreatePlaylist: Fragment() {

    private lateinit var binding: FragmentNewplaylistBinding

    private lateinit var textWatcher: TextWatcher

    private lateinit var textWatcher2: TextWatcher

    private  var uriImg: String = ""

    private lateinit var onBackPlaylistDebounce: (RootActivity) -> Unit

    private val createPlaylistViewModel by viewModel<CreatePlaylistViewModel>()

    private var fromWho: Int? = null

    private var controlPlaylist: ControlPlaylist = ControlPlaylist()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewplaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromWho = arguments?.getInt(FROM_WHO)

        onBackPlaylistDebounce = debounce<RootActivity>(CLICK_DEBOUNCE_DELAY, GlobalScope, Dispatchers.Main) { activity ->
            activity.animateBottomNavigationView(View.VISIBLE)
        }
        binding.back.setOnClickListener {
            when(controlPlaylist.needSave()){
                0 -> {
                    if (fromWho == 1){
                        findNavController().navigateUp()
                    }else{
                        findNavController().navigateUp()
                        onBackPlaylistDebounce(activity as RootActivity)
                    }
                }
                else -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireContext().getString(R.string.dialog_title))
                        .setMessage(requireContext().getString(R.string.dialog_message))
                        .setNeutralButton("Отмена") { dialog, which ->
                            dialog.cancel()
                        }
                        .setPositiveButton("Завершить") { dialog, which ->
                            findNavController().navigateUp()
                            onBackPlaylistDebounce(activity as RootActivity)
                        }
                        .show()
                }
            }
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createBtn.isEnabled =  !isEmpty(s)
                controlPlaylist.namePlaylist = count
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        textWatcher.let { binding.inputEditTextName.addTextChangedListener(it) }

        textWatcher2 = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                controlPlaylist.discribePlaylist = count
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        textWatcher2.let { binding.inputEditTextDiscribe.addTextChangedListener(it) }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext())
                        .load(uri)
                        .centerCrop()
                        .transform(RoundedCorners(dpToPx(8f, requireContext())))
                        .placeholder(R.drawable.ic_placeholder_playlist)
                        .into(binding.cover)
                    controlPlaylist.imgPlaylist = 1
                    uriImg = uri.toString()
                }
            }


        binding.cover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        createPlaylistViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.createBtn.setOnClickListener {
            createPlaylistViewModel.createPlaylist(
                playlistName = binding.inputEditTextName.text.toString(),
                playlistDescribe = binding.inputEditTextDiscribe.text.toString(),
                imgFilePath = uriImg,
            )
        }
    }

    private fun render(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.Error -> {
                Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            }
            is CreatePlaylistState.Success -> {
                Toast.makeText(context, requireContext().getString(R.string.playlist_toast) + " "+ state.playlistName + " " + requireContext().getString(R.string.create_toast), Toast.LENGTH_SHORT).show()
                if (fromWho == 1){
                    findNavController().navigateUp()
                }else{
                    findNavController().navigateUp()
                    onBackPlaylistDebounce(activity as RootActivity)
                }
            }
        }
    }

    private fun isEmpty(s: CharSequence?): Boolean {
        return s.isNullOrEmpty()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 100L
        private const val FROM_WHO = "from"
    }

    class ControlPlaylist(){
        var imgPlaylist: Int = 0
        var namePlaylist: Int = 0
        var discribePlaylist: Int = 0

        fun needSave(): Int{
            return imgPlaylist+namePlaylist+discribePlaylist
        }
    }
}