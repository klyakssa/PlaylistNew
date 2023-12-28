package com.kuzmin.playlist.presentation.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kuzmin.playlist.presentation.library.Fragments.FavoriteFragment
import com.kuzmin.playlist.presentation.library.Fragments.PlaylistFragment

class LibraryViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}