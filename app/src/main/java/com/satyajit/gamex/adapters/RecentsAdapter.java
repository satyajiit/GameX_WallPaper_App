package com.satyajit.gamex.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.activities.SlideImageActivity;
import com.satyajit.gamex.activities.WallpaperByCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {
    private final Context context;

    //Extending the Recycler View to use it as the required adapter

    private List<Items> namesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,count;
        ImageView img;
        ProgressBar progressBar;
        MaterialRippleLayout clicker;
        LinearLayout container;

        //Declared all the views from single item xml


        public MyViewHolder(View view) {
            super(view);

            //Init
            name = view.findViewById(R.id.name);
            img = view.findViewById(R.id.image);
            count = view.findViewById(R.id.count);
            progressBar = view.findViewById(R.id.progress_bar);
            clicker = view.findViewById(R.id.clicker);
            container = view.findViewById(R.id.listed);

        }
    }


    public RecentsAdapter(List<Items> namesList, Context context) {
        this.namesList = namesList;
        this.context = context;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_wallpaper, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



        final Items items = namesList.get(position);



        holder.name.setText(items.getName());

        float count = Float.parseFloat(items.getCount());

        float temp;

        if (count>1000){

            temp = count/1000;

            if (temp > 99) holder.count.setText(String.format(java.util.Locale.US,"%.0f", count/1000)+"K");

            else     holder.count.setText(String.format(java.util.Locale.US,"%.1f", count/1000)+"K");

        }


        else
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

                Log.d("ss",items.getImage());
                namesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,namesList.size());
               // holder.shim.stopShimmer();
               // holder.img.setVisibility(View.VISIBLE);
                //holder.shim.setVisibility(View.GONE);
               // holder.name.setVisibility(View.VISIBLE);
            }

        });



    holder.clicker.setOnClickListener(view -> {
        Intent intent = new Intent(context, SlideImageActivity.class);
        intent.putExtra("position",position);
        context.startActivity(intent);
    });


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d("SSSS","HEYYY");
    }

    @Override
    public int getItemCount() {
        return namesList.size();
    }
}