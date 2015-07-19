package com.example.android.popularmoviess100;

/**
 * Created by Adan on 15/07/2015.
 * http://www.javacodegeeks.com/2013/08/android-custom-grid-view-example-with-image-and-text.html
 */

import android.content.Context;



import java.net.URL;
import java.util.ArrayList;

        import android.app.Activity;
        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 *
 * @author manish.s
 *
 */
public class CustomGridViewAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
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
            //TextView textView = (TextView) gridView.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)gridView.findViewById(R.id.grid_image);
            //textView.setText("Hola");
            Picasso.with(context).load("http://image.tmdb.org/t/p/"+"w185/"+posterURL.get(position).toString()).error(R.drawable.image1).into(imageView);

        }
        else{
            gridView = (View) convertView;
        }
        return gridView;
    }
}
