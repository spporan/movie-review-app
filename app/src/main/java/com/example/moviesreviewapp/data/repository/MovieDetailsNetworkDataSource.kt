package com.example.moviesreviewapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsNetworkDataSource(private val apiInterface: MovieApiInterface,private val compositeDisposable:CompositeDisposable) {
    private val _networkState=MutableLiveData<NetworkState>()
    val networkState:LiveData<NetworkState>
        get()=_networkState

    private val _downloadedMovieData=MutableLiveData<MovieDetails>()
    val movieDetails:LiveData<MovieDetails>
        get()=_downloadedMovieData

    fun getMovieData(movie_id:Int){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiInterface.getMovieDetails(movie_id)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedMovieData.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsNetwork",it.message)
                        }
                    )
            )

        }catch (e:Exception ){
            Log.e("MovieDetailsNetwork",e.message)
        }
    }

}