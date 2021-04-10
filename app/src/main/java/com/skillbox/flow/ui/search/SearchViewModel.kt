package com.skillbox.flow.ui.search

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.skillbox.flow.data.Movie
import com.skillbox.flow.data.MovieType
import com.skillbox.flow.repositories.MainRepository
import com.skillbox.flow.repositories.StoreRepository
import com.skillbox.flow.utils.SingleLiveEvent
import com.skillbox.flow.utils.loggingDebug
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val mainRepo = MainRepository(StoreRepository(application))
    private var requestJob: Job? = null
    private val loadingStateFlow = MutableStateFlow(false)
    private val moviesStateFlow = MutableStateFlow<List<Movie>>(emptyList())
    private val networkStateFlow = MutableStateFlow(true)
    private val browserEvent = SingleLiveEvent<Intent>()

    private lateinit var _movieTitleFlow: Flow<String>
    private lateinit var _movieTypeFlow: Flow<MovieType>

    val getMoviesStateFlow: StateFlow<List<Movie>>
        get() = moviesStateFlow
    val getLoadingState: StateFlow<Boolean>
        get() = loadingStateFlow
    val getNetworkStateFlow: StateFlow<Boolean>
        get() = networkStateFlow
    val getBrowserEvent: LiveData<Intent>
        get() = browserEvent

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun bindFlows(movieTitleFlow: Flow<String>, movieTypeFlow: Flow<MovieType>) {
        _movieTitleFlow = movieTitleFlow
        _movieTypeFlow = movieTypeFlow

        requestJob?.cancel()
        var searchTitle = ""
        var searchType = MovieType.UNKNOWN

        requestJob = combine(
            movieTypeFlow,
            movieTitleFlow
        ) { movieType, movieTitle ->
            movieTitle to movieType
        }
            .debounce(500L)
            .mapLatest {
                it
            }
            // UI недоступен
            .onEach {
                searchTitle = it.first
                searchType = it.second
                loadingStateFlow.emit(true)
            }
            // запрос на сервер
            .onEach {
                moviesStateFlow.emit(
                    mainRepo.requestMovies(it)
                )
            }
            .catch {
                loggingDebug("$it")
                loadingStateFlow.emit(false)
                if (it is UnknownHostException) {
                    networkStateFlow.emit(false)
                    moviesStateFlow.emit(
                        mainRepo.requestFromDatabase(searchTitle to searchType)
                    )
                }
                bindFlows(movieTitleFlow, movieTypeFlow)
            }
            // UI доступен
            .onEach {
                loadingStateFlow.emit(false)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun getMovieType(radioButtonId: Int): MovieType {
        return mainRepo.getMovieType(radioButtonId)
    }

    fun navigateToMovie(movieId: String) {
        viewModelScope.launch {
            val browserIntent = mainRepo.getBrowserIntent(movieId)
            browserEvent.postValue(browserIntent)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend fun stopLoading() {
        requestJob?.let {
            it.cancel()
            loadingStateFlow.emit(false)
            bindFlows(_movieTitleFlow, _movieTypeFlow)
        }
    }

    override fun onCleared() {
        super.onCleared()
        requestJob?.cancel()
    }
}
