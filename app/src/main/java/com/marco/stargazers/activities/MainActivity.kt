package com.marco.stargazers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

private const val VISIBLE_TRESHOLD = 3

class MainActivity : AppCompatActivity() {

    private val repos = mutableListOf<Repo?>()
    private var adapter : ReposAdapter? = null

    private var isLoading : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ReposAdapter(repos)

        val linearLayouManager = LinearLayoutManager(this)
        main_recycler.layoutManager = linearLayouManager
        main_recycler.setHasFixedSize(true)
        main_recycler.adapter = adapter

        main_send_btn.setOnClickListener {
            listRepos(main_editTxt.text.toString(),1).enqueue(object : Callback<List<Repo>> {
                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    Log.e("Non funziona", t.message)
                }

                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    val newRepos = response.body()



                    repos.clear()
                    newRepos?.let { repoList ->
                        repos.addAll(repoList)
                    }
                    adapter?.notifyDataSetChanged()

                    newRepos?.forEach {r ->
                        Log.i("REPO", r.name)
                    }
                }
            })
        }


        main_recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayouManager.itemCount
                val lastVisibleItem = linearLayouManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + VISIBLE_TRESHOLD) {

                    isLoading = true
                    repos.add(null)
                    adapter?.notifyItemInserted(repos.lastIndex)

                    listRepos(main_editTxt.text.toString(), 2).enqueue(object : Callback<List<Repo>> {
                        override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                            Log.e("Non funziona", t.message)
                        }

                        override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                            val newRepos = response.body()

                            if (newRepos == null || newRepos.isEmpty()) {
                                Log.i("STOP RECYCLER", "stoppa di listenare per scrollare brutto")
                                return
                            }

                            newRepos?.let { repoList ->
                                repos.addAll(repoList)
                            }
                            adapter?.notifyDataSetChanged()

                            newRepos?.forEach { r ->
                                Log.i("REPO", r.name)
                            }
                        }
                    })
                }
            }
        })
    }
}
