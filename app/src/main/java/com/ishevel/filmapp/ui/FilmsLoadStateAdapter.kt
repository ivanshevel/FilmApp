package com.ishevel.filmapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishevel.filmapp.R
import com.ishevel.filmapp.databinding.ItemFooterLoadingBinding

class FilmsLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<FilmsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: FilmsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FilmsLoadStateViewHolder {
        return FilmsLoadStateViewHolder.create(parent, retry)
    }
}

class FilmsLoadStateViewHolder(
    private val binding: ItemFooterLoadingBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): FilmsLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer_loading, parent, false)
                val binding = ItemFooterLoadingBinding.bind(view)
                return FilmsLoadStateViewHolder(binding, retry)
            }
        }
}