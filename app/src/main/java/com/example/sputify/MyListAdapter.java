package com.example.sputify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
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
        holder.userImage.setImageResource(listdata[position].getImgId());
        holder.userIdTextView.setText(listdata[position].getUserId());
//        holder.linearLayout.setOnClickListener(view -> Toast.makeText(view.getContext(),"click on item: "+ myListData.getSongName(),Toast.LENGTH_LONG).show());
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView userImage;
        public TextView songNameTextView;
        public TextView userIdTextView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.userImage = itemView.findViewById(R.id.user_dp);
            this.songNameTextView = itemView.findViewById(R.id.song_name);
            this.userIdTextView = itemView.findViewById(R.id.user_id);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}