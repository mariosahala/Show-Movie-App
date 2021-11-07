package com.example.moviedb.ui.genre

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.GenreRespones
import com.example.moviedb.services.ApiState
import com.example.moviedb.services.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreViewModel : ViewModel() {
    val resultOnSucces = MutableLiveData<ApiState<GenreRespones>>()
    val resultOnFailure = MutableLiveData<String>()

    fun setContent() {
        viewModelScope.launch(Dispatchers.IO) {
            resultOnSucces.postValue(ApiState.loading())
            try {
                RetrofitClient.instance.getGenreList().let {
                    if (it.isSuccessful) {
                        resultOnSucces.postValue(ApiState.success(it.body()))
                    } else {
                        resultOnSucces.postValue(ApiState.error(it.message(), null, it.code()))
                    }
                }
            } catch (e: Throwable) {
                resultOnFailure.value = e.message.toString()
                Log.e("Hasil Erorr", e.message.toString())
            }

        }
    }
}
