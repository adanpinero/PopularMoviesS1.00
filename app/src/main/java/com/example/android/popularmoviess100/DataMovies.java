package com.example.android.popularmoviess100;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
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
 * Created by Adan on 17/07/2015. A Class to help with DataMovies Data. This class call the API.
 */
public class DataMovies {
    String SHORT_TYPE="popular"; //todo quitar cuando el tipo de orden sea por settings
    ArrayList<String> poster_URL; // String URL of image posters to show it using Picasso
    // More object to save data from API
    ArrayList<String> original_language;
    ArrayList<String> original_title;
    ArrayList<String> overview;
    ArrayList<String> release_date;
    ArrayList<Integer> vote_average;
    ArrayList<Integer> vote_count;

    Activity currentActivity; // Current activity
    CustomGridViewAdapter miAdapter; // Current adapter

    //Get method
    public String getURLString(int position){
        return poster_URL.get(position).toString();
    }
    //Get method
    public String getOriginal_language(int position){

        return original_language.get(position);
    }
    //Get method
    public String getOriginal_title(int position) {

        return original_title.get(position);
    }
    //Get method
    public String getOverview(int position) {

        return overview.get(position);
    }
    //Get method
    public String getRelease_date(int position) {

        return release_date.get(position);
    }
    //Get method
    public Integer getVote_average(int position) {

        return vote_average.get(position);
    }

    //Get method
    public Integer getVote_count(int position) {

        return vote_count.get(position);

    }
    //Constructor. Fix activity and call API
    public DataMovies(Activity activity) {
        currentActivity=activity;
        PopularMoviesApiRequest miPOPRequest = new PopularMoviesApiRequest();
        miPOPRequest.execute();
    }

    public void SetAdapter(CustomGridViewAdapter adapter){
        miAdapter=adapter;
    }

    public void setCurrentActivity(Activity activity){

        this.currentActivity=activity;
    }

    //Call the API to obtain JSON data
    private class PopularMoviesApiRequest extends AsyncTask<Void, Void, String> {
        ProgressDialog enProgreso;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create and show Connecting to Internet message / ProgressDialog during doInBackGround
            enProgreso = new ProgressDialog(currentActivity);
            enProgreso.setIndeterminate(true);
            enProgreso.setMessage("Looking for Movies...");
            enProgreso.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            final String LOG_TAG = getClass().getName();//To debug and handle errors
            final String API_KEY = "764e1a37d810cfc4ef5be76e31f7b238"; // API key TheMovieDB
            final String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?"; // Base TheMovieDB API Url
            final String SHORT_PARAM = "sort_by"; // ShortBy API parameter
            final String API_KEY_PARAM = "api_key"; // API key parameter

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
                        .appendQueryParameter(SHORT_PARAM, "popularity.desc")//todo modificar con settings
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

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
                    //Call a method to extract data from JSON Object
                    getPopularMoviesFromJson(forecastJsonStr);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            enProgreso.dismiss(); //Close Connecting to internet message
        }

        // Get data from JSON
        private void getPopularMoviesFromJson(String forecastJsonStr)
                throws JSONException {
            //ArrayList<String> posters=new ArrayList<>();
            poster_URL=new ArrayList<>();
            original_language=new ArrayList<>();
            original_title=new ArrayList<>();
            overview=new ArrayList<>();
            release_date=new ArrayList<>();
            vote_average=new ArrayList<>();
            vote_count=new ArrayList<>();

            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray results = forecastJson.getJSONArray("results"); // aqui todos los datos de cada peli en Array
            for(int i=0;i<results.length();i++){
                poster_URL.add(results.getJSONObject(i).getString("poster_path"));
                original_title.add(results.getJSONObject(i).getString("original_title"));
                original_language.add(results.getJSONObject(i).getString("original_language"));
                overview.add(results.getJSONObject(i).getString("overview"));
                release_date.add(results.getJSONObject(i).getString("release_date"));
                vote_average.add(results.getJSONObject(i).getInt("vote_average"));
                vote_count.add(results.getJSONObject(i).getInt("vote_count"));
            }

            //ArrayList<URL> misPostersURL=getPopularMoviesURLs(posters);
            //url_init=misPostersURL;
            ActualizaAdapter(poster_URL);
        }

        private void ActualizaAdapter(ArrayList<String> misPostersURL){
            miAdapter.clear();
            miAdapter.addAll(misPostersURL);
        }
    }

}