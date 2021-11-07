package com.example.moviedb.ui.listmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.ListMovieResponse
import com.example.moviedb.services.ApiState
import com.example.moviedb.services.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    val resultOnSucces = MutableLiveData<ApiState<ListMovieResponse>>()
    val resultOnFailure = MutableLiveData<String>()

    fun setContent(genreId: Int, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitClient.instance.getMovieList(genreId, page).let {
                    if (it.isSuccessful){
                        resultOnSucces.postValue(ApiState.success(it.body()))
                    }else{
                        resultOnSucces.postValue(ApiState.error(it.message(),null,it.code()))
                    }
                }
            }catch (e:Throwable){

            }
        }
    }
}
