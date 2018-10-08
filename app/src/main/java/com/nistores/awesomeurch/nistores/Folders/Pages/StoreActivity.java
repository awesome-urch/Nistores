package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.R;

public class StoreActivity extends AppCompatActivity {
    TextView navbarTitle;
    ImageView backNavigate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navbarTitle = findViewById(R.id.nav_title);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String id = bundle.getString("id");
            String storeName = bundle.getString("sName");
            navbarTitle.setText(storeName);
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
