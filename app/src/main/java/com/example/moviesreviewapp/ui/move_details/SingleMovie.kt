package com.example.moviesreviewapp.ui.move_details
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.moviesreviewapp.R
import com.example.moviesreviewapp.data.api.MovieApiInterface
import com.example.moviesreviewapp.data.api.MovieClient
import com.example.moviesreviewapp.data.api.POSTER_BASE_URL
import com.example.moviesreviewapp.data.repository.MovieDetailsRepository
import com.example.moviesreviewapp.data.repository.NetworkState
import com.example.moviesreviewapp.data.vo.MovieDetails
import com.example.moviesreviewapp.viewModel.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var repository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)
        val movieId=intent.getIntExtra("id",0)
        val apiService:MovieApiInterface=MovieClient.getClient()
        repository= MovieDetailsRepository(apiService)
        viewModel=getViewModel(movieId)

        viewModel.movieDetails.observe(this,androidx.lifecycle.Observer {
            bindUI(it)
        })
        viewModel.networkState.observe(this,androidx.lifecycle.Observer {

            if (it==NetworkState.LOADING){
                loader_id.visibility=View.VISIBLE
                scrollView.visibility=View.GONE

            }  else {
                scrollView.visibility=View.VISIBLE
                loader_id.visibility=View.GONE

            }
          if(it==NetworkState.ERROR){
              error_id.visibility=View.VISIBLE
              scrollView.visibility=View.GONE
          }else{
              error_id.visibility=View.GONE
              scrollView.visibility=View.VISIBLE
          }




        })


    }


    @SuppressLint("SetTextI18n")
    fun bindUI(it:MovieDetails){

        movie_name.text="Movie Name: ${it.title}"
        movie_sub.text="Movie Subtitle: ${it.tagline}"
        movie_release.text="ReleaseDate: ${it.releaseDate}"
        movie_ratings.text="Rating: ${it.rating}"
        movie_runtime.text="RunTime: ${it.runtime} Minutes"
        movie_overview.text=it.overview
        val numberFormat=NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text="Budget : ${numberFormat.format(it.budget)}"
        movie_revenue.text="Total Revenue :${numberFormat.format(it.revenue)}"
        val moviePosterUrl= POSTER_BASE_URL+it.posterPath

        Glide.with(this)
            .load(moviePosterUrl)
            .into(movie_poster)

    }
    private fun getViewModel(movie_id:Int):SingleMovieViewModel{

        return ViewModelProviders.of(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(repository,movie_id) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
