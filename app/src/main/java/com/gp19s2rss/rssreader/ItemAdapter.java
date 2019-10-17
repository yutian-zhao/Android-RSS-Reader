package com.gp19s2rss.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * <h1>Show item in list-viewer </h1>
 * This class can extract data from items,
 * and show the information in list-viewer(id.layout.list_view_items)
 *
 * @version 1.0
 * @since 2019-10-9th
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    ArrayList<Item> items;

    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        items = objects;
    }


    /**
     * This method will collect data from item and represent to users
     *
     * @param position    item position
     * @param convertView item convert of view
     * @param parent      item parent
     * @return convertView - the information represented on the list viewer
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, null);

        }
        TextView title = convertView.findViewById(R.id.Title);
        TextView description = convertView.findViewById(R.id.Description);
        TextView link = convertView.findViewById(R.id.Link);
        TextView date = convertView.findViewById(R.id.Date);

        // Insert date
        title.setText(items.get(position).getTitle());
        description.setText(items.get(position).getDescription());
        link.setText(items.get(position).getLink());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        date.setText(dateFormat.format(items.get(position).getDate()));
        return convertView;
    }
}