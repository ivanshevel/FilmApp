package com.ishevel.filmapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ishevel.filmapp.data.model.Actor
import com.ishevel.filmapp.databinding.ItemActorBinding

class ActorAdapter :
    PagingDataAdapter<Actor, ActorViewHolder>(ACTOR_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ActorViewHolder {
        return ActorViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val ACTOR_COMPARATOR = object : DiffUtil.ItemCallback<Actor>() {
            override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean =
                oldItem.name == newItem.name &&
                        oldItem.profilePath == newItem.profilePath
        }
    }
}

class ActorViewHolder(
    binding: ItemActorBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val actorImage: ImageView = binding.actorImageView
    private val actorName: TextView = binding.actorNameTextView

    fun bind(actor: Actor) {
        actorName.text = actor.name
        actorImage.load(actor.profilePath)
    }

    companion object {
        fun create(parent: ViewGroup): ActorViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ActorViewHolder(ItemActorBinding.inflate(inflater, parent, false))
        }
    }
}