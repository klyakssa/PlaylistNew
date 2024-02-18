package com.kuzmin.playlist.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kuzmin.playlist.R
import com.kuzmin.playlist.databinding.ActivityLibraryBinding
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.FavoriteFragment

class LibraryFragment : Fragment() {

    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = LibraryViewPagerAdapter(requireActivity())


        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorites)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

    }

    override fun onResume() {
        super.onResume()
        //binding.tabLayout.getTabAt(0)?.select() // при возврате на library не знаю как сделать проверку на добавившиеся треки в базе
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}