package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.MemberAdapter;
import com.nistores.awesomeurch.nistores.Folders.Adapters.ProductAdapter;
import com.nistores.awesomeurch.nistores.Folders.Adapters.TopStoresAdapter;
import com.nistores.awesomeurch.nistores.Folders.Adapters.TopicAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Member;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Product;
import com.nistores.awesomeurch.nistores.Folders.Helpers.TopStores;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Topic;
import com.nistores.awesomeurch.nistores.Folders.Helpers.VolleyRequest;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class searchResultActivity extends AppCompatActivity {
    Intent intent;
    ConstraintLayout loaderLayout,networkErrorLayout;
    RecyclerView recyclerProduct, recyclerStore, recyclerMember, recyclerTopic;
    LinearLayout productSection, storeSection, memberSection, topicSection;
    AppCompatButton allProductsBtn, allStoresBtn, allMembersBtn, allTopicsBtn, retryBtn;
    List<Product> products;
    List<TopStores> stores;
    List<Member> members;
    List<Topic> topics;
    ProductAdapter productAdapter;
    TopStoresAdapter topStoresAdapter;
    MemberAdapter memberAdapter;
    TopicAdapter topicAdapter;
    String URL;
    ApiUrls apiUrls;

    private static String PRODUCT = "product";
    private static String STORE = "store";
    private static String MEMBER = "member";
    private static String TOPIC = "topic";
    private String specSearch = "all";
    private String specWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        recyclerProduct = findViewById(R.id.recycler_product);
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(getApplicationContext(), products);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerProduct.setLayoutManager(cLayoutManager);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(imageRecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        categoryRecycler.addItemDecoration(dividerItemDecoration);*/
        recyclerProduct.setAdapter(productAdapter);

        recyclerStore = findViewById(R.id.recycler_store);
        stores = new ArrayList<>();
        topStoresAdapter = new TopStoresAdapter(getApplicationContext(), stores);
        RecyclerView.LayoutManager tLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerStore.setLayoutManager(tLayoutManager);
        recyclerStore.setAdapter(topStoresAdapter);


        recyclerMember = findViewById(R.id.recycler_member);
        members = new ArrayList<>();
        memberAdapter = new MemberAdapter(getApplicationContext(), members);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMember.setLayoutManager(mLayoutManager);
        recyclerMember.setAdapter(memberAdapter);

        recyclerTopic = findViewById(R.id.recycler_topic);
        topics = new ArrayList<>();
        topicAdapter = new TopicAdapter(getApplicationContext(), topics);
        RecyclerView.LayoutManager topicLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTopic.setLayoutManager(topicLayoutManager);
        recyclerTopic.setAdapter(topicAdapter);

        productSection = findViewById(R.id.product_section);
        storeSection = findViewById(R.id.store_section);
        memberSection = findViewById(R.id.member_section);
        topicSection = findViewById(R.id.topic_section);

        allProductsBtn = findViewById(R.id.all_products);
        allStoresBtn = findViewById(R.id.all_stores);
        allMembersBtn = findViewById(R.id.all_members);
        allTopicsBtn = findViewById(R.id.all_topics);
        retryBtn = findViewById(R.id.btn_retry);

        allProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allResults(PRODUCT);
            }
        });
        allStoresBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allResults(STORE);
            }
        });
        allMembersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allResults(MEMBER);
            }
        });
        allTopicsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allResults(TOPIC);
            }
        });
        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSearchResults();
            }
        });


        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();
        loaderLayout = findViewById(R.id.loader_layout);
        networkErrorLayout = findViewById(R.id.network_error_layout);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            specSearch = bundle.getString("searchType");
            specWord = bundle.getString("searchWord");
            getSearchResults();
        }
    }

    public void allResults(String type){
        Log.d("SRCH",type);
        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
    }

    public void getSearchResults(){
        String originURL = URL + "request=search&term=" + specWord + "&type=" + specSearch;
        Log.d("CHECK",originURL);

        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext(), originURL) {
            @Override
            public void onProcess() {
                //do nothing while processing
                networkErrorLayout.setVisibility(View.GONE);
                loaderLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JSONObject response) {
                loaderLayout.setVisibility(View.GONE);
                networkErrorLayout.setVisibility(View.GONE);
                try {

                    Integer err = response.getInt("error");
                    if(err==0){
                        JSONObject data = response.getJSONObject("data");
                        Log.d("MPUTA",data.toString());
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
                loaderLayout.setVisibility(View.GONE);
                networkErrorLayout.setVisibility(View.VISIBLE);
            }
        };
        volleyRequest.setCache(true);
        volleyRequest.fetchResources();

    }

    public void fillInItems(JSONObject data){

        try {
            JSONArray productsArray = data.getJSONArray("products");
            int productSize = productsArray.length();
            if(productSize > 0){
                productSection.setVisibility(View.VISIBLE);
                List<Product> productItems = new Gson().fromJson(productsArray.toString(), new TypeToken<List<Product>>() {
                }.getType());
                products.clear();
                products.addAll(productItems);
                // refreshing recycler view
                productAdapter.notifyDataSetChanged();
                if(productSize > 19){
                    allProductsBtn.setVisibility(View.VISIBLE);
                }
            }

            JSONArray storesArray = data.getJSONArray("stores");
            int storeSize = storesArray.length();
            if(storeSize > 0){
                storeSection.setVisibility(View.VISIBLE);
                List<TopStores> storeItems = new Gson().fromJson(storesArray.toString(), new TypeToken<List<TopStores>>() {
                }.getType());
                stores.clear();
                stores.addAll(storeItems);
                // refreshing recycler view
                topStoresAdapter.notifyDataSetChanged();
                if(storeSize > 19){
                    allStoresBtn.setVisibility(View.VISIBLE);
                }
            }

            JSONArray membersArray = data.getJSONArray("members");
            int memberSize = membersArray.length();
            if(memberSize > 0){
                storeSection.setVisibility(View.VISIBLE);
                List<Member> memberItems = new Gson().fromJson(membersArray.toString(), new TypeToken<List<Member>>() {
                }.getType());
                members.clear();
                members.addAll(memberItems);
                // refreshing recycler view
                topStoresAdapter.notifyDataSetChanged();
                if(memberSize > 19){
                    allMembersBtn.setVisibility(View.VISIBLE);
                }
            }

            JSONArray topicsArray = data.getJSONArray("topics");
            int topicSize = topicsArray.length();
            if(topicSize > 0){
                topicSection.setVisibility(View.VISIBLE);
                List<Topic> topicItems = new Gson().fromJson(topicsArray.toString(), new TypeToken<List<Topic>>() {
                }.getType());
                topics.clear();
                topics.addAll(topicItems);
                // refreshing recycler view
                topStoresAdapter.notifyDataSetChanged();
                if(topicSize > 19){
                    allTopicsBtn.setVisibility(View.VISIBLE);
                }
            }

        }catch(Exception e){
            Log.d("ERROR", e+"");
        }


    }

}