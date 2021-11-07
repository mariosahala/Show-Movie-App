package com.example.moviedb.services

import com.example.moviedb.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("3/genre/movie/list?api_key=be8b6c8aa9a5f4e240bb6093f9849051")
    suspend fun getGenreList(): Response<GenreRespones>

    @GET("3/discover/movie?api_key=be8b6c8aa9a5f4e240bb6093f9849051")
    suspend fun getMovieList(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int? = 1
    ): Response<ListMovieResponse>

    @GET("3/movie/{movie_id}?api_key=be8b6c8aa9a5f4e240bb6093f9849051")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int): Response<DetailResponse>

    @GET("3/movie/{movie_id}/videos?api_key=be8b6c8aa9a5f4e240bb6093f9849051")
    suspend fun getVideoTrailer(@Path("movie_id") movieId: Int): Response<VideoTrailerRespones>

    @GET("3/movie/{movie_id}/reviews?api_key=be8b6c8aa9a5f4e240bb6093f9849051")
    suspend fun getReview(@Path("movie_id") movieId: Int): Response<ReviewResponse>


}