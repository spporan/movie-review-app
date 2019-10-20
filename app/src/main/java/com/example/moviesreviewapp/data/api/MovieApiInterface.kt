package com.example.moviesreviewapp.data.api

import com.example.moviesreviewapp.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MovieApiInterface {
    //https://api.themoviedb.org/3/movie/475557?api_key=2843103b39df8e223fccb35c1f8b7e0a&language=en-US
    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int) :Single<MovieDetails>
}