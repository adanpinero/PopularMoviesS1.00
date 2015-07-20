package com.example.android.popularmoviess100;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView original_titleTV=(TextView)rootView.findViewById(R.id.original_title);
        original_titleTV.setText(getActivity().getIntent().getExtras().getString("original_title"));

        TextView original_languageTV=(TextView)rootView.findViewById(R.id.original_language);
        original_languageTV.setText(getActivity().getIntent().getExtras().getString("original_language"));

        TextView release_dateTV=(TextView)rootView.findViewById(R.id.release_date);
        release_dateTV.setText(getActivity().getIntent().getExtras().getString("release_date"));

        TextView overviewTV=(TextView)rootView.findViewById(R.id.overview);
        overviewTV.setText(getActivity().getIntent().getExtras().getString("overview"));

        ImageView imageViewPoster = (ImageView)rootView.findViewById(R.id.poster);
        Picasso.with(rootView.getContext()).load("http://image.tmdb.org/t/p/"+"w185/"+getActivity().getIntent().getExtras().getString("posterURLString")).error(R.drawable.image1).into(imageViewPoster);

        RatingBar ratingBar=(RatingBar)rootView.findViewById(R.id.rating_bar);
        ratingBar.setRating(getActivity().getIntent().getExtras().getInt("vote_average")/2);// API Rate 0-10 and RateBar 0-5
        return rootView;
    }
}
