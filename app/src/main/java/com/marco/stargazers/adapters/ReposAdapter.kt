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
import com.marco.stargazers.models.Repo
import kotlinx.android.synthetic.main.item_repo.view.*

/**
 * Created by marco on 22/11/2018
 */

private const val VIEW_ITEM = 0
private const val VIEW_LOADING = 1

class ReposAdapter(private val repos : List<Repo?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener : RepoListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType){
            VIEW_ITEM -> RepoViewHolder(layoutInflater.inflate(R.layout.item_repo, parent, false))
            else -> LoadingViewHolder(layoutInflater.inflate(R.layout.item_loading,parent,false))
        }
    }


    override fun getItemCount(): Int = repos.size

    override fun getItemViewType(position: Int): Int {
        return if (repos[position] == null) VIEW_LOADING else VIEW_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = this.repos[position] ?: return

        (holder as? RepoViewHolder)?.let {
            it.bind(item)
            holder.itemView.setOnClickListener {
                listener?.onRepoSelected(item)
            }
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

    inner class LoadingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

}