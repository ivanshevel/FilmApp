package com.ishevel.filmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.ishevel.filmapp.databinding.ActivityMainBinding
import com.ishevel.filmapp.ui.FilmDetailsFragmentDirections
import com.ishevel.filmapp.ui.FilmsFavoritesFragmentDirections
import com.ishevel.filmapp.ui.FilmsListFragmentDirections

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener(navListener)
    }

    private val navListener = NavigationBarView.OnItemSelectedListener {

        val fragmentId = findNavController(R.id.nav_host_fragment).currentDestination?.id
        val navController = findNavController(R.id.nav_host_fragment)

        when (it.itemId) {
            R.id.bottom_menu_home -> {
                if (fragmentId == R.id.filmsFavoritesFragment) {
                    val action =
                        FilmsFavoritesFragmentDirections.actionFilmsFavoritesFragmentToFilmsListFragment()
                    navController.navigate(action)
                }
                if (fragmentId == R.id.filmDetailsFragment) {
                    val action =
                        FilmDetailsFragmentDirections.actionFilmDetailsFragmentToFilmsListFragment()
                    navController.navigate(action)
                }
            }
            R.id.bottom_menu_fav -> {
                if (fragmentId == R.id.filmsListFragment ) {
                    val action =
                        FilmsListFragmentDirections.actionFilmsListFragmentToFilmsFavoritesFragment()
                    navController.navigate(action)
                }
                if (fragmentId == R.id.filmDetailsFragment) {
                    val action =
                        FilmDetailsFragmentDirections.actionFilmDetailsFragmentToFilmsFavoritesFragment()
                    navController.navigate(action)
                }
            }
        }
        true
    }
}