package com.example.android.popularmoviess100;

/**
 * Created by Adan on 15/07/2015.
 * http://www.javacodegeeks.com/2013/08/android-custom-grid-view-example-with-image-and-text.html
 */

import android.content.Context;
import java.util.ArrayList;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 *
 * @author manish.s
 *
 */
public class CustomGridViewAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> posterURL = new ArrayList<>();


    public CustomGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<String> posterURLS) {
        super(context, layoutResourceId, posterURLS);
        this.context = context;
        this.posterURL = posterURLS;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;

        if (gridView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            gridView = inflater.inflate(R.layout.grid_single, parent, false);
            ImageView imageView = (ImageView)gridView.findViewById(R.id.grid_image);
            Picasso.with(context).load("http://image.tmdb.org/t/p/"+"w185/"+posterURL.get(position)).error(R.drawable.image1).into(imageView);
            Log.d("SI","GridViewAdapter position"+Integer.toString(position) );

        }
        else{
            gridView = convertView;
        }
        return gridView;
    }
}
