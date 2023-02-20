package com.ishevel.filmapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.ishevel.filmapp.databinding.FragmentFilmsListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmsListFragment : Fragment() {

    private val viewModel: FilmsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmsListBinding.inflate(inflater, container, false)

        binding.bindRecyclerView(viewModel)
        observeFilmSelected(viewModel)

        return binding.root
    }

    private fun FragmentFilmsListBinding.bindRecyclerView(
        viewModel: FilmsListViewModel
    ) {
        val filmAdapter = FilmAdapter(viewModel::onFilmClicked)
        filmsRecyclerView.adapter = filmAdapter.withLoadStateHeaderAndFooter(
            header = FilmsLoadStateAdapter { filmAdapter.retry() },
            footer = FilmsLoadStateAdapter { filmAdapter.retry() }
        )

        retryButton.setOnClickListener { filmAdapter.retry() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filmsFlow.collectLatest { pagingData ->
                filmAdapter.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            filmAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty = loadState.refresh is LoadState.NotLoading && filmAdapter.itemCount == 0
                filmsRecyclerView.isVisible = !isListEmpty
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorMsg.text = (loadState.refresh as? LoadState.Error)?.error?.message
                errorMsg.isVisible = loadState.source.refresh is LoadState.Error

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        activity,
                        "\uD83D\uDE28 Whoops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun observeFilmSelected(viewModel: FilmsListViewModel) {
        viewModel.filmSelected.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                val action =
                    FilmsListFragmentDirections.actionFilmsListFragmentToFilmDetailsFragment()
                findNavController().navigate(action)
            }
        }
    }
}