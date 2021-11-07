package com.example.moviedb.ui.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.models.GenreRespones

class GenreAdapter(private val mutableList: MutableList<GenreRespones.Genre>) :
    RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class GenreViewHolder (itemView : View):RecyclerView.ViewHolder(itemView) {
        val tvGenre: TextView = itemView.findViewById(R.id.tv_genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = mutableList[position]

        holder.tvGenre.text = genre.name
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(mutableList[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = mutableList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GenreRespones.Genre)
    }
}