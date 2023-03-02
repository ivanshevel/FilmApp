package com.ishevel.filmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.ishevel.filmapp.databinding.ActivityMainBinding
import com.ishevel.filmapp.ui.FilmsFavoritesFragmentDirections
import com.ishevel.filmapp.ui.FilmsListFragmentDirections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener(navListener)

        onBackPressedDispatcher.addCallback(this, callback)
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
            }
            R.id.bottom_menu_fav -> {
                if (fragmentId == R.id.filmsListFragment ) {
                    val action =
                        FilmsListFragmentDirections.actionFilmsListFragmentToFilmsFavoritesFragment()
                    navController.navigate(action)
                }
            }
        }
        true
    }

    fun changeVisibilityOfBottomNavigation(status: Boolean) {
        binding.bottomNavigation.isVisible = status
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val bottomNav = binding.bottomNavigation as NavigationBarView
            when (findNavController(R.id.nav_host_fragment).previousBackStackEntry?.destination?.id) {
                R.id.filmsListFragment -> {
                    bottomNav.menu.getItem(0).isChecked = true
                }
                R.id.filmsFavoritesFragment -> {
                    bottomNav.menu.getItem(1).isChecked = true
                }
            }
            if (isEnabled) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
            isEnabled = true
        }

    }
}