package com.example.moviedb.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviedb.R
import com.example.moviedb.models.ReviewResponse

class ReviewAdapter(private val mutableList: MutableList<ReviewResponse.Result>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNameUser = itemView.findViewById<TextView>(R.id.tvNameReview)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescriptionReview)
        val ivImageUser = itemView.findViewById<ImageView>(R.id.ivIconUserReview)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewAdapter.ReviewViewHolder {
        return ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        val review = mutableList[position]
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

        holder.tvNameUser.text = review.author
        holder.tvDescription.text = review.content

        Glide.with(holder.itemView)
            .load(IMAGE_BASE + review.authorDetails.avatarPath)
            .into(holder.ivImageUser)
    }

    override fun getItemCount(): Int = mutableList.size
}