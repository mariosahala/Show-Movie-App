package com.example.moviedb.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityDetailBinding
import com.example.moviedb.util.Constant.GENRE_MOVIE_EXTRA
import com.example.moviedb.util.State
import com.example.moviedb.util.getIntPref
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var rvMain: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(binding.root)
        setupObservable()
        movieTrailer()
        reviewMovie()
    }

    private fun setupObservable() {
        val movie = getIntPref(GENRE_MOVIE_EXTRA)
        val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setContent(movie)
        viewModel.resutlOnSucces.observe(this, {
            when (it.status) {
                State.SUCCESS -> {
                    binding.progressDetail.visibility = View.GONE
                    Glide.with(this)
                        .load(IMAGE_BASE + it.data?.posterPath)
                        .transform(CenterCrop())
                        .into(binding.ivPoster)

                    Glide.with(this)
                        .load(IMAGE_BASE + it.data?.backdropPath)
                        .transform(CenterCrop())
                        .into(binding.ivBackdrop)

                    binding.apply {
                        tvDetailMovieTitle.text = it.data?.title
                        tvDetailMovieYear.text = it.data?.releaseDate
                        tvDetailDescription.text = it.data?.overview
                        tvMovieDetailRate.text = it.data?.voteAverage.toString()
                        tvMovieDetailLike.text = it.data?.voteCount.toString()
                    }
                }
                State.LOADING -> {
                    binding.progressDetail.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressDetail.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.resultOnFailure.observe(this, {
            binding.progressDetail.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun movieTrailer() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        val movie = getIntPref(GENRE_MOVIE_EXTRA)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setVideoTrailer(movie)
        viewModel.resultOnFailureVideoTrailer.observe(this, {
            binding.progressDetail.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.resultOnSuccesVideoTrailer.observe(this, { result ->
            when (result.status) {
                State.SUCCESS -> {
                    binding.progressDetail.visibility = View.GONE
                    val youTubePlayerView: YouTubePlayerView =
                        findViewById(R.id.youtube_player_view)
                    lifecycle.addObserver(youTubePlayerView)

                    youTubePlayerView.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            super.onReady(youTubePlayer)
                            if (result.data?.results?.isEmpty()!!) {
                                binding.youtubePlayerView.isEnabled = false
                                binding.youtubePlayerView.isClickable = false
                                binding.youtubePlayerView.visibility = View.INVISIBLE
                            } else {
                                val data = result.data.results[0].key
                                youTubePlayer.cueVideo(data, 0f)
                            }
                        }
                    })
                }
                State.LOADING -> {
                    binding.progressDetail.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressDetail.visibility = View.GONE
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun reviewMovie() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        rvMain = findViewById(R.id.rv_review_movie)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.setHasFixedSize(true)

        val movie = getIntPref(GENRE_MOVIE_EXTRA)
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.setReviewMovie(movie)

        viewModel.resultOnSuccesReview.observe(this, {
            when (it.status) {
                State.SUCCESS -> {
                    if (it.data?.results?.isEmpty() == true) {
                        binding.tvNoReview.visibility = View.VISIBLE
                    }
                    val adapter = it.data?.results?.let { it1 -> ReviewAdapter(it1) }
                    rvMain.adapter = adapter
                }
                State.LOADING -> {
                    binding.progressDetail.visibility = View.VISIBLE
                }
                State.ERROR -> {
                    binding.progressDetail.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.resultOnFailureReview.observe(this, {
            binding.progressDetail.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}