package com.example.android.popularmoviess100;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    GridView grid; // Grid in fragment_main to show posters
    CustomGridViewAdapter adapter; // The adapter for grid

    public MainActivityFragment() {

    }
    DataMovies miDataMovies; // An object for my DataMovies

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        miDataMovies=new DataMovies(getActivity()); //Create data - empty or from API
        View rootView = inflater.inflate(R.layout.fragment_main, container, false); // Create view
        adapter = new CustomGridViewAdapter(getActivity(), R.id.grid_image, new ArrayList<String>()); // Create adapter
        miDataMovies.SetAdapter(adapter); //Set adapter to miDataMovies. On postExecute it will refresh adapter with API data
        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent detail = new Intent(view.getContext(),MovieDetailActivity.class);//Intent for detail view
                miDataMovies.setCurrentActivity(null);//Quit Activity in myDataMovies
                miDataMovies.SetAdapter(null); // Quit Adapter in myDataMovies
                // Add detail data to intent to use this data in detail view
                detail.putExtra("original_language", miDataMovies.getOriginal_language(position));
                detail.putExtra("original_title", miDataMovies.getOriginal_title(position));
                detail.putExtra("overview", miDataMovies.getOverview(position));
                detail.putExtra("release_date", miDataMovies.getRelease_date(position));
                detail.putExtra("posterURLString", miDataMovies.getURLString(position));
                detail.putExtra("vote_average", miDataMovies.getVote_average(position));
                detail.putExtra("vote_count",miDataMovies.getVote_count(position));
                startActivity(detail); // Launch intent for detail activity
            }
        });

        return rootView;
    }

}

