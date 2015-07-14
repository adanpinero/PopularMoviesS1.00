package com.example.android.popularmoviess100;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("miPID","");
        PopularMoviesApiRequest miPOPRequest=new PopularMoviesApiRequest();
        miPOPRequest.execute();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private class PopularMoviesApiRequest extends AsyncTask <Void,Void,String>{
        public String[] popularMovies;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            final String LOG_TAG=getClass().getName();
            final String API_KEY="764e1a37d810cfc4ef5be76e31f7b238";
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
            if(forecastJsonStr!=null){
                try {
                    getPopularMoviesFromJson(forecastJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
//todo implementar metodo que conje el JSON bruto y devuelve lo que necesito
        private void getPopularMoviesFromJson(String forecastJsonStr)
                throws JSONException{
            Log.d("DREAL", forecastJsonStr);

        }
    }

}

