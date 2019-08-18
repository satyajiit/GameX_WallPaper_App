package com.satyajit.gamex.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.adapters.RecentsAdapter;
import com.satyajit.gamex.utils.AutoFitGridLayoutManager;
import com.satyajit.gamex.utils.GameX;
import com.satyajit.gamex.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RecentFragment extends Fragment {


    private OnChildFragmentToActivityInteractionListener mActivityListener;
    private OnChildFragmentInteractionListener mParentListener;
    RecyclerView recyclerView;

     private List<Items> namesList = new ArrayList<>();

    private RecentsAdapter mAdapter;
    ProgressBar pBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isShuffle = FALSE ;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent, container, false);

        setHasOptionsMenu(true);

        initUI(v);

        setupRecylerView();

        //loadToList();

        if (mAdapter.getItemCount()==0||namesList.size()<1)
        new LoadURL().execute("ss");

        Listeners();



        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    public interface OnChildFragmentToActivityInteractionListener {
        void messageFromChildFragmentToActivity(String myString);
    }

    public interface OnChildFragmentInteractionListener {
        void messageFromChildToParent(String myString);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (namesList != null && getActivity() != null)

                ((GameX) getActivity().getApplication()).setList(namesList);

            if (recyclerView!=null){
                new LoadURL().execute("");
            }
           // Log.d("sssdaw","sssss");

        }

       else{

           if (namesList!=null&&namesList.size()>1) {
               int size = namesList.size();
               namesList.clear();
               mAdapter.notifyItemRangeRemoved(0, size);
           }

        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.recent_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.shuffle:
                //newGame();
                Collections.shuffle(namesList);
                mAdapter.notifyDataSetChanged();
                isShuffle = TRUE ;
                return true;
            case R.id.Latest:
                isShuffle = FALSE ;
                new LoadURL().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void Listeners(){

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            new LoadURL().execute();
        });

    }

    void setupRecylerView(){

        mAdapter = new RecentsAdapter(namesList, getActivity());
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);  //Per row 3 items ... 1000/3=333.33
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

    void initUI(View v){

        recyclerView = v.findViewById(R.id.recycler_view_recents);
        pBar = v.findViewById(R.id.progressBar);
        mSwipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

    }


    private class LoadURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {



            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://adminpanelririo.com/Rgaming//api.php?latest=");

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

                        Items item = new Items(jsonObj.getString("category_name"),jsonObj.getString("image"),jsonObj.getString("view_count"));
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

            if (isShuffle) Collections.shuffle(namesList);

            mAdapter.notifyDataSetChanged();

            mSwipeRefreshLayout.setRefreshing(false);

            recyclerView.setVisibility(View.VISIBLE);

            ((GameX) getActivity().getApplication()).setList(namesList);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            namesList.clear();
            if (!mSwipeRefreshLayout.isRefreshing())
            pBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }



}
