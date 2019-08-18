package com.satyajit.gamex.MenuFragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.satyajit.gamex.DatabaseHelper.SQLiteDBHelper;
import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.adapters.FavAdapter;
import com.satyajit.gamex.adapters.RecentsAdapter;
import com.satyajit.gamex.utils.AutoFitGridLayoutManager;
import com.satyajit.gamex.utils.GameX;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {


    RecyclerView recyclerView;

    private List<Items> namesList = new ArrayList<>();

    private FavAdapter mAdapter;

    LinearLayout holder;

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);


        initUI(v);

        setupRecylerView();

        LoadToList();

        return v;
    }

    void initUI(View v){

        recyclerView = v.findViewById(R.id.recycler_view);
        holder = v.findViewById(R.id.lyt_no_favorite);

    }



    void setupRecylerView(){

        mAdapter = new FavAdapter(namesList, getActivity());
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);  //Per row 3 items ... 1000/3=333.33
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



    }


    void LoadToList(){



        SQLiteDatabase database = new SQLiteDBHelper(getActivity()).getReadableDatabase();

            String selectQuery = "SELECT  * FROM images";


            Cursor cursor = database.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                  Items item = new Items(cursor.getString(1),cursor.getString(2));

                    namesList.add(item);
                } while (cursor.moveToNext());
            }

        mAdapter.notifyDataSetChanged();

            if (namesList.size()>0) {
                holder.setVisibility(View.GONE);
                ((GameX) getActivity().getApplication()).setList(namesList);
            }

        }


    }




