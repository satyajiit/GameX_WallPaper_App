package com.satyajit.gamex.fragments;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.activities.WallpaperByCategoryActivity;
import com.satyajit.gamex.adapters.CategoryGamesAdapter;
import com.satyajit.gamex.adapters.GamesAdapter;
import com.satyajit.gamex.utils.AutoFitGridLayoutManager;
import com.satyajit.gamex.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class GamesFragment extends Fragment {

    RecyclerView recyclerView;
    public List<Items> namesList = new ArrayList<>();
    private GamesAdapter mAdapter;
    ProgressBar pBar;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_games, container, false);

        setHasOptionsMenu(true);

        initUI(v);
        setupRecylerView();

        if (mAdapter.getItemCount()==0)
            new LoadURL().execute("ss");

        Listeners();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.games_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    void Listeners(){

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            new LoadURL().execute();
        });

    }

    void setupRecylerView(){

        mAdapter = new GamesAdapter(namesList,getActivity());
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 1000);  //Per row 3 items ... 1000/3=333.33
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

    void initUI(View v){

        recyclerView = v.findViewById(R.id.recycler_view_games);
        pBar = v.findViewById(R.id.progressBar);
        mSwipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

    }


    private class LoadURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {



            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://adminpanelririo.com/Rgaming//api.php");

            Log.e("SSS", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray jSONArray = jsonObj.getJSONArray("MaterialWallpaper");

                    int i = 0;

                    // looping through All Contacts
                    while (i < jSONArray.length()) {
                        jsonObj = jSONArray.getJSONObject(i);

                        Items item = new Items(jsonObj.getString("category_name"),jsonObj.getString("category_image"),jsonObj.getString("total_gallery"),jsonObj.getString("cid"));
                        namesList.add(item);

                        i++;
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");


            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            pBar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();

            mSwipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!mSwipeRefreshLayout.isRefreshing())
                pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }



}
