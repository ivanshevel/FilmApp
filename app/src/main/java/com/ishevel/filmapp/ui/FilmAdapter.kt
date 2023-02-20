package com.ishevel.filmapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ishevel.filmapp.R
import com.ishevel.filmapp.databinding.ItemFilmBinding
import coil.load
import com.ishevel.filmapp.data.model.Film

class FilmAdapter(private val onClick: (Film) -> Unit) :
    PagingDataAdapter<Film, FilmViewHolder>(FILM_COMPARATOR) {

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

class FilmViewHolder(
    binding: ItemFilmBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val title: TextView = binding.titleTextView
    private val poster: ImageView = binding.posterFilmImageView
    private val genre1: TextView = binding.genre1TextView
    private val genre2: TextView = binding.genre2TextView
    private val genreDivider: View = binding.genreDivider
    private val averageVote: TextView = binding.averageVoteTextView
    private val releaseDate: TextView = binding.releaseDateTextView

    private var film: Film? = null

    fun bind(film: Film?) {
        if (film == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
            averageVote.text = resources.getString(R.string.unknown)
            genre1.text = resources.getString(R.string.unknown)
            genre2.text = resources.getString(R.string.unknown)
            releaseDate.text = "?"
        } else {
            showFilmData(film)
        }
    }

    private fun showFilmData(film: Film) {
        this.film = film
        title.text = film.title
        poster.load(film.listPosterUrl)
        averageVote.text = film.rating
        releaseDate.text = film.releaseDate
        if (film.genre1 != null) {
            genre1.visibility = VISIBLE
            genre1.text = film.genre1
        } else {
            genre1.visibility = INVISIBLE
        }
        if (film.genre2 != null) {
            genreDivider.visibility = VISIBLE
            genre2.visibility = VISIBLE
            genre2.text = film.genre2
        } else {
            genreDivider.visibility = INVISIBLE
            genre2.visibility = INVISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup): FilmViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return FilmViewHolder(ItemFilmBinding.inflate(inflater, parent, false))
        }
    }
}