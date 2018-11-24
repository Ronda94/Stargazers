package com.marco.stargazers.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marco.stargazers.R
import com.marco.stargazers.adapters.StargazersAdapter
import com.marco.stargazers.models.GitHubUser
import com.marco.stargazers.models.Repo
import com.marco.stargazers.service.listStargazers
import kotlinx.android.synthetic.main.activity_stargazers.*
import kotlinx.android.synthetic.main.bordered_image.*
import kotlinx.android.synthetic.main.owner_header.*
import retrofit2.Call
import retrofit2.Response

private const val VISIBLE_TRESHOLD = 5


class StargazersActivity : AppCompatActivity() {

    private lateinit var repo : Repo
    private var adapter : StargazersAdapter? = null
    private var pageNumber : Int = 1

    private val stargazers = mutableListOf<GitHubUser?>()



    private var isLoading : Boolean = false
        set(value) {
            field = value

            if (value){
                stargazers.add(null)
                adapter?.notifyItemInserted(stargazers.lastIndex)
            }else {
                val lastIndex = stargazers.lastIndex
                stargazers.removeAt(lastIndex)
                adapter?.notifyItemRemoved(lastIndex)
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stargazers)

        setSupportActionBar(stargazer_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        stargazers_collapsing.setExpandedTitleColor(Color.TRANSPARENT)
        stargazers_collapsing.setCollapsedTitleTextColor(Color.WHITE)

        adapter = StargazersAdapter(stargazers)

        intent.getParcelableExtra<Repo>(REPO_EXTRA)?.let {
            repo = it
            initHeader()
        } ?: return

        val linearLayouManager = LinearLayoutManager(this)
        stargazers_recycler.layoutManager = linearLayouManager
        stargazers_recycler.setHasFixedSize(true)
        stargazers_recycler.adapter = adapter

        searchStargazers()

        stargazers_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayouManager.itemCount
                val lastVisibleItem = linearLayouManager.findLastVisibleItemPosition()
                if (!isLoading && lastVisibleItem != RecyclerView.NO_POSITION && totalItemCount <= lastVisibleItem + VISIBLE_TRESHOLD && pageNumber != 0) {
                    searchStargazers()
                }
            }
        })

    }

    private fun initHeader(){
        Glide.with(this).load(repo.owner.imageUrl).into(bordered_img)
        header_title.text = repo.owner.name
        header_subtitle.text = repo.name
        stargazers_collapsing.title = repo.name
    }

    private fun searchStargazers(){
        isLoading = true
        listStargazers(repo.owner.name, repo.name, pageNumber).enqueue(StargazersCallBack())
    }

    private fun addStargazers(newStargazers : List<GitHubUser>){
        val lastIndex = stargazers.lastIndex + 1
        stargazers.addAll(newStargazers)
        adapter?.notifyItemRangeInserted(lastIndex, newStargazers.size)
    }

    private fun showNoStargazersView(){

    }


    inner class StargazersCallBack : retrofit2.Callback<List<GitHubUser>> {

        override fun onFailure(call: Call<List<GitHubUser>>, t: Throwable) {
            t.printStackTrace()
            isLoading = false
        }

        override fun onResponse(call: Call<List<GitHubUser>>, response: Response<List<GitHubUser>>) {
            isLoading = false
            val newStargazers = response.body()
            if (newStargazers == null || newStargazers.isEmpty()){
                if (stargazers.isEmpty())
                    showNoStargazersView()
                pageNumber = 0
                return
            }
            pageNumber++
            addStargazers(newStargazers)
        }
    }
}
