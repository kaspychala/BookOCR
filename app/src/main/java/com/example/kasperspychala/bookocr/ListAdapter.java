package com.example.kasperspychala.bookocr;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kasper on 21.05.2016.
 */
public class ListAdapter extends ArrayAdapter<RowBook> {

    Context context;
    int layoutResourceId;
    RowBook data[] = null;

    public ListAdapter(Context context, int layoutResourceId, RowBook[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RowBeanHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imageBook);
            holder.txtTitle = (TextView)row.findViewById(R.id.textBook);
            holder.txtAuthor = (TextView)row.findViewById(R.id.textAuthor);

            row.setTag(holder);
        }
        else
        {
            holder = (RowBeanHolder)row.getTag();
        }

        RowBook object = data[position];
        holder.txtTitle.setText(object.title);
        holder.txtAuthor.setText(object.author);
        holder.imgIcon.setImageResource(object.icon);

        return row;
    }

    static class RowBeanHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtAuthor;
    }
}
