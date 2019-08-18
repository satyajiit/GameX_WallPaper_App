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
import com.satyajit.gamex.activities.SlideImageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    //Extending the Recycler View to use it as the required adapter

    private List<Items> namesList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView img;
        ProgressBar progressBar;
        MaterialRippleLayout holder;



        Context con;

        //Declared all the views from single item xml


        public MyViewHolder(View view) {
            super(view);

            //Init
            name = view.findViewById(R.id.name);
            img = view.findViewById(R.id.image);
            progressBar = view.findViewById(R.id.progress_bar);
            holder = view.findViewById(R.id.clicker);


        }
    }


    public FavAdapter(List<Items> namesList,Context context ) {
        this.namesList = namesList;
        this.context = context;

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_list_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {



        final Items items = namesList.get(position);



        holder.name.setText(items.getName());


        //holder.img.setVisibility(View.GONE);

        holder.progressBar.setVisibility(View.GONE);


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





        holder.holder.setOnClickListener(view -> {
            Intent intent = new Intent(context, SlideImageActivity.class);

            intent.putExtra("position",position);

            context.startActivity(intent);
        });


    }



    @Override
    public int getItemCount() {
        return namesList.size();
    }
}
