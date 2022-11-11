package com.ihm.dronemobile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihm.dronemobile.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    ITmdbApi tmdbApi = null;
    int mCurrentPage = 1;
    List<PersonData> results = new ArrayList<>();
    PersonListAdapter mPersonListAdapter = null;

    // views
    private final Context mContext = this;
    // private RecyclerView mPopularPersonRv;
    // private TextView mPageNumberTv;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init API client
        if (ApiClient.get() != null)
            tmdbApi = ApiClient.get().create(ITmdbApi.class);
        else
            finish(); // ends activity

        // Current page number text view
        //mPageNumberTv = findViewById(R.id.page_number_tv);
        setPageNumber();

        // set recycler view
        //mPopularPersonRv = findViewById(R.id.popular_person_rv);
        //mPopularPersonRv.setHasFixedSize(true);
        binding.popularPersonRv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this); // use a linear layout manager
        //mPopularPersonRv.setLayoutManager(layoutManager);
        binding.popularPersonRv.setLayoutManager(layoutManager);

        mPersonListAdapter = new PersonListAdapter(results); // attach a custom adapter
        //mPopularPersonRv.setAdapter(mPersonListAdapter);
        binding.popularPersonRv.setAdapter(mPersonListAdapter);

        // call TMDB api
        refreshPopularPersons();
    }

    private void refreshPopularPersons() {
        if (tmdbApi != null) {
            binding.progressWheel.setVisibility(View.VISIBLE);
            Call<PersonPopularResponse> call = tmdbApi.getPersonPopular(ITmdbApi.KEY, String.valueOf(mCurrentPage));
            call.enqueue(new Callback<PersonPopularResponse>() {
                @Override
                public void onResponse(@NonNull Call<PersonPopularResponse> call, @NonNull Response<PersonPopularResponse> response) {
                    results.clear();
                    if (response.code() == 200) {
                        PersonPopularResponse personResponse = response.body();
                        if (personResponse != null && personResponse.getResults() != null) {
                            results.addAll(personResponse.getResults());
                            Log.d(LOG_TAG, "Number of popular person found=" + results.size());
                        }
                    } else {
                        Log.e(LOG_TAG, "HTTP error " + response.code());
                        results.clear();
                        mCurrentPage = 0;
                        Toast toast = Toast.makeText(mContext, R.string.toast_http_error, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    //mPersonListAdapter.notifyDataSetChanged();
                    //mPopularPersonRv.scrollToPosition(0);
                    mPersonListAdapter.notifyItemRangeChanged(0,results.size());
                    binding.popularPersonRv.scrollToPosition(0);
                    setPageNumber();
                    binding.progressWheel.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<PersonPopularResponse> call, @NonNull Throwable t) {
                    Log.e(LOG_TAG, "Call to 'getPersonPopular' failed");
                    results.clear();
                    binding.progressWheel.setVisibility(View.GONE);
                    mCurrentPage = 0;
                    //mPersonListAdapter.notifyDataSetChanged(); // no longer used due to performance warning
                    mPersonListAdapter.notifyItemRangeChanged(0,0);
                    setPageNumber();
                    Toast toast = Toast.makeText(mContext, R.string.toast_network_error, Toast.LENGTH_LONG);
                    toast.show();

                }
            });

        } else {
            Log.e(LOG_TAG, "Api not initialized");
        }
    }

    // helper to update page number caption
    private void setPageNumber() {
        String caption = getResources().getString(R.string.page_number, mCurrentPage);
        //mPageNumberTv.setText(caption);
        binding.pageNumberTv.setText(caption);
    }

    public void onClickPrevious (View v) {
        if (mCurrentPage > 1) {
            mCurrentPage--;
            refreshPopularPersons();
        } else {
            Toast toast = Toast.makeText(this, R.string.toast_no_previous_page, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onClickNext(View view) {
        mCurrentPage++;
        refreshPopularPersons();
    }
}