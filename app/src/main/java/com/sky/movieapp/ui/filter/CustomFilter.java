package com.sky.movieapp.ui.filter;

import android.widget.Filter;

import com.sky.movieapp.ui.adapter.MoviesAdapter;
import com.sky.movieapp.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {


    MoviesAdapter moviesAdapter;

    List<MoviesResponse> filterList;


    public CustomFilter(List<MoviesResponse> filterList, MoviesAdapter adapter)
    {
        this.moviesAdapter=adapter;
        this.filterList=filterList;

    }
    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS

            List<MoviesResponse> filteredMovies = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getGenre().toUpperCase().contains(constraint) ||filterList.get(i).getTitle().toUpperCase().contains(constraint) )
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredMovies.add(filterList.get(i));
                }
            }

            results.count=filteredMovies.size();
            results.values=filteredMovies;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        moviesAdapter.movieList = (List<MoviesResponse>)results.values;
        //REFRESH
        moviesAdapter.notifyDataSetChanged();
    }
}
