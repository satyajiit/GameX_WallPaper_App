package com.satyajit.gamex.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.activities.WallpaperByCategoryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryGamesAdapter extends RecyclerView.Adapter<CategoryGamesAdapter.MyViewHolder> {

    //Extending the Recycler View to use it as the required adapter

    private List<Items> namesList;
    Context context;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView count;
        ImageView img;
        ProgressBar progressBar;


        //Declared all the views from single item xml


        public MyViewHolder(View view) {
            super(view);

            //Init

            img = view.findViewById(R.id.image);
            count = view.findViewById(R.id.count);
            progressBar = view.findViewById(R.id.progress_bar);

        }
    }


    public CategoryGamesAdapter(List<Items> namesList, Context context) {
        this.namesList = namesList;
        this.context = context;
    }



    @Override
    public CategoryGamesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_games_from_category, parent, false);

        return new CategoryGamesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGamesAdapter.MyViewHolder holder, int position) {



        final Items items = namesList.get(position);





        float count = Float.parseFloat(items.getCount());

        if (count>1000)
            holder.count.setText(String.format(java.util.Locale.US,"%.1f", count/1000)+"K");

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