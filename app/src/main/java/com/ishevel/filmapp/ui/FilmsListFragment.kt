package com.ishevel.filmapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishevel.filmapp.Injection
import com.ishevel.filmapp.data.SearchResult
import com.ishevel.filmapp.databinding.FragmentFilmsListBinding


class FilmsListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmsListBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, Injection.provideFilmsListViewModelFactory(owner = this))
            .get(FilmsListViewModel::class.java)

        binding.bindState(viewModel)

        return binding.root
    }

    private fun FragmentFilmsListBinding.bindState(
        viewModel : FilmsListViewModel
    ) {
        val filmAdapter = FilmAdapter(viewModel::onFilmClicked)
        filmsRecyclerView.adapter = filmAdapter

        bindList(
            adapter = filmAdapter,
            uiState = viewModel.state,
            onScrollChanged = viewModel.accept
        )
    }

    private fun FragmentFilmsListBinding.bindList(
        adapter: FilmAdapter,
        uiState: LiveData<UiState>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        setupScrollListener(onScrollChanged)

        uiState
            .map(UiState::searchResult)
            .distinctUntilChanged()
            .observe(this@FilmsListFragment.viewLifecycleOwner) { result ->
                when (result) {
                    is SearchResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is SearchResult.Error -> {
                        Toast.makeText(
                            this@FilmsListFragment.requireActivity(),
                            "\uD83D\uDE28 Wooops $result.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
    }

    private fun FragmentFilmsListBinding.showEmptyList(show: Boolean) {
        emptyRecyclerView.isVisible = show
        filmsRecyclerView.isVisible = !show
    }

    private fun FragmentFilmsListBinding.setupScrollListener(
        onScrollChanged: (UiAction.Scroll) -> Unit
    ) {
        val layoutManager = filmsRecyclerView.layoutManager as LinearLayoutManager
        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        })
    }
}