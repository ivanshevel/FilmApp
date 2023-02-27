package com.ishevel.filmapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.ishevel.filmapp.Injection
import com.ishevel.filmapp.databinding.FragmentFilmsListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FilmsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmsListBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(owner = this, requireActivity().applicationContext))
            .get(FilmsListViewModel::class.java)

        binding.bindRecyclerView(viewModel)
        observeFilmSelected(viewModel)

        return binding.root
    }

    private fun FragmentFilmsListBinding.bindRecyclerView(
        viewModel: FilmsListViewModel
    ) {
        val filmAdapter = FilmAdapter(viewModel::onFilmClicked)
        filmsRecyclerView.adapter = filmAdapter.withLoadStateFooter(
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
                val isListEmpty = loadState.mediator?.refresh is LoadState.NotLoading && filmAdapter.itemCount == 0
                filmsRecyclerView.isVisible = !isListEmpty
                progressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
                retryButton.isVisible = (loadState.mediator?.refresh is LoadState.Error && loadState.source.refresh is LoadState.NotLoading && filmAdapter.itemCount == 0)
                errorMsg.text = (loadState.mediator?.refresh as? LoadState.Error)?.error?.message
                errorMsg.isVisible = (loadState.mediator?.refresh is LoadState.Error && loadState.source.refresh is LoadState.NotLoading && filmAdapter.itemCount == 0)
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