package com.example.moviesreviewapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.api.POST_PER_PAGE
import com.example.moviesreviewapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class PopularMoviePageListRepository (private  val apiInterface: MovieApiInterface) {
    lateinit var moviePageList:LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: DataSourceFactory

    fun fetchMoviePageList(compositeDisposable: CompositeDisposable):LiveData<PagedList<Movie>>{
        movieDataSourceFactory= DataSourceFactory(apiInterface,compositeDisposable)
        val config=PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePageList=LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviePageList

    }

    fun getNetworkState():LiveData<NetworkState>{
        return Transformations.switchMap<PopularMovieDataSource,NetworkState>(
            movieDataSourceFactory.moviesLiveDataSoursce,PopularMovieDataSource::networkState
        )

    }
}