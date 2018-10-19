package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.ProductAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.EndlessRecyclerViewScrollListener;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Product;
import com.nistores.awesomeurch.nistores.Folders.Helpers.VolleyRequest;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private LinearLayout networkErrorLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EndlessRecyclerViewScrollListener scroller;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private String URL, userId;
    private static int FAVOURITE = 1;
    private int pageNo = 0;
    private boolean refresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        progressBar = findViewById(R.id.loader);
        networkErrorLayout = findViewById(R.id.network_error);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        setRefresh(true);
                        fetchItems(0);

                    }
                }
        );

        recyclerView = findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getApplicationContext(), productList);
        productAdapter.setDesign(FAVOURITE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

        scroller = new EndlessRecyclerViewScrollListener ((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                //Toast.makeText(getApplicationContext(),""+page,Toast.LENGTH_SHORT).show();
                //scroller.resetState();
                pageNo = page * 20;
                fetchItems(pageNo);

            }
        };

        recyclerView.addOnScrollListener(scroller);

        ApiUrls apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getString("user",null);

        fetchItems(0);
    }

    private void fetchItems(final int start){

        String newURL = URL + "request=favourites&id=" + userId + "&start=" + start;
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext(), newURL) {
            @Override
            public void onProcess() {
                //do nothing while processing
                if(start == 0){
                    networkErrorLayout.setVisibility(View.GONE);
                    if(!isRefresh()){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onSuccess(JSONObject response) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                networkErrorLayout.setVisibility(View.GONE);
                try {

                    Integer err = response.getInt("error");
                    if(err==0){

                        JSONArray data = response.getJSONArray("data");
                        Log.d("MPUTA",data.toString());
                        if(start > 0){
                            appendData(data);
                        }else{
                            fillInItems(data);
                        }

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
                if(start == 0){
                    progressBar.setVisibility(View.GONE);
                    networkErrorLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        volleyRequest.setCache(false);
        volleyRequest.fetchResources();
    }

    private void fillInItems(JSONArray data){
        scroller.resetState();
        List<Product> productItems = new Gson().fromJson(data.toString(), new TypeToken<List<Product>>() {
        }.getType());
        productList.clear();
        productList.addAll(productItems);
        // refreshing recycler view
        productAdapter.notifyDataSetChanged();
    }

    private void appendData(JSONArray data){
        List<Product> productItems = new Gson().fromJson(data.toString(), new TypeToken<List<Product>>() {
        }.getType());
        productList.addAll(productItems);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                //scroller.resetState();
                productAdapter.notifyItemInserted(productList.size() - 1);
            }
        });
    }

    private boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
}