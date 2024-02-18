package com.kuzmin.playlist.presentation.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kuzmin.playlist.presentation.library.Fragments.Favorite.FavoriteFragment
import com.kuzmin.playlist.presentation.library.Fragments.Playlist.PlaylistFragment

class LibraryViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}