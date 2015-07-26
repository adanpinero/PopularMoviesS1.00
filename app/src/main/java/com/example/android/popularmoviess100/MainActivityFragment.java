package com.example.android.popularmoviess100;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView grid; // Grid in fragment_main to show posters
    CustomGridViewAdapter adapter; // The adapter for grid
    DataMovies miDataMovies; // An object for my DataMovies
    String preferencesShortFlag;


    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main, container, false); // Create view
        adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter


        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            miDataMovies = savedInstanceState.getParcelable("miDataMovies");
        }else{
            miDataMovies=new DataMovies(getActivity(),adapter);
        }

        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()&& miDataMovies.getPoster_AllURL()!=null){
            adapter.addAll(miDataMovies.getPoster_AllURL());}

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent detail = new Intent(view.getContext(), MovieDetailActivity.class);//Intent for detail view
                detail.putExtra("original_language", miDataMovies.getOriginal_language(position));
                detail.putExtra("original_title", miDataMovies.getOriginal_title(position));
                detail.putExtra("overview", miDataMovies.getOverview(position));
                detail.putExtra("release_date", miDataMovies.getRelease_date(position));
                detail.putExtra("posterURLString", miDataMovies.getURLString(position));
                detail.putExtra("vote_average", miDataMovies.getVote_average(position));
                detail.putExtra("vote_count", miDataMovies.getVote_count(position));
                startActivity(detail); // Launch intent for detail activity
            }
        });
        return rootView;
    }

    public void onStart() {
        super.onStart();
        if(!(miDataMovies.getShortBy().equals(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc")))){
            adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter
            grid.setAdapter(adapter);
            miDataMovies=new DataMovies(getActivity(),adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("miDataMovies", miDataMovies);// coloca el objeto miDataMovies en el Bundle al desaparecer el fragmento
        outState.putString("ShortMode", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc"));
        preferencesShortFlag=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc");
        super.onSaveInstanceState(outState);
    }

}

