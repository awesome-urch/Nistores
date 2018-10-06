package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.MyStoreAdapter;
import com.nistores.awesomeurch.nistores.Folders.Adapters.PollAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.MyStore;
import com.nistores.awesomeurch.nistores.Folders.Helpers.VolleyRequest;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyStoresActivity extends AppCompatActivity {
    Intent intent;
    private ProgressBar progressBar;
    private LinearLayout networkErrorLayout;
    private RecyclerView recyclerView;
    private ApiUrls apiUrls;
    private List<MyStore> myStoreList;
    private MyStoreAdapter mAdapter;
    private String URL, userId, storeString;
    private AppCompatButton retryBtn;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);

        progressBar = findViewById(R.id.loader);
        networkErrorLayout = findViewById(R.id.network_error);
        retryBtn = findViewById(R.id.btn_retry);
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchItems();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        myStoreList = new ArrayList<>();
        mAdapter = new MyStoreAdapter(getApplicationContext(), myStoreList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = preferences.getString("user",null);

        if(savedInstanceState != null){
            storeString = savedInstanceState.getString("stores");
            if(storeString != null){
                try {
                    JSONArray allStatesArray = new JSONArray(storeString);
                    //userStores = storeArray;
                    fillInItems(allStatesArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                fetchItems();
            }
        }else{
            fetchItems();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stores",storeString);
    }

    private void fetchItems(){

        String newURL = URL + "request=my_stores&id=" + userId;
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext(), newURL) {
            @Override
            public void onProcess() {
                //do nothing while processing
                networkErrorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                networkErrorLayout.setVisibility(View.GONE);
                try {

                    Integer err = response.getInt("error");
                    if(err==0){

                        JSONArray data = response.getJSONArray("data");
                        Log.d("MPUTA",data.toString());
                        storeString = data.toString();
                        fillInItems(data);

                    }else{

                        networkErrorLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Log.e("V_ERROR",e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onNetworkError() {
                progressBar.setVisibility(View.GONE);
                networkErrorLayout.setVisibility(View.VISIBLE);
            }
        };
        volleyRequest.fetchResources();
    }

    private void fillInItems(JSONArray data){
        List<MyStore> items = new Gson().fromJson(data.toString(), new TypeToken<List<MyStore>>() {
        }.getType());
        myStoreList.clear();
        myStoreList.addAll(items);
        // refreshing recycler view
        mAdapter.notifyDataSetChanged();
    }
}
