package com.skillbox.flow.ui.search

import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.flow.R
import com.skillbox.flow.databinding.FragmentSearchBinding
import com.skillbox.flow.ui.adapters.DividerDecorator
import com.skillbox.flow.ui.adapters.MovieAdapter
import com.skillbox.flow.utils.FragmentViewBinding
import com.skillbox.flow.utils.buttonChangeFlow
import com.skillbox.flow.utils.textChangedFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchFragment : FragmentViewBinding<FragmentSearchBinding>(FragmentSearchBinding::inflate) {
    private val searchViewModel: SearchViewModel by viewModels()
    private var _movieAdapter: MovieAdapter? = null
    private val movieAdapter: MovieAdapter
        get() = _movieAdapter!!

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()
        bindUI()
        observeViewModel()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun bindUI() {
        _movieAdapter = MovieAdapter { adapterPosition ->
            val movieId = movieAdapter.getMovieId(adapterPosition)
            searchViewModel.navigateToMovie(movieId)
        }

        binder.apply {
            recView.adapter = movieAdapter
            recView.layoutManager = LinearLayoutManager(requireContext())
            recView.addItemDecoration(DividerDecorator(requireContext()))

            val radioButtonsFlow = radioGroup
                .buttonChangeFlow()
                .onStart {
                    emit(
                        searchViewModel.getMovieType(binder.radioGroup.checkedRadioButtonId)
                    )
                }

            val textWatcherFlow = editText
                .textChangedFlow()

            searchViewModel.bindFlows(textWatcherFlow, radioButtonsFlow)
            btnStop.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    searchViewModel.stopLoading()
                }
            }
        }
    }

    private fun observeViewModel() {
        searchViewModel.apply {
            getMoviesStateFlow.asLiveData().observe(viewLifecycleOwner) { newList ->
                movieAdapter.updateMovieList(newList)
                movieAdapter.notifyDataSetChanged()
                if (newList.isEmpty()) binder.textEmpty.isVisible = true
            }
            getLoadingState.asLiveData().observe(viewLifecycleOwner) { isLoading ->
                enableUI(isLoading)
            }
            getNetworkStateFlow.asLiveData().observe(viewLifecycleOwner) { networkState ->
                if (!networkState)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_network_toast),
                        Toast.LENGTH_LONG
                    ).show()
            }
            getBrowserEvent.observe(viewLifecycleOwner) { intent ->
                requireContext().startActivity(intent)
            }
        }
    }

    private fun enableUI(isLoading: Boolean) {
        binder.apply {
            if (isLoading)
                textEmpty.isVisible = false
            editText.isEnabled = !isLoading
            radioGroup.children.forEach {
                it.isEnabled = !isLoading
            }

            recView.visibility = if (isLoading.not()) View.VISIBLE else View.GONE
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnStop.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _movieAdapter = null
    }
}
