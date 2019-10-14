package com.gp19s2rss.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemsAdapter extends BaseAdapter {
    private int[] colors = new int[] { 0xff3cb371, 0xffa0a0a0 };
    ArrayList<Item> items;
    Context context;

    public ItemsAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.Title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.Description);
            viewHolder.date = (TextView) convertView.findViewById(R.id.Date);
            viewHolder.link = (TextView) convertView.findViewById(R.id.Link);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Insert data into the viewHolder
        viewHolder.title.setText((String) items.get(position).getTitle());
        viewHolder.description.setText((String) items.get(position).getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
        viewHolder.date.setText(dateFormat.format(items.get(position).date));
        viewHolder.link.setText((String) items.get(position).getLink());

        int colorPos = position % colors.length;
        convertView.setBackgroundColor(colors[colorPos]);

        return convertView;
    }

    // Four elements will be displayed out
    final class ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        TextView link;
    }
}
