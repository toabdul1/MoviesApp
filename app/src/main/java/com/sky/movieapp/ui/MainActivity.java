package com.sky.movieapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sky.movieapp.BuildConfig;
import com.sky.movieapp.R;
import com.sky.movieapp.api.NetworkService;
import com.sky.movieapp.ui.adapter.MoviesAdapter;

import com.sky.movieapp.api.Client;
import com.sky.movieapp.api.Service;


import com.sky.movieapp.model.Movies;
import com.sky.movieapp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity /*implements SharedPreferences.OnSharedPreferenceChangeListener*/ {


    private MoviesAdapter adapter;
    private List<MoviesResponse> movieList;

    public ProgressDialog progressDialog;
    NetworkService networkService;
    private SwipeRefreshLayout swipeContainer;
    private AppCompatActivity activity = MainActivity.this;
    public static final String LOG_TAG = MoviesAdapter.class.getName();
    int cacheSize = 10 * 1024 * 1024; // 10 MiB

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.mSearch)
    SearchView searchView;;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();

    }

    public Activity getActivity(){
        Context context = this;
        while (context instanceof ContextWrapper){
            if (context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;

    }

    private void initViews(){
        movieList = new ArrayList<>();
        adapter = new MoviesAdapter(this, movieList);
        networkService=new NetworkService(MainActivity.this);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //SEARCH
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                initViews();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        if(progressDialog ==null) {
            progressDialog = getProgressDialog();
            progressDialog.show();
        }
        networkService.loadJSON();

    }

    ProgressDialog getProgressDialog()
{
    if(progressDialog==null){
    progressDialog = new ProgressDialog(MainActivity.this);
    progressDialog.setMax(100);
    progressDialog.setMessage("Loading Please wait....");
    progressDialog.setTitle("Movies App");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    return progressDialog;

}
else{
        return progressDialog;
}
}
    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/

    /*private void loadJSON(){

        try{


            if (!isNetworkAvailable()){
                Toast.makeText(getApplicationContext(), "Check Internet Settings", Toast.LENGTH_SHORT).show();
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
                return;
            }
            Cache cache = new Cache(getCacheDir(), cacheSize);


            Retrofit retrofit = Client.getClient();
            Service apiService = retrofit.create(Service.class);

            Call<Movies> call = apiService.reposMovies();
            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies movies = response.body();
                    //Collections.sort(movies, Movie.BY_NAME_ALPHABETICAL);
                   movieList=movies.getData();
                    adapter = new MoviesAdapter(getApplicationContext(), movies.getData());
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition(0);
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }


*//*
                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
*//*

                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

public void showView(List<MoviesResponse> movieList){
    adapter = new MoviesAdapter(getApplicationContext(), movieList);
    recyclerView.setAdapter(adapter);
    recyclerView.smoothScrollToPosition(0);
    if(progressDialog!=null && progressDialog.isShowing()) {
        progressDialog.dismiss();
        progressDialog = null;
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (movieList.isEmpty()){
            if(progressDialog==null){
                progressDialog = getProgressDialog();
            progressDialog.show();}
            if(networkService!=null)
                networkService.loadJSON();
            else{
                initViews();
            }
        }
    }


}
