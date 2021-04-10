package com.skillbox.flow.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.flow.data.Movie
import com.skillbox.flow.databinding.ItemMovieBinding

class MovieAdapter(
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<MovieViewHolder>() {
    lateinit var movieList: List<Movie>
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binder = ItemMovieBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binder, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movieList[position])
    }

    override fun getItemCount() = movieList.size

    fun updateMovieList(newList: List<Movie>) {
        movieList = newList
    }

    fun getMovieId(adapterPosition: Int): String {
        return movieList[adapterPosition]._id
    }
}
