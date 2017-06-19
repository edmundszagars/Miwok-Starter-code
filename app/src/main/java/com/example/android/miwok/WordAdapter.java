package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edmunds_TDL on 09/04/2017.
 */

public class WordAdapter extends ArrayAdapter <Word> {

    private int mcolourRecourceId;

    public WordAdapter(Activity context, List<Word> androidFlavors, int colourRecourceId) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
        mcolourRecourceId = colourRecourceId;}

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Word wordPosition = getItem(position);

        TextView wordTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        wordTextView.setText(wordPosition.getDdefaultTranslation());

        TextView miwokWordTextView = (TextView) listItemView.findViewById(R.id.minok_text_view);
        miwokWordTextView.setText(wordPosition.getMivokTranslation());

        ImageView miwokImageView = (ImageView) listItemView.findViewById(R.id.miwok_image_view);
        if (wordPosition.hasImage()) {
            miwokImageView.setImageResource(wordPosition.getImageResourceID());
            miwokImageView.setVisibility(View.VISIBLE);
        }
        else {miwokImageView.setVisibility(View.GONE);}

        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(),mcolourRecourceId);
        textContainer.setBackgroundColor(color);
        return listItemView;
    }
}
