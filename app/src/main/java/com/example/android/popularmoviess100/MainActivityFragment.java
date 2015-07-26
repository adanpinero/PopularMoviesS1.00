package com.example.android.popularmoviess100;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView grid; // Grid in fragment_main to show posters
    CustomGridViewAdapter adapter; // The adapter for grid
    Boolean recycledData;
    DataMovies miDataMovies; // An object for my DataMovies
    String preferencesShortFlag;


    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SI", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("SI", "onCreateView");
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_main, container, false); // Create view
        adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter


        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            Log.d("SI", "reciclo");
            miDataMovies = (DataMovies) savedInstanceState.getParcelable("miDataMovies");
            if(miDataMovies.getPoster_AllURL()==savedInstanceState.getStringArrayList("URLs")){
                Log.d("SI","URLs coinciden");            }


        }else{
            miDataMovies=new DataMovies(getActivity(),adapter);
            //miDataMovies.SetAdapter(adapter); //Set adapter to miDataMovies. On postExecute it will refresh adapter with API data
            //adapter.clear();
            //adapter.addAll(miDataMovies.getPoster_AllURL());
            Log.d("SI", "nuevo - no bundle anterior");
        }
        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()){
            adapter.addAll(miDataMovies.getPoster_AllURL());}

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent detail = new Intent(view.getContext(), MovieDetailActivity.class);//Intent for detail view
                //miDataMovies.setCurrentActivity(null);//Quit Activity in myDataMovies
                //miDataMovies.SetAdapter(null); // Quit Adapter in myDataMovies
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

    public void onStart() {
        super.onStart();
        Log.d("SI", "onStart");
        if(miDataMovies.getShortBy()!=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc")){
            //miDataMovies=new DataMovies(getActivity(),adapter);
            Log.d("SI", "onStartCambioPrefs");
            Log.d("SI", "new miDataMovies");
            adapter.clear();
            adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter
            grid.setAdapter(adapter);
            grid.refreshDrawableState();
            miDataMovies=new DataMovies(getActivity(),adapter);

            //grid get adapter en datamovies
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SI", "onResume");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("SI", "onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("miDataMovies", miDataMovies);// coloca el objeto miDataMovies en el Bundle al desaparecer el fragmento
        outState.putString("ShortMode", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc"));
        outState.putStringArrayList("URLs",miDataMovies.getPoster_AllURL());
        preferencesShortFlag=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("shortby_list", "popularity.desc");
        super.onSaveInstanceState(outState);
        Log.d("SI", "guardo");
    }

}

