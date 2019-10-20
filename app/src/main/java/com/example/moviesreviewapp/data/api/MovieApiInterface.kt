package com.example.moviesreviewapp.data.api

import com.example.moviesreviewapp.data.vo.Movie
import com.example.moviesreviewapp.data.vo.MovieDetails
import com.example.moviesreviewapp.data.vo.PopularMovie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {
    //
    @GET("movie/popular")
    fun getPopularMovieList(@Query("page") page:Int):Single<PopularMovie>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int) :Single<MovieDetails>
}