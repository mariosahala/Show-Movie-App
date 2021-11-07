package com.example.moviedb.ui.listmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityMovieBinding
import com.example.moviedb.models.ListMovieResponse
import com.example.moviedb.ui.detail.DetailActivity
import com.example.moviedb.util.Constant.GENRE_EXTRA
import com.example.moviedb.util.Constant.GENRE_ID_EXTRA
import com.example.moviedb.util.Constant.GENRE_MOVIE_EXTRA
import com.example.moviedb.util.State
import com.example.moviedb.util.getIntPref
import com.example.moviedb.util.getStringPref
import com.example.moviedb.util.setIntPref

class MovieActivity : AppCompatActivity() {
    private lateinit var rvMovie: RecyclerView
    private var list: ArrayList<ListMovieResponse.Result> = arrayListOf()
    private lateinit var viewModel: MovieViewModel
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var binding : ActivityMovieBinding
    var page = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_movie)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val genreName = getStringPref(GENRE_EXTRA)
        (supportActionBar as ActionBar).title = genreName

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        rvMovie = binding.rvMovie
        rvMovie.setHasFixedSize(true)

        val movieAdapter = MovieAdapter(list)
        rvMovie.adapter = movieAdapter
        gridLayoutManager = GridLayoutManager(this, 2)
        rvMovie.layoutManager = gridLayoutManager
        movieAdapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallbackMovie {
            override fun onItemClicked(data: ListMovieResponse.Result) {
                setIntPref(GENRE_MOVIE_EXTRA, data.id)
                startActivity(Intent(this@MovieActivity, DetailActivity::class.java))
            }
        })
        pagination()
        val genreID = getIntPref(GENRE_ID_EXTRA)
        viewModel.setContent(genreID, 1)
        viewModel.resultOnSucces.observe(this, {
            when (it.status) {
                State.SUCCESS -> {
                    binding.progressMovie.visibility = View.GONE
                    val data = it.data?.results
                    data?.let { movie->
                        list.addAll(movie)
                        movieAdapter.notifyDataSetChanged()
                    }
                }
                State.LOADING -> {
                    binding.progressMovie.visibility = View.VISIBLE
                }
                else-> {
                    binding.progressMovie.visibility = View.GONE
                    Toast.makeText(this,it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

        private fun pagination() {
        var totalItemCount: Int
        val genreID = getIntPref(GENRE_ID_EXTRA)

        rvMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                totalItemCount = gridLayoutManager.itemCount
                val lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition()

                if (totalItemCount - 1 == lastVisibleItem) {
                    viewModel.setContent(genreID, page++)
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
