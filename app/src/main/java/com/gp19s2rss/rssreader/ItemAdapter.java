// reference: https://blog.csdn.net/lvyoujt/article/details/51599220
// CC BY-SA 4.0

package com.gp19s2rss.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private int[] colors = new int[]{0xff3cb371, 0xffa0a0a0};
    ArrayList<Item> items;
    Context context;

    public ItemAdapter(ArrayList<Item> items, Context context) {
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
            viewHolder.title = convertView.findViewById(R.id.Title);
            viewHolder.description = convertView.findViewById(R.id.Description);
            viewHolder.date = convertView.findViewById(R.id.Date);
            viewHolder.link = convertView.findViewById(R.id.Link);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Insert data into the viewHolder
        viewHolder.title.setText(items.get(position).getTitle());
        viewHolder.description.setText(items.get(position).getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, DD MMM yyyy HH:mm:ss");
        viewHolder.date.setText(dateFormat.format(items.get(position).date));
        viewHolder.link.setText(items.get(position).getLink());

        // Change background color of each item
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
