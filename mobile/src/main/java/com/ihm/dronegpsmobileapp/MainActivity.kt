package com.ihm.dronegpsmobileapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.ihm.dronegpsmobileapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    var tmdbApi: ITmdbApi? = null
    var mCurrentPage = 1
    var results: MutableList<PersonData> = ArrayList()
    var mPersonListAdapter: PersonListAdapter? = null

    // views
    private val mContext: Context = this

    // private RecyclerView mPopularPersonRv;
    // private TextView mPageNumberTv;
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // init API client
        if (ApiClient.get() != null) tmdbApi = ApiClient.get()?.create(ITmdbApi::class.java) else finish() // ends activity

        // Current page number text view
        //mPageNumberTv = findViewById(R.id.page_number_tv);
        setPageNumber()

        // set recycler view
        //mPopularPersonRv = findViewById(R.id.popular_person_rv);
        //mPopularPersonRv.setHasFixedSize(true);
        binding!!.popularPersonRv.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this) // use a linear layout manager
        //mPopularPersonRv.setLayoutManager(layoutManager);
        binding!!.popularPersonRv.layoutManager = layoutManager
        mPersonListAdapter = PersonListAdapter(results) // attach a custom adapter
        //mPopularPersonRv.setAdapter(mPersonListAdapter);
        binding!!.popularPersonRv.adapter = mPersonListAdapter

        // call TMDB api
        refreshPopularPersons()
    }

    private fun refreshPopularPersons() {
        if (tmdbApi != null) {
            binding!!.progressWheel.visibility = View.VISIBLE
            val call = tmdbApi!!.getPersonPopular(ITmdbApi.KEY, mCurrentPage.toString())
            call?.enqueue(object : Callback<PersonPopularResponse?> {
                override fun onResponse(call: Call<PersonPopularResponse?>, response: Response<PersonPopularResponse?>) {
                    results.clear()
                    if (response.code() == 200) {
                        val personResponse = response.body()
                        if (personResponse != null && personResponse.results != null) {
                            results.addAll(personResponse.results)
                            Log.d(LOG_TAG, "Number of popular person found=" + results.size)
                        }
                    } else {
                        Log.e(LOG_TAG, "HTTP error " + response.code())
                        results.clear()
                        mCurrentPage = 0
                        val toast = Toast.makeText(mContext, R.string.toast_http_error, Toast.LENGTH_LONG)
                        toast.show()
                    }
                    //mPersonListAdapter.notifyDataSetChanged();
                    //mPopularPersonRv.scrollToPosition(0);
                    mPersonListAdapter!!.notifyItemRangeChanged(0, results.size)
                    binding!!.popularPersonRv.scrollToPosition(0)
                    setPageNumber()
                    binding!!.progressWheel.visibility = View.GONE
                }

                override fun onFailure(call: Call<PersonPopularResponse?>, t: Throwable) {
                    Log.e(LOG_TAG, "Call to 'getPersonPopular' failed")
                    results.clear()
                    binding!!.progressWheel.visibility = View.GONE
                    mCurrentPage = 0
                    //mPersonListAdapter.notifyDataSetChanged(); // no longer used due to performance warning
                    mPersonListAdapter!!.notifyItemRangeChanged(0, 0)
                    setPageNumber()
                    val toast = Toast.makeText(mContext, R.string.toast_network_error, Toast.LENGTH_LONG)
                    toast.show()
                }
            })
        } else {
            Log.e(LOG_TAG, "Api not initialized")
        }
    }

    // helper to update page number caption
    private fun setPageNumber() {
        val caption = resources.getString(R.string.page_number, mCurrentPage)
        //mPageNumberTv.setText(caption);
        binding!!.pageNumberTv.text = caption
    }

    fun onClickPrevious(v: View?) {
        if (mCurrentPage > 1) {
            mCurrentPage--
            refreshPopularPersons()
        } else {
            val toast = Toast.makeText(this, R.string.toast_no_previous_page, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun onClickNext(view: View?) {
        mCurrentPage++
        refreshPopularPersons()
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }
}