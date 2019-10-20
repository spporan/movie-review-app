package com.example.moviesreviewapp.data.repository

import android.net.Network

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED
}
class NetworkState (val staus:Status,val msg:String)
{
    companion object{
        val LOADED:NetworkState = NetworkState(Status.SUCCESS,"Success")
        val LOADING:NetworkState = NetworkState(Status.RUNNING,"Running")
        val ERROR:NetworkState = NetworkState(Status.FAILED,"Error")
    }
}