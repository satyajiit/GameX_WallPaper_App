package com.satyajit.gamex.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.satyajit.gamex.BuildConfig;
import com.satyajit.gamex.MenuFragments.FavoriteFragment;
import com.satyajit.gamex.MenuFragments.MainFragment;
import com.satyajit.gamex.MenuFragments.SettingsFragment;
import com.satyajit.gamex.R;

public class MainActivity extends AppCompatActivity {

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


        NavigationView nvDrawer =findViewById(R.id.nav_view);
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
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
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
                //fragmentClass = ThirdFragment.class;

                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                startFragment(fragmentClass);
                break;
            case R.id.nav_rate:
                //fragmentClass = ThirdFragment.class;
                break;
            case R.id.nav_share:
               // fragmentClass = ThirdFragment.class;
                break;
        }



        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
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
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

    }
}
