package com.marco.stargazers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.marco.stargazers.R
import com.marco.stargazers.adapters.ReposAdapter
import com.marco.stargazers.models.service.Repo
import com.marco.stargazers.models.service.listRepos
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val repos = mutableListOf<Repo>()
    private var adapter : ReposAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ReposAdapter(repos)

        val linearLayouManager = LinearLayoutManager(this)
        main_recycler.layoutManager = linearLayouManager
        main_recycler.adapter = adapter

        main_send_btn.setOnClickListener {
            listRepos(main_editTxt.text.toString()).enqueue(object : Callback<List<Repo>> {
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
    }
}
