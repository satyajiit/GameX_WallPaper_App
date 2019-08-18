package com.satyajit.gamex.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.satyajit.gamex.BuildConfig;
import com.satyajit.gamex.MenuFragments.FavoriteFragment;
import com.satyajit.gamex.MenuFragments.MainFragment;
import com.satyajit.gamex.MenuFragments.SettingsFragment;
import com.satyajit.gamex.R;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener{

    DrawerLayout mDrawer;
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        mDrawer = findViewById(R.id.drawer_layout);


        NavigationView nvDrawer = findViewById(R.id.nav_view);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
         drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);

         startFragment(MainFragment.class);

        Menu menu = nvDrawer.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_home);
        menuItem.setChecked(true);

        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_drawer_header);
        TextView version = headerLayout.findViewById(R.id.verion_text);
        version.setText("Version: V "+BuildConfig.VERSION_NAME);




       /*
*/




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }





    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = MainFragment.class;
                startFragment(fragmentClass);
                break;
            case R.id.nav_fav:
                fragmentClass = FavoriteFragment.class;
                startFragment(fragmentClass);
                break;
            case R.id.nav_upd:
                openLink("https://play.google.com/store/apps/details?id=com.satyajit.gamex");

                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                startFragment(fragmentClass);
                break;
            case R.id.nav_rate:
                openLink("https://play.google.com/store/apps/details?id=com.satyajit.gamex");
                break;
            case R.id.nav_share:
               shareApp();
                break;
        }


    if (menuItem.getItemId()!= R.id.nav_share && menuItem.getItemId()!= R.id.nav_rate && menuItem.getItemId()!= R.id.nav_upd ) {
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());

    }

    else {


    }
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    void startFragment(Class fr){

        Fragment fragment = null;

        try {
            fragment = (Fragment) fr.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();



        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.flContent, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }


    @Override
    public void messageFromParentFragmentToActivity(String myString) {
        Log.i("TAG", myString);
    }


    private void shareApp(){

        String shareBody = "Get Exclusive Game Wallpapers for Your Device , Download the app now https://play.google.com/store/apps/details?id=com.satyajit.gamex";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "GameX");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using : "));


    }


    void openLink(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

    }


}
