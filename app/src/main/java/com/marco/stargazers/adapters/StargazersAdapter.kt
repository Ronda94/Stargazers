package com.marco.stargazers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marco.stargazers.R
import com.marco.stargazers.models.GitHubUser

/**
 * Created by marco on 23/11/2018
 */
class StargazersAdapter(private val users : List<GitHubUser?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType){
            VIEW_ITEM -> StargazerViewHolder(layoutInflater.inflate(R.layout.item_stargazer, parent, false))
            else -> LoadingViewHolder(layoutInflater.inflate(R.layout.item_loading,parent,false))
        }
    }

    override fun getItemCount(): Int = users.size

    override fun getItemViewType(position: Int): Int {
        return if (users[position] == null) VIEW_LOADING else VIEW_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = this.users[position] ?: return
        (holder as? StargazerViewHolder)?.bind(item)

    }


    inner class StargazerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview){

        fun bind(user : GitHubUser){}

    }


}