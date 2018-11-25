package com.marco.stargazers.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
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
import kotlinx.android.synthetic.main.empty_view.*

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
            if (repos.isEmpty()){
                empty_view.visibility = GONE
                main_recycler.visibility = VISIBLE
            }
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

        initEmptyView()

        adapter = ReposAdapter(repos)
        adapter?.listener = this

        val linearLayouManager = LinearLayoutManager(this)
        main_recycler.layoutManager = linearLayouManager
        main_recycler.setHasFixedSize(true)
        main_recycler.adapter = adapter

        main_send_btn.setOnClickListener {
            it.hideKeyboard()
            clearRepos()
            isLoading = true
            listRepos(main_txt_edit.text.toString(),pageNumber).enqueue(ReposCallBack())
        }

        main_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayouManager.itemCount
                val lastVisibleItem = linearLayouManager.findLastVisibleItemPosition()
                if (!isLoading && lastVisibleItem != RecyclerView.NO_POSITION && totalItemCount <= lastVisibleItem + VISIBLE_TRESHOLD && pageNumber != 0) {
                    isLoading = true
                    listRepos(main_txt_edit.text.toString(), pageNumber).enqueue(ReposCallBack())
                }
            }
        })
    }

    private fun initEmptyView(){
        empty_img.setImageDrawable(getDrawable(R.drawable.happy_octocat))
        empty_txt.setText(R.string.search_user_repo)
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
            showSnackbarMessage(R.string.no_repo_found)
            empty_view.visibility = VISIBLE
            main_recycler.visibility = GONE
        }else {
            empty_view.visibility = GONE
            main_recycler.visibility = VISIBLE
        }
    }

    private fun showSnackbarMessage(localizedId : Int){
        Snackbar.make(main_constr_layout, localizedId, Snackbar.LENGTH_LONG).show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onRepoSelected(repo: Repo) {

        if (repo.stargazers.toInt() == 0){
            showSnackbarMessage(R.string.no_stargazers_found)
            return
        }

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
        }
    }
}
