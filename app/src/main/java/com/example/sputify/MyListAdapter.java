package com.example.sputify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MyListData[] listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(MyListData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyListData myListData = listdata[position];
        holder.songNameTextView.setText(listdata[position].getSongName());
        holder.userIdTextView.setText(listdata[position].getUserId());
        Bitmap bmap = null;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try  {
                    String url = "https://homepages.cae.wisc.edu/~ece533/images/airplane.png";
                    Bitmap bmap = getBitmapFromURL(url);
                    if (bmap == null)
                        System.out.println("Invalid URL " +"BTABJSD");
                    else {
                        System.out.println("Valid URL " + "Bmap isnot null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (bmap != null)
            holder.userImage.setImageBitmap(bmap);
        else
            Log.e("BMAP IS NULL", "NULL!");
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView songNameTextView;
        public TextView userIdTextView;
        public LinearLayout listItemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.user_dp);
            this.songNameTextView = itemView.findViewById(R.id.song_name);
            this.userIdTextView = itemView.findViewById(R.id.user_id);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }
}