package com.sky.movieapp.api;

import com.sky.movieapp.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/api/movies")
    Call<Movies> reposMovies();
}
