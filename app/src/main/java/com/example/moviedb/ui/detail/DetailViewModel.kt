package com.example.moviedb.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.models.DetailResponse
import com.example.moviedb.models.ReviewResponse
import com.example.moviedb.models.VideoTrailerRespones
import com.example.moviedb.services.ApiState
import com.example.moviedb.services.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    val resutlOnSucces = MutableLiveData<ApiState<DetailResponse>>()
    val resultOnFailure = MutableLiveData<String>()
    val resultOnSuccesVideoTrailer = MutableLiveData<ApiState<VideoTrailerRespones>>()
    val resultOnFailureVideoTrailer = MutableLiveData<String>()
    val resultOnSuccesReview = MutableLiveData<ApiState<ReviewResponse>>()
    val resultOnFailureReview = MutableLiveData<String>()

    fun setContent(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            resutlOnSucces.postValue(ApiState.loading())
            try {
                RetrofitClient.instance.getMovieDetail(movieId).let {
                    if (it.isSuccessful) {
                        resutlOnSucces.postValue(ApiState.success(it.body()))
                    } else {
                        resutlOnSucces.postValue(ApiState.error(it.message(), null, it.code()))
                    }
                }

            } catch (e: Throwable) {
                resultOnFailure.postValue(e.message)
            }
        }
    }

    fun setVideoTrailer(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            resultOnSuccesVideoTrailer.postValue(ApiState.loading())
            try {
                RetrofitClient.instance.getVideoTrailer(movieId).let {
                    if (it.isSuccessful) {
                        resultOnSuccesVideoTrailer.postValue(ApiState.success(it.body()))
                    } else {
                        resultOnSuccesVideoTrailer.postValue(
                            ApiState.error(
                                it.message(),
                                null,
                                it.code()
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                resultOnFailureVideoTrailer.postValue(e.message)
            }
        }
    }

    fun setReviewMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            resultOnSuccesReview.postValue(ApiState.loading())
            try {
                RetrofitClient.instance.getReview(movieId).let {
                    if (it.isSuccessful) {
                        resultOnSuccesReview.postValue(ApiState.success(it.body()))
                    } else {
                        resultOnSuccesReview.postValue(
                            ApiState.error(
                                it.message(),
                                null,
                                it.code()
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                resultOnFailureReview.postValue(e.message)
            }

        }
    }
}