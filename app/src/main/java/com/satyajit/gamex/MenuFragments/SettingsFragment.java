package com.satyajit.gamex.MenuFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.satyajit.gamex.BuildConfig;
import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {



    public SettingsFragment() {
        // Required empty public constructor
    }


    TextView version;
    LinearLayout reportBugs, rate, clrCache, share, dmca, policy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);


        initUI(v);

        version.setText(BuildConfig.VERSION_NAME);

        setHasOptionsMenu(true);

        listeners();

        return v;
    }

    private void initUI(View v){

        version = v.findViewById(R.id.version);
        reportBugs = v.findViewById(R.id.reportBugs);
        rate = v.findViewById(R.id.llRate);
        clrCache = v.findViewById(R.id.llCache);
        share = v.findViewById(R.id.llShare);
        dmca = v.findViewById(R.id.llDmca);
        policy = v.findViewById(R.id.llPrivacyPolicy);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    void listeners(){

        reportBugs.setOnClickListener(view -> openLink("mailto:satyajiit0@gmail.com"));
        rate.setOnClickListener(view -> openLink("https://play.google.com/store/apps/details?id=com.satyajit.gamex"));
        clrCache.setOnClickListener(view -> deleteCache(getActivity()));
        share.setOnClickListener(view -> shareApp());
        dmca.setOnClickListener(view -> openLink("https://docs.google.com/document/d/1wIiK2lLlEiUvYGsPgkiCk7pkvoasbHsdtnLlqxzvcrA/edit?usp=sharing"));
        policy.setOnClickListener(view -> openLink("https://docs.google.com/document/d/1e-4yaR0AjuTSNox5LTNcBTVHlhOtljVPxHJy0hqPQBE/edit?usp=sharing"));


    }

    void openLink(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            Toast.makeText(context, "Cleared!!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void shareApp(){

        String shareBody = "Get Exclusive Game Wallpapers for Your Device , Download the app now https://play.google.com/store/apps/details?id=com.satyajit.gamex";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "GameX");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using : "));


    }


}
