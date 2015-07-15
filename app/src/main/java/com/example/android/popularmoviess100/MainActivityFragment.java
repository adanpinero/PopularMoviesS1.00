package com.example.android.popularmoviess100;

import android.app.ProgressDialog;
import android.content.Context;
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

    public ProgressDialog enProgreso; //Object to use during connection to Internet
    GridView grid;
    String[] web = {
            "Google",

    } ;
    int[] imageId = {
            R.drawable.image1
    };
    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("miPID", "");
        PopularMoviesApiRequest miPOPRequest = new PopularMoviesApiRequest();
        miPOPRequest.execute();
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        CustomGrid adapter = new CustomGrid(getActivity(), web, imageId);
        grid=(GridView)rootView.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });

        return rootView;
    }

    private class PopularMoviesApiRequest extends AsyncTask<Void, Void, String> {
        public String[] popularMovies;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create and show Connecting to Internet message / ProgressDialog during doInBackGround
            enProgreso = new ProgressDialog(getActivity());
            enProgreso.setIndeterminate(true);
            enProgreso.setMessage("Conecting to Internet");
            enProgreso.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            final String LOG_TAG = getClass().getName();
            final String API_KEY = "764e1a37d810cfc4ef5be76e31f7b238";
            final String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SHORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                // Creo la URL
                Uri builtUri = Uri.parse(FORECAST_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(SHORT_PARAM, "popularity.desc")
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.d("DREAL", url.toString());

                // Create the request to TheMovieDbAPI, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                return forecastJsonStr;

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String forecastJsonStr) {
            super.onPostExecute(forecastJsonStr);
            if (forecastJsonStr != null) {
                try {
                    getPopularMoviesFromJson(forecastJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            enProgreso.dismiss(); //Close Connectiong to internet message
        }

        //todo implementar metodo que conje el JSON bruto y devuelve lo que necesito
        private void getPopularMoviesFromJson(String forecastJsonStr)
                throws JSONException {
            ArrayList<String> posters=new ArrayList<>();
            Log.d("DREAL", forecastJsonStr);
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            //JSONArray page1 = forecastJson.getJSONArray("page");
            //for(int i=0;i<page1.length();i++){
            //    Log.d("DREAL", page1.getString(i));
            //}

            JSONArray results = forecastJson.getJSONArray("results"); // aqui todos los datos de cada peli en Array
            for(int i=0;i<results.length();i++){
                Log.d("DREAL", results.getString(i));

            }
            for(int i=0;i<results.length();i++){
                posters.add(results.getJSONObject(i).getString("backdrop_path"));
            }

            ArrayList<URL> misPostersURL=getPopularMoviesURLs(posters);
        }
//todo crear metodo igual al siguiente que también devuelva Array de titulos de peliculas.
        //todo en los xml cambiar los relative view por otros apropiados
        private ArrayList<URL> getPopularMoviesURLs(ArrayList<String> posters){

            final String FORECAST_BASE_URL="http://image.tmdb.org/t/p/";
            final String RESOLUTION="w185/";
            ArrayList<URL> misPostersURL=new ArrayList<>();


            for(int i=0;i<posters.size();i++){
                Uri posterUri = Uri.parse(FORECAST_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(RESOLUTION)
                        .appendEncodedPath(posters.get(i))
                        .build();
                Log.d("DREAL",posterUri.toString());
                //URL posterURL = null;
                try {
                    URL posterURL = new URL(posterUri.toString());
                    misPostersURL.add(posterURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            return misPostersURL;
        }
    }

}

