package com.ishevel.filmapp.ui

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import com.ishevel.filmapp.R
import com.ishevel.filmapp.data.FilmData
import com.ishevel.filmapp.databinding.FragmentFilmDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmDetailsFragment : Fragment() {

    private val viewModel: FilmDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)

        with(binding) {
            val loader = ImageLoader(requireContext())
            viewModel.selectedFilm.observe(viewLifecycleOwner) {
                when (it) {
                    is FilmData.Ok -> processOk(it, loader)
                    is FilmData.Error -> processError()
                }
            }
        }

        binding.bindCastRecyclerView(viewModel)

        return binding.root
    }

    private fun FragmentFilmDetailsBinding.bindCastRecyclerView(
        viewModel: FilmDetailsViewModel
    ) {
        val actorAdapter = ActorAdapter()

        actorsRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            val rightOffset = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                24f,
                requireContext().resources.displayMetrics
            ).toInt()

            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = rightOffset
            }
        })
        actorsRecyclerView.adapter = actorAdapter.withLoadStateHeaderAndFooter(
            header = FilmsLoadStateAdapter { actorAdapter.retry() },
            footer = FilmsLoadStateAdapter { actorAdapter.retry() }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.actorsFlow.collectLatest { pagingData ->
                actorAdapter.submitData(pagingData)
            }
        }
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
                .data(backdropUrl)
                .allowHardware(false)
                .target { result ->
                    posterImageView.setImageDrawable(result)
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