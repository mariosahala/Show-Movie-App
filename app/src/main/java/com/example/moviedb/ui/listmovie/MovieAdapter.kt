package com.example.moviedb.ui.listmovie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.models.ListMovieResponse

class MovieAdapter(private val mutableList: MutableList<ListMovieResponse.Result>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallbackMovie


    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMovie: ImageView = itemView.findViewById(R.id.iv_movie_photo)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = mutableList[position]
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        holder.tvTitle.text = movie.originalTitle
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(mutableList[holder.bindingAdapterPosition])
        }
        Glide.with(holder.itemView)
            .load(IMAGE_BASE + movie.posterPath)
            .into(holder.ivMovie)
    }

    override fun getItemCount(): Int = mutableList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallbackMovie) {
        this.onItemClickCallback = onItemClickCallback
    }


    interface OnItemClickCallbackMovie {
        fun onItemClicked(data: ListMovieResponse.Result)
    }
}