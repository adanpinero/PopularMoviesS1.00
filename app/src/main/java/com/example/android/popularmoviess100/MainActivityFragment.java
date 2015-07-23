package com.example.android.popularmoviess100;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    Boolean recycledData;


    public MainActivityFragment() {

    }
    DataMovies miDataMovies; // An object for my DataMovies

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SI", "onCreate");
        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            miDataMovies = (DataMovies) savedInstanceState.getParcelable("miDataMovies");
            miDataMovies.setCurrentActivity(getActivity());
            recycledData=true;
            Log.d("SI","reciclo");
        }else{
            miDataMovies=new DataMovies(getActivity());
            recycledData=false;
            Log.d("SI","nuevo");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("SI", "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false); // Create view
        adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter
        miDataMovies.SetAdapter(adapter); //Set adapter to miDataMovies. On postExecute it will refresh adapter with API data
        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        if(recycledData){ // Refresh adapter if recycled Data, if new Data the adapter will refresh when asynk task finish
            adapter.clear();
            adapter.addAll(miDataMovies.getPoster_AllURL());
        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent detail = new Intent(view.getContext(), MovieDetailActivity.class);//Intent for detail view
                miDataMovies.setCurrentActivity(null);//Quit Activity in myDataMovies
                miDataMovies.SetAdapter(null); // Quit Adapter in myDataMovies
                // Add detail data to intent to use this data in detail view
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
  /*  public void onStart() {
        super.onStart();
        Log.d("SI", "onStart");
        //miDataMovies=new DataMovies(getActivity());
        adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>());
        adapter.clear();
        adapter.addAll(miDataMovies.getPoster_AllURL());
        miDataMovies.SetAdapter(adapter);
        grid.setAdapter(adapter);
        //miDataMovies.ActualizaAdapter();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("miDataMovies", miDataMovies);// coloca el objeto miDataMovies en el Bundle al desaparecer el fragmento
        Log.d("SI", "guardo");
    }
}

