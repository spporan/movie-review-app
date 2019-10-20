package com.example.moviesreviewapp.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviesreviewapp.data.api.FIRST_PAGE
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.vo.Movie
import com.example.moviesreviewapp.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularMovieDataSource (private val apiService:MovieApiInterface ,private val compositeDisposable:
CompositeDisposable):PageKeyedDataSource<Int,Movie> (){

    private var page= FIRST_PAGE
    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovieList(page)
                .subscribeOn(Schedulers.io())
                .subscribe (
                    {
                        callback.onResult(it.movieList,null,page+1)
                        networkState.postValue(NetworkState.LOADED)


                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("PopularData",it.message)
                    }
                )


        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(
            apiService.getPopularMovieList(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        if(it.totalPages>=params.key){
                            callback.onResult(it.movieList,params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                            Log.e("PopularMovieDataSource","End of the List")
                        }

                    },{
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("PopularMovieDataSource",it.message)
                    }
                )

        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }






}