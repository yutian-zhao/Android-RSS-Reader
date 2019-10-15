package com.gp19s2rss.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_items, null);

        }
        TextView title = convertView.findViewById(R.id.Title);
        TextView description = convertView.findViewById(R.id.Description);
        TextView link = convertView.findViewById(R.id.Link);
        TextView date = convertView.findViewById(R.id.Date);
        title.setText(items.get(position).getTitle());
        description.setText(items.get(position).getDescription());
        link.setText(items.get(position).getChannel());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
        date.setText(dateFormat.format(items.get(position).getDate()));
        return convertView;
    }
}