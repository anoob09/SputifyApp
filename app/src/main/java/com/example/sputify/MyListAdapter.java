package com.example.sputify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{


    public Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }

    public String DownloadFiles(String url){
        String file = "";
        try {
            URL u = new URL(url);
            String fileName = url.substring(url.indexOf("image") + 6) + ".jpeg";
            InputStream is = u.openStream();

            DataInputStream dis = new DataInputStream(is);

            byte[] buffer = new byte[1024];
            int length;

            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)  + fileName));
            while ((length = dis.read(buffer))>0) {
                fos.write(buffer, 0, length);
            }
            file = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)  + fileName;
            File f = new File(file);

            if(f.exists())
                Log.e("FILE DOWNLOADED", "file downloaded");
            else
                Log.e("404", "FILE NOT FOUND");
        } catch (MalformedURLException mue) {
            Log.e("SYNC getUpdate", "malformed url error", mue);
        } catch (IOException ioe) {
            Log.e("SYNC getUpdate", "io error", ioe);
        } catch (SecurityException se) {
            Log.e("SYNC getUpdate", "security error", se);
        }

        return file;
    }


    private MyListData[] listdata;

    // RecyclerView recyclerView;
    public MyListAdapter(MyListData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        final String[] file = {""};
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                file[0] = DownloadFiles(myListData.getAlbumUrl());
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Log.e("FILE ", file[0]);
        Bitmap bmap = getBitmap(file[0]);
        holder.userImage.setImageBitmap(bmap);
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