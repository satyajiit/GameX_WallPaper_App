package com.satyajit.gamex.MenuFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.satyajit.gamex.R;
import com.satyajit.gamex.adapters.TabAdapter;
import com.satyajit.gamex.fragments.GamesFragment;
import com.satyajit.gamex.fragments.PopularFragment;
import com.satyajit.gamex.fragments.RecentFragment;


public class MainFragment extends Fragment implements RecentFragment.OnChildFragmentInteractionListener {

    public MainFragment() {
        // Required empty public constructor
    }

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        viewPager =  v.findViewById(R.id.viewPager);
        tabLayout = v.findViewById(R.id.tabLayout);


        setupAdapter();

        listener();

        return v;
    }

    void setupAdapter(){


        adapter = new TabAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new RecentFragment(), "RECENT");
        adapter.addFragment(new GamesFragment(), "GAMES");

        adapter.addFragment(new PopularFragment(), "POPULAR");





        viewPager.setAdapter(adapter);
        //int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        //viewPager.setOffscreenPageLimit(limit);
        //tabLayout.setupWithViewPager(viewPager);



        // this is a workaround
        tabLayout.post(() -> {
            //provide the viewPager to TabLayout.
            tabLayout.setupWithViewPager(viewPager);
        });

        //to preload the adjacent tabs. This makes transition smooth.

        viewPager.setOffscreenPageLimit(1);



    }


    @Override
    public void messageFromChildToParent(String myString) {
        Log.i("TAG", myString);
    }

    public interface OnFragmentInteractionListener {
        void messageFromParentFragmentToActivity(String myString);
    }




    void listener(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        getActivity().setTitle("RECENT");
                        break;
                    case 1:
                        getActivity().setTitle("GAMES");
                        break;
                    case 2:
                        getActivity().setTitle("POPULAR");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

}
