package com.ishevel.filmapp.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishevel.filmapp.R
import com.ishevel.filmapp.api.model.ApiFilm
import com.ishevel.filmapp.databinding.ItemFilmBinding
import coil.load
import com.ishevel.filmapp.data.model.Film

class FilmAdapter(private val onClick: (Film) -> Unit) :
    ListAdapter<Film, FilmViewHolder>(FILM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        return FilmViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                onClick(item)
                val action = FilmsListFragmentDirections.actionFilmsListFragmentToFilmDetailsFragment()
                val navController = Navigation.findNavController(holder.binding.root)
                navController.navigate(action)
            }
        }
    }

    companion object {
        private val FILM_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean =
                oldItem.title == newItem.title
        }
    }
}

class FilmViewHolder(
    val binding: ItemFilmBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val title: TextView = binding.titleTextView
    private val poster: ImageView = binding.posterFilmImageView
    private val genre1: TextView = binding.genre1TextView
    private val genre2: TextView = binding.genre2TextView
    private val averageVote: TextView = binding.averageVoteTextView

    private var film: Film? = null

    init {
        //binding.root.setOnClickListener { clickListener(absoluteAdapterPosition) }
        /*binding.root.setOnClickListener {

            val action = FilmsListFragmentDirections.actionFilmsListFragmentToFilmDetailsFragment()
            val navController = Navigation.findNavController(binding.root)
            navController.navigate(action)
        }*/
    }

    fun bind(film: Film?) {
        if (film == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
            //description.visibility = View.GONE
            //language.visibility = View.GONE
            averageVote.text = resources.getString(R.string.unknown)
            genre1.text = resources.getString(R.string.unknown)
            genre2.text = resources.getString(R.string.unknown)
        } else {
            showFilmData(film)
        }
    }

    private fun showFilmData(film: Film) {
        this.film = film
        title.text = film.title
        poster.load(film.listPosterUrl)
        genre1.text = film.genre1
        genre2.text = film.genre2
    }

    companion object {
        fun create(parent: ViewGroup): FilmViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return FilmViewHolder(ItemFilmBinding.inflate(inflater, parent, false))
        }
    }
}