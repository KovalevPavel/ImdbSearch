package com.skillbox.flow.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.skillbox.flow.R
import com.skillbox.flow.databinding.FragmentMainBinding
import com.skillbox.flow.ui.adapters.MoviePagerAdapter
import com.skillbox.flow.ui.allMovies.AllMoviesFragment
import com.skillbox.flow.ui.search.SearchFragment
import com.skillbox.flow.utils.FragmentViewBinding

class MainFragment : FragmentViewBinding<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private lateinit var pagerAdapter: MoviePagerAdapter

    private lateinit var fragmentList: MutableList<Pair<Fragment, String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragmentList()
        bindPagerAdapter()
    }

    private fun createFragmentList() {
        fragmentList = mutableListOf(
            SearchFragment() to getString(R.string.movie_search),
            AllMoviesFragment() to getString(R.string.all_movies)
        )
    }

    private fun bindPagerAdapter() {
        pagerAdapter = MoviePagerAdapter(this, fragmentList)
        binder.pager.adapter = pagerAdapter
        TabLayoutMediator(binder.tabLayout, binder.pager) { tab, i ->
            tab.text = fragmentList[i].second
        }.attach()
    }
}
