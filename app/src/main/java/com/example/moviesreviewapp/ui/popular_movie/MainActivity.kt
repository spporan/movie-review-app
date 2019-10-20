package com.example.moviesreviewapp.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesreviewapp.R
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.api.MovieClient
import com.example.moviesreviewapp.data.repository.NetworkState
import com.example.moviesreviewapp.data.repository.PopularMoviePageListRepository
import com.example.moviesreviewapp.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private  lateinit var  viewModel: MainViewModel
    private lateinit var popularMovieRepo:PopularMoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiService=MovieClient.getClient()
        popularMovieRepo= PopularMoviePageListRepository(apiService)
        viewModel=getViewModel()
        val movieAdapter=PopularMoviePageListAdapter(this)
        val gridLay=GridLayoutManager(this,2)

        gridLay.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType=movieAdapter.getItemViewType(position)
                return if(viewType==movieAdapter.MOVIE_VIEW_TYPE) 1
                else 2

            }

        }

        recyclerView.layoutManager=gridLay
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            loader_recycler.visibility=if (viewModel.listEmpty() && it== NetworkState.LOADING) View.VISIBLE else View.GONE
            error_recycler.visibility=if (viewModel.listEmpty() && it== NetworkState.ERROR) View.VISIBLE else View.GONE
            if(!viewModel.listEmpty()){
                movieAdapter.setNetworkstate(it)
            }

        })
    }

    fun getViewModel():MainViewModel{
        return ViewModelProviders.of(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(popularMovieRepo) as T
            }
        })[MainViewModel::class.java]
    }
}
