package com.example.moviesreviewapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviesreviewapp.data.repository.NetworkState
import com.example.moviesreviewapp.data.repository.PopularMoviePageListRepository
import com.example.moviesreviewapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MainViewModel(private  val movieListRepo:PopularMoviePageListRepository) :ViewModel() {
    private val compositeDisposable=CompositeDisposable()

    val moviePagedList:LiveData<PagedList<Movie>> by lazy {
        movieListRepo.fetchMoviePageList(compositeDisposable)
    }
    val networkState:LiveData<NetworkState> by lazy {
        movieListRepo.getNetworkState()
    }

    fun listEmpty():Boolean{

        return moviePagedList.value?.isEmpty()?:true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}