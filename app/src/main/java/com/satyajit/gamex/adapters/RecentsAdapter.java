package com.satyajit.gamex.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.satyajit.gamex.GetterSetter.RecentItems;
import com.satyajit.gamex.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {

    //Extending the Recycler View to use it as the required adapter

    private List<RecentItems> namesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,count;
        ImageView img;
        ProgressBar progressBar;

        //Declared all the views from single item xml


        public MyViewHolder(View view) {
            super(view);

            //Init
            name = view.findViewById(R.id.name);
            img = view.findViewById(R.id.image);
            count = view.findViewById(R.id.count);
            progressBar = view.findViewById(R.id.progress_bar);

        }
    }


    public RecentsAdapter(List<RecentItems> namesList) {
        this.namesList = namesList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_wallpaper, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



        final RecentItems items = namesList.get(position);



        holder.name.setText(items.getName());
        holder.count.setText(items.getCount());


        holder.img.setVisibility(View.GONE);



        Picasso.get().load("http://adminpanelririo.com/Rgaming//upload/"+items.getImage()).into(holder.img,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                holder.progressBar.setVisibility(View.GONE);
                holder.img.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Exception e) {

                Log.d("ss",e.getMessage());
               // holder.shim.stopShimmer();
               // holder.img.setVisibility(View.VISIBLE);
                //holder.shim.setVisibility(View.GONE);
               // holder.name.setVisibility(View.VISIBLE);
            }

        });






    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }
}