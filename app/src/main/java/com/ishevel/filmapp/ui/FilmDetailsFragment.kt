package com.ishevel.filmapp.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.ishevel.filmapp.Injection
import com.ishevel.filmapp.R
import com.ishevel.filmapp.data.FilmData
import com.ishevel.filmapp.databinding.FragmentFilmDetailsBinding


class FilmDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, Injection.provideFilmDetailsViewModelFactory(owner = this))
            .get(FilmDetailsViewModel::class.java)

        with(binding) {
            val loader = ImageLoader(requireContext())
            viewModel.selectedFilm.observe(viewLifecycleOwner) {
                Log.d("2/Selected film is: ","${it.toString()}")
                when (it) {
                    is FilmData.Ok -> processOk(it, loader)
                    is FilmData.Error -> processError()
                }
            }
        }

        return binding.root
    }

    private fun FragmentFilmDetailsBinding.processError() {
        posterImageView.load(R.drawable.no_film)
        titleTextView.text = getString(R.string.unexpected_outcome)
        genresTextView.text = getString(R.string.disaster)
        averageVoteTextView.text = getString(R.string.page_not_found)
    }

    private fun FragmentFilmDetailsBinding.processOk(
        ok: FilmData.Ok,
        loader: ImageLoader
    ) {
        ok.film.run {
            val request = ImageRequest.Builder(requireContext())
                .data(listPosterUrl) // demo link
                .allowHardware(false)
                .target { result ->
                    posterImageView.setImageDrawable(result)
                    val bitmap = (result as BitmapDrawable).bitmap
                    Palette.from(bitmap).generate { palette ->
                        palette?.vibrantSwatch?.let { swatch ->
                            root.setBackgroundColor(swatch.rgb)
                        }
                    }
                }
                .build()
            loader.enqueue(request)
            titleTextView.text = title
            genresTextView.text = if (genre1 != null) {
                if (genre2 != null) {
                    "$genre1, $genre2"
                } else {
                    genre1
                }
            } else ""
            averageVoteTextView.text = rating
            plotTextView.text = overview
        }
    }
}