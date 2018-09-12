package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.BottomNavigationHelper;
import com.nistores.awesomeurch.nistores.R;

import com.nistores.awesomeurch.nistores.Folders.Pages.AllProductsFragment;
import com.nistores.awesomeurch.nistores.Folders.Pages.BusinessLoungeFragment;
import com.nistores.awesomeurch.nistores.Folders.Pages.DisplayPortFragment;
import com.nistores.awesomeurch.nistores.Folders.Pages.ChatsFragment;
import com.nistores.awesomeurch.nistores.Folders.Pages.TopStoresFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Intent intent;
    Fragment fragment;
    private TextView mTextMessage;
    SharedPreferences preferences;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    fragment = AllProductsFragment.newInstance();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_business_lounge:
                    //mTextMessage.setText(R.string.title_business_lounge);
                    fragment = BusinessLoungeFragment.newInstance();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_display_port:
                    //mTextMessage.setText(R.string.title_display_port);
                    fragment = new DisplayPortFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_chats:
                    //mTextMessage.setText(R.string.title_chats);
                    fragment = new ChatsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_top_stories:
                    //mTextMessage.setText(R.string.title_top_stories);
                    fragment = new TopStoresFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragment = new AllProductsFragment();
        loadFragment(fragment);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        BottomNavigationHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_messages) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_recent_posts) {


        } else if (id == R.id.nav_favourites) {

        } else if (id == R.id.nav_explore_stores) {

        } else if (id == R.id.logout){
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("user");
            editor.apply();
            intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}
