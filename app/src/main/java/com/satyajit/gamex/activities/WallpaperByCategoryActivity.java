package com.satyajit.gamex.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;

import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.adapters.CategoryGamesAdapter;
import com.satyajit.gamex.utils.AutoFitGridLayoutManager;
import com.satyajit.gamex.utils.GameX;
import com.satyajit.gamex.utils.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class WallpaperByCategoryActivity extends AppCompatActivity {


    RecyclerView recyclerView;

    private List<Items> namesList = new ArrayList<>();

    private CategoryGamesAdapter mAdapter;
    ProgressBar pBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String id,cat_name;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_by_category);

        readFromFragment();

        initUI();

        setupRecylerView();

        if (mAdapter.getItemCount()==0)
            new LoadURL().execute("ss");

        Listeners();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setTitle(cat_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        onBackPressed();
        return true;
    }

    void readFromFragment(){

        Intent i = getIntent();

        Bundle b = i.getExtras();

        id = b != null ? b.getString("id", "0") : null;
        cat_name = b != null ? b.getString("name", "0") : null;

    }

    void Listeners(){

        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            new LoadURL().execute();
        });

    }

    void setupRecylerView(){

        mAdapter = new CategoryGamesAdapter(namesList,this);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);  //Per row 2 items ... 1000/500=2
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }

    void initUI(){

        recyclerView = findViewById(R.id.recycler_view_category_selection);
        pBar = findViewById(R.id.progressBar);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

    }


    private class LoadURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {



            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall("http://adminpanelririo.com/Rgaming//api.php?cat_id="+id);

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

                        Items item = new Items(jsonObj.getString("cat_name"),jsonObj.getString("images"),jsonObj.getString("view_count"));
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

            recyclerView.setVisibility(View.VISIBLE);

            ((GameX) getApplication()).setList(namesList);


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
