package com.gp19s2rss.rssreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> items;

    ItemAdapter(Context context, int id, ArrayList<Item> items){
        super(context, id, items);
        this.items = items;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        super.getView(position, convertView, parent);
        View view = convertView;
        if (view == null){
            Context c = MainActivity.getAppContext();
            LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate(R.layout.item, null);
        }
        ((TextView)view.findViewById(R.id.textView)).setText(items.get(position).title);
        ((TextView)view.findViewById(R.id.textView2)).setText(items.get(position).description);
        ((TextView)view.findViewById(R.id.textView3)).setText(items.get(position).channel);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
        ((TextView)view.findViewById(R.id.textView4)).setText(dateFormat.format(items.get(position).date));
        return view;
    }
}
