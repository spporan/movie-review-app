package com.example.moviesreviewapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.vo.Movie
import com.example.moviesreviewapp.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable

class DataSourceFactory (private val apiService:MovieApiInterface,private val compositeDisposable: CompositeDisposable):
    DataSource.Factory<Int,Movie>() {

    val moviesLiveDataSoursce =MutableLiveData<PopularMovieDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieDataSource=PopularMovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSoursce.postValue(movieDataSource)
        return movieDataSource
    }
}