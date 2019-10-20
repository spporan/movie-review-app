package com.example.moviesreviewapp.ui.popular_movie

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesreviewapp.R
import com.example.moviesreviewapp.data.api.POSTER_BASE_URL
import com.example.moviesreviewapp.data.repository.NetworkState
import com.example.moviesreviewapp.data.vo.Movie
import com.example.moviesreviewapp.ui.move_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.progress_bar_id
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class PopularMoviePageListAdapter (private val context: Context):PagedListAdapter<Movie,RecyclerView.ViewHolder>(MovieCallBackDiff()) {
    val MOVIE_VIEW_TYPE=1
    val NETWORK_VIEW_TYPE=2
     var networkState:NetworkState?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater:LayoutInflater= LayoutInflater.from(parent.context)
        val  view:View

        return if(viewType==MOVIE_VIEW_TYPE){
            view=layoutInflater.inflate(R.layout.recyclerview_item,parent,false)
            MovieListViewHolder(view)
        }else{
            view=layoutInflater.inflate(R.layout.network_state_item,parent,false)
            NetworkStateViewHolder(view)
        }

    }
    private fun hasExtraRow():Boolean{
        return networkState==null && networkState!= NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position==itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position)==MOVIE_VIEW_TYPE){
            (holder as MovieListViewHolder).bindUI(getItem(position),context)
        }else{
            (holder as NetworkStateViewHolder).bind(networkState)
        }


    }

    class MovieCallBackDiff:DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id==newItem.id
        }

    }

    class MovieListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindUI(movie:Movie?, context:Context){
            itemView.popular_movie_name.text="Movie Name :${movie?.title}"
            itemView.popular_release.text="ReleaseDate: ${movie?.releaseDate}"

            val posterUrl= POSTER_BASE_URL+ movie?.posterPath
            Glide.with(context)
                .load(posterUrl)
                .into(itemView.popular_movie_poster)

            itemView.setOnClickListener{
                val intent=Intent(context,SingleMovie::class.java)
                intent.putExtra("id",movie?.id)
                context.startActivity(intent)
            }

        }

    }
    class NetworkStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(networkState:NetworkState?){
            if(networkState!=null && networkState==NetworkState.LOADING){
                itemView.loader_item.visibility=View.VISIBLE
            }else{
                itemView.loader_item.visibility=View.GONE
            }

            if(networkState!=null && networkState== NetworkState.ERROR){
                itemView.error_message_item.visibility=View.VISIBLE
                itemView.error_message_item.text=networkState.msg
            }else if(networkState!=null && networkState== NetworkState.ENDOFLIST){
                itemView.error_message_item.visibility=View.VISIBLE
                itemView.error_message_item.text=networkState.msg
            }else{
                itemView.error_message_item.visibility=View.GONE
            }

        }



    }
    fun setNetworkstate(newNetworkState: NetworkState){
        val previousState:NetworkState?=this.networkState
        val hadExtraRow:Boolean=hasExtraRow()
        this.networkState=newNetworkState
        val hasExtraRow:Boolean=hasExtraRow()

        if(hadExtraRow !=hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }else if(hasExtraRow &&previousState !=networkState){
            notifyItemChanged(itemCount-1)
        }
    }

}