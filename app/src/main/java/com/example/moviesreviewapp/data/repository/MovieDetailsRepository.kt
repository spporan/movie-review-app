package com.example.moviesreviewapp.data.repository

import androidx.lifecycle.LiveData
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable


class MovieDetailsRepository(private val ApiInterface:MovieApiInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource
    fun fetchingSingleMovieDetails(compositeDisposable: CompositeDisposable, movie_id:Int):LiveData<MovieDetails>{
        movieDetailsNetworkDataSource= MovieDetailsNetworkDataSource(ApiInterface,compositeDisposable )
        movieDetailsNetworkDataSource.getMovieData(movie_id)

        return movieDetailsNetworkDataSource.movieDetails
    }

    fun fetchNetworkstate():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}