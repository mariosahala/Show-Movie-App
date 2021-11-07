package com.example.moviedb.ui.genre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityGenreBinding
import com.example.moviedb.models.GenreRespones
import com.example.moviedb.ui.listmovie.MovieActivity
import com.example.moviedb.util.Constant.GENRE_EXTRA
import com.example.moviedb.util.Constant.GENRE_ID_EXTRA
import com.example.moviedb.util.State
import com.example.moviedb.util.setIntPref
import com.example.moviedb.util.setStringPref

class GenreActivity : AppCompatActivity() {
    private lateinit var rvMain: RecyclerView
    private var list: MutableList<GenreRespones.Genre> = mutableListOf()
    private lateinit var viewModel: GenreViewModel
    private lateinit var binding: ActivityGenreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_genre)
        setContentView(binding.root)
        (supportActionBar as ActionBar).title = "GENRE"
        rvMain = binding.rvMain
        rvMain.setHasFixedSize(true)
        setObserve()
    }

    private fun setObserve() {
        viewModel = ViewModelProvider(this)[GenreViewModel::class.java]
        viewModel.setContent()
        viewModel.resultOnSucces.observe(this, {
            when (it.status) {
                State.SUCCESS -> {
                    binding.progressGenre.visibility = View.GONE
                    val data = it.data?.genres
                    data?.let { genre ->
                        list.addAll(genre)
                        setupRecyclerView(list)
                    }
                }
                State.LOADING -> {
                    binding.progressGenre.visibility = View.VISIBLE
                }
                else -> {
                    binding.progressGenre.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun setupRecyclerView(list: MutableList<GenreRespones.Genre>) {
        val genreAdapter = GenreAdapter(list)
        rvMain.adapter = genreAdapter
        rvMain.layoutManager = LinearLayoutManager(this)
        genreAdapter.setOnItemClickCallback(object : GenreAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GenreRespones.Genre) {
                setStringPref(GENRE_EXTRA, data.name)
                setIntPref(GENRE_ID_EXTRA, data.id)
                val intent = Intent(this@GenreActivity, MovieActivity::class.java)
                startActivity(intent)
            }
        })
    }
}