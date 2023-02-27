package com.ishevel.filmapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ishevel.filmapp.Injection
import com.ishevel.filmapp.databinding.FragmentFilmsFavoritesBinding

class FilmsFavoritesFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        val binding = FragmentFilmsFavoritesBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(owner = this, requireActivity().applicationContext))
            .get(FilmFavoritesViewModel::class.java)

        binding.bindRecyclerView(viewModel)

        return binding.root
    }

    private fun FragmentFilmsFavoritesBinding.bindRecyclerView(
        viewModel: FilmFavoritesViewModel
    ) {
        val filmAdapter = FavoritesFilmAdapter(viewModel::onFilmClicked)
        filmsFavoritesRecyclerView.adapter = filmAdapter
        filmsFavoritesRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.filmFavoritesFlow.observe(viewLifecycleOwner) { films ->
            films.let { filmAdapter.submitList(it) }
        }
    }
}