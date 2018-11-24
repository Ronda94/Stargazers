package com.marco.stargazers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import com.marco.stargazers.R
import com.marco.stargazers.adapters.ReposAdapter
import com.marco.stargazers.models.Repo
import com.marco.stargazers.service.listRepos
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.marco.stargazers.interfaces.RepoListener

private const val VISIBLE_TRESHOLD = 3
const val REPO_EXTRA = "repoExtra"

class MainActivity : AppCompatActivity(), RepoListener {

    private val repos = mutableListOf<Repo?>()
    private var adapter : ReposAdapter? = null
    private var pageNumber : Int = 1

    private var isLoading : Boolean = false
    set(value) {
        field = value

        if (value){
            repos.add(null)
            adapter?.notifyItemInserted(repos.lastIndex)
        }else {
            val lastIndex = repos.lastIndex
            repos.removeAt(lastIndex)
            adapter?.notifyItemRemoved(lastIndex)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ReposAdapter(repos)
        adapter?.listener = this

        val linearLayouManager = LinearLayoutManager(this)
        main_recycler.layoutManager = linearLayouManager
        main_recycler.setHasFixedSize(true)
        main_recycler.adapter = adapter

        main_send_btn.setOnClickListener {
            clearRepos()
            isLoading = true
            listRepos(main_editTxt.text.toString(),pageNumber).enqueue(ReposCallBack())
        }

        main_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayouManager.itemCount
                val lastVisibleItem = linearLayouManager.findLastVisibleItemPosition()
                if (!isLoading && lastVisibleItem != RecyclerView.NO_POSITION && totalItemCount <= lastVisibleItem + VISIBLE_TRESHOLD && pageNumber != 0) {
                    isLoading = true
                    listRepos(main_editTxt.text.toString(), pageNumber).enqueue(ReposCallBack())
                }
            }
        })
    }

    private fun initEmptyView(){

    }

    private fun addRepos(newRepos : List<Repo>){
        val lastIndex = repos.lastIndex + 1
        repos.addAll(newRepos)
        adapter?.notifyItemRangeInserted(lastIndex, newRepos.size)
    }

    private fun clearRepos(){
        pageNumber = 1
        repos.clear()
        adapter?.notifyDataSetChanged()
    }

    private fun checkRepos(newRepos: List<Repo>?){

        if (!newRepos.isNullOrEmpty()) {
            addRepos(newRepos)
            pageNumber++
        }else
            pageNumber = 0

        if (repos.isEmpty()){
            Snackbar.make(main_recycler, R.string.no_repo_found, Snackbar.LENGTH_LONG).show()
            empty_view.visibility = VISIBLE
            main_recycler.visibility = GONE
        }else {
            empty_view.visibility = GONE
            main_recycler.visibility = VISIBLE
        }


//
//        if (!hasNewRepo && repos.isEmpty()){
//
//        }
//
//        if (newRepos == null || newRepos.isEmpty()){
//            if (repos.isEmpty()) {
//                Snackbar.make(main_recycler, R.string.no_repo_found, Snackbar.LENGTH_LONG).show()
//                empty_view.visibility = VISIBLE
//                main_recycler.visibility = GONE
//            }
//            pageNumber = 0
//            return
//        }
//
//
//        pageNumber++
//        addRepos(newRepos)
    }

    override fun onRepoSelected(repo: Repo) {
        val intent = Intent(this, StargazersActivity::class.java).apply {
            putExtra(REPO_EXTRA, repo)
        }
        startActivity(intent)
    }


    inner class ReposCallBack : Callback<List<Repo>>{

        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
            t.printStackTrace()
            isLoading = false
        }

        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
            isLoading = false
            val newRepos = response.body()
            checkRepos(newRepos)
//            if (newRepos == null || newRepos.isEmpty()){
//                if (repos.isEmpty())
//                    Snackbar.make(main_recycler, R.string.no_repo_found, Snackbar.LENGTH_LONG).show()
//                pageNumber = 0
//                return
//            }
//            pageNumber++
//            addRepos(newRepos)
        }
    }
}
