package com.marco.stargazers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marco.stargazers.R
import com.marco.stargazers.interfaces.RepoListener
import com.marco.stargazers.models.service.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

/**
 * Created by marco on 22/11/2018
 */

class ReposAdapter(private val repos : List<Repo>) : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {

    var listener : RepoListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposAdapter.RepoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RepoViewHolder(layoutInflater.inflate(R.layout.item_repo, parent, false))
    }


    override fun getItemCount(): Int = repos.size


    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {

        val item = this.repos[position]

        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener?.onRepoSelected(item)
        }

    }

    inner class RepoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val title : TextView = itemView.repo_title

        fun bind(repo :Repo){
            title.text = repo.name
        }

    }

}