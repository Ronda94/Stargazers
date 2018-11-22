package com.marco.stargazers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

        private val title : TextView = itemView.repo_title
        private val desc : TextView = itemView.repo_desc
        private val language : TextView = itemView.repo_language
        private val stargazers : TextView = itemView.repo_stargazers

        fun bind(repo :Repo){

            title.text = repo.name
            desc.text = repo.description
            language.text = repo.language
            stargazers.text = "${repo.stargazers}"

            desc.visibility = if(repo.description != null) VISIBLE else GONE

        }

    }

}