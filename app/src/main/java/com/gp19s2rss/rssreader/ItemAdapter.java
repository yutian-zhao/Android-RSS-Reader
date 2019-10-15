package com.gp19s2rss.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {

    ArrayList<Item> items = new ArrayList<>();

    public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        items = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_view_items, null);

        }
        TextView title = (TextView) v.findViewById(R.id.Title);
        TextView description = (TextView) v.findViewById(R.id.Description);
        TextView link = (TextView) v.findViewById(R.id.Link);
        TextView date = (TextView) v.findViewById(R.id.Date);
        title.setText(items.get(position).getTitle());
        description.setText(items.get(position).getDescription());
        link.setText(items.get(position).getChannel());
        date.setText(items.get(position).getDate());
        return v;

    }

}
