package com.sky.movieapp.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.sky.movieapp.model.Movies;
import com.sky.movieapp.ui.MainActivity;
import com.sky.movieapp.ui.adapter.MoviesAdapter;

import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by torah on 04-02-2018.
 */

public class NetworkService {
    private Context context;
    Client client;
    public NetworkService(Context context)
    {
        this.context = context;
    }
    public void loadJSON(){

        try{
            client = new Client(context);

           /* if (!client.isNetworkAvailable()){
                Toast.makeText(context.getApplicationContext(), "Check Internet Settings", Toast.LENGTH_SHORT).show();
                if(((MainActivity)(context)).progressDialog!=null && ((MainActivity)(context)).progressDialog.isShowing()) {
                    ((MainActivity)(context)).progressDialog.dismiss();
                    ((MainActivity)(context)).progressDialog = null;
                }
                return;
            }*/
            //Cache cache = new Cache(getCacheDir(), cacheSize);


           // Retrofit retrofit = client.getClient(); // Normall call without cache
            Retrofit retrofit = client.getRetrofit(); // with Cache offline
            Service apiService = retrofit.create(Service.class);

            Call<Movies> call = apiService.reposMovies();
            call.enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    Movies movies = response.body();


                    ((MainActivity)context).showView(movies.getData());


/*
                    if (swipeContainer.isRefreshing()){
                        swipeContainer.setRefreshing(false);
                    }
*/

                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    if(((MainActivity)context).progressDialog.isShowing()) {
                        ((MainActivity)context).progressDialog.dismiss();
                        ((MainActivity)context).progressDialog = null;
                    }
                    Toast.makeText(context, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
