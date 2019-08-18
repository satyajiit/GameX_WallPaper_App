package com.satyajit.gamex.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import androidx.viewpager.widget.PagerAdapter;

import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {


    public List<Items> namesList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter(List<Items> namesList, Context context) {
        this.context = context;
        this.namesList=namesList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return namesList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.view_pager_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout
                .findViewById(R.id.image);

        ProgressBar pBar = imageLayout.findViewById(R.id.loading);


        //imageView.setImageResource(IMAGES.get(position));

        final Items items = namesList.get(position);


        Picasso.get().load("http://adminpanelririo.com/Rgaming//upload/"+items.getImage()).into(imageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                pBar.setVisibility(View.GONE);



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

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
