package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.R;

public class OwnStoreActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_store);
    }
}
