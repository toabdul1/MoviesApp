package com.sky.movieapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.sky.movieapp.R;
import com.sky.movieapp.ui.filter.CustomFilter;
import com.sky.movieapp.model.MoviesResponse;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> implements Filterable {

    private Context mContext;


    public List<MoviesResponse> movieList;

  CustomFilter customFilter;
    public MoviesAdapter(Context mContext, List<MoviesResponse> movieList){
        this.mContext = mContext;
        this.movieList = movieList;
    }
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder viewHolder, int i){

        viewHolder.title.setText(movieList.get(i).getTitle());
        viewHolder.genre.setText(movieList.get(i).getGenre());

        String poster = movieList.get(i).getPoster();
        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    @Override
    public Filter getFilter() {
        if(customFilter==null)
        {
            customFilter=new CustomFilter(this.movieList,this);
        }

        return customFilter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, genre;
        public ImageView thumbnail;

        public MyViewHolder(View view){
            super(view);
            //Movie Title
           title = (TextView) view.findViewById(R.id.title);
           //Movie Genre
            genre = (TextView) view.findViewById(R.id.genre);
            //Movie Poster
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);


        }
    }
}
