package com.ishevel.filmapp.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ishevel.filmapp.data.model.Film

class FavoritesFilmAdapter(private val onClick: (Film) -> Unit) :
    ListAdapter<Film, FilmViewHolder>(FILM_COMPARATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    companion object {
        private val FILM_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem.title == newItem.title &&
                        oldItem.listPosterUrl == newItem.listPosterUrl &&
                        oldItem.averageVote == newItem.averageVote &&
                        oldItem.genre1 == newItem.genre1 &&
                        oldItem.genre2 == newItem.genre2
        }
    }
}