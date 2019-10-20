package com.example.moviesreviewapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviesreviewapp.data.repository.MovieDetailsRepository
import com.example.moviesreviewapp.data.repository.NetworkState
import com.example.moviesreviewapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel (private val movieDetailsRepository: MovieDetailsRepository,movie_id:Int):ViewModel (){
    private val compositeDisposable=CompositeDisposable()

    val movieDetails:LiveData<MovieDetails> by lazy{
        movieDetailsRepository.fetchingSingleMovieDetails(compositeDisposable,movie_id)

    }
    val networkState:LiveData<NetworkState> by lazy {
        movieDetailsRepository.fetchNetworkstate()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}