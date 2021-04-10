package com.skillbox.flow.ui.allMovies

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.skillbox.flow.data.Movie
import com.skillbox.flow.repositories.MainRepository
import com.skillbox.flow.repositories.StoreRepository
import com.skillbox.flow.utils.SingleLiveEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AllMoviesViewModel(application: Application) : AndroidViewModel(application) {
    private val mainRepo = MainRepository(StoreRepository(application))
    private val allMoviesFlow = MutableStateFlow<List<Movie>>(emptyList())
    private val browserEvent = SingleLiveEvent<Intent>()

    val getAllMoviesFlow: StateFlow<List<Movie>>
        get() = allMoviesFlow
    val getBrowserEvent: LiveData<Intent>
        get() = browserEvent

    init {
        mainRepo.observeMovies()
            .onEach {
                allMoviesFlow.emit(it)
            }
            .launchIn(viewModelScope)
    }

    fun navigateToMovie(movieId: String) {
        viewModelScope.launch {
            val browserIntent = mainRepo.getBrowserIntent(movieId)
            browserEvent.postValue(browserIntent)
        }
    }
}
