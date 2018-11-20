package com.marco.stargazers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.marco.stargazers.models.service.GitHubRequest
import com.marco.stargazers.models.service.Repo
import com.marco.stargazers.models.service.gitHubService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            //chiama le api github
            gitHubService.listRepos(editText.text.toString()).enqueue(object : Callback<List<Repo>> {
                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    Log.e("Non funziona", t.message)
                }

                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    val repos = response.body()
                    repos?.forEach {r ->
                        Log.i("REPO", r.name)
                    }
                }
            })
        }
    }
}
