package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nistores.awesomeurch.nistores.Folders.Adapters.ProductAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.AllProductsTable;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.DatabaseHelper;
import com.nistores.awesomeurch.nistores.Folders.Helpers.GridSpacingItemDecoration;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Product;
import com.nistores.awesomeurch.nistores.Folders.Helpers.RecyclerScroller;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;


public class AllProductsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = AllProductsFragment.class.getSimpleName();

    ApiUrls apiUrls;
    private String URL;

    //private static final String URL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=products&start=0";
    //private static final String URL = "https://api.androidhive.info/json/movies_2017.json";

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter mAdapter;
    DatabaseHelper helper;
    Integer rid = 1;
    Dao<AllProductsTable, Integer> dao;
    Dao<AllProductsTable, Long> dao1;
    List<AllProductsTable> allProductsTableList;
    JSONArray myProductArray = new JSONArray();
    private RecyclerScroller scroller;
    int pageNo;

    //private OnFragmentInteractionListener mListener;

    public AllProductsFragment() {
        // Required empty public constructor
    }

    public static AllProductsFragment newInstance(String param1, String param2) {
        AllProductsFragment fragment = new AllProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_all_products, container, false);

        apiUrls = new ApiUrls();

        URL = apiUrls.getProductsURL();

        recyclerView = view.findViewById(R.id.recycler_view);
        productList = new ArrayList<>();
        mAdapter = new ProductAdapter(getContext(), productList);

        /*RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
recyclerView.setNestedScrollingEnabled(false);*/

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        scroller = new RecyclerScroller(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                scroller.resetState();
                pageNo += 20;
                getData(pageNo);
                Toast.makeText(getContext(),""+pageNo,Toast.LENGTH_SHORT).show();
                //onCreateData(newPage);
            }
        };
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        helper = new DatabaseHelper(getContext()).getInstance(getContext());
        AllProductsTable AllProductsTable = null;
        AllProductsTable dbCount = null;

        getData(0);

        return view;

    }

    private void getData(int start){

        //start = pageNo;
        String origin = Integer.toString(start);
        try{
            //helper = new DatabaseHelper(getContext());
            dao1 = DaoManager.createDao(helper.getConnectionSource(), AllProductsTable.class);
            int length = 20;
            String startStr = Integer.toString(start);

            Log.d("CHECK",startStr);

            QueryBuilder<AllProductsTable,Long> queryBuilder = dao1.queryBuilder().orderBy("id",true);
            queryBuilder.offset(Long.parseLong(startStr)).limit(Long.parseLong(Integer.toString(length)));
            allProductsTableList = queryBuilder.query();

            if(allProductsTableList.isEmpty()){ //Nothing is in the database
                Log.d("CHECK","Nothing in db");
                fetchProductItems(origin);
            }else{ //Something is in the database
                Log.d("CHECK","Something in db");

                for(AllProductsTable item:allProductsTableList){
                    String product_id = item.getProductId();
                    String pname = item.getName();
                    String pphoto = item.getPhoto();
                    String pprice = item.getPrice();
                    String views = item.getViews();
                    String store_uid = item.getStore_uid();

                    JSONObject myProductObject = new JSONObject();
                    myProductObject.put("product_id",product_id);
                    myProductObject.put("pname",pname);
                    myProductObject.put("pphoto",pphoto);
                    myProductObject.put("pprice",pprice);
                    myProductObject.put("views",views);
                    myProductObject.put("store_uid",store_uid);

                    myProductArray.put(myProductObject);
                }

                Log.d("CHECK",myProductArray.toString());

                List<Product> items = new Gson().fromJson(myProductArray.toString(), new TypeToken<List<Product>>() {
                }.getType());

                fillInItems(items);

                //List<AllProductsTable> list1 = dao1.queryForAll();
            }

        }catch (Exception e){
            Log.e("Err","Error occurred",e);
            Log.d("ERR",e.toString());
        }
    }



    private void fetchProductItems(String origin){
        String originURL = URL+"&start="+origin;
        Log.d("CHECK",originURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, originURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("RTN",response.toString());
                        try {

                            Integer err = response.getInt("error");
                            JSONArray data = response.getJSONArray("data");
                            if(err==0){

                                List<Product> items = new Gson().fromJson(data.toString(), new TypeToken<List<Product>>() {
                                }.getType());

                                fillInItems(items);
                                for(int i=0;i<data.length();i++){
                                    JSONObject obj = data.getJSONObject(i);
                                    String product_id = obj.getString("product_id");
                                    String pname = obj.getString("pname");
                                    String pphoto = obj.getString("pphoto");
                                    String pprice = obj.getString("pprice");
                                    String views = obj.getString("views");
                                    String store_uid = obj.getString("store_uid");

                                    helper = new DatabaseHelper(getContext()).getInstance(getContext());
                                    dao1 = DaoManager.createDao(helper.getConnectionSource(), AllProductsTable.class);
                                    QueryBuilder<AllProductsTable,Long> queryBuilder1 = dao1.queryBuilder();
                                    queryBuilder1.where().eq("productId",product_id);
                                    allProductsTableList = queryBuilder1.query();
                                    if(allProductsTableList.isEmpty()) {
                                        AllProductsTable all = new AllProductsTable();
                                        all.setProductId(product_id);
                                        all.setName(pname);
                                        all.setPhoto(pphoto);
                                        all.setPrice(pprice);
                                        all.setViews(views);
                                        all.setStore_uid(store_uid);
                                        dao1.create(all);
                                    }
                                }

                            }else{
                                Toast.makeText(getContext(),"Sorry an error occurred",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("ERR",e.toString());
                            e.printStackTrace();
                        } catch (SQLException e) {
                            Log.e("ERR",e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Sorry an error occurred. Try again",Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.toString());

            }
        });

        InitiateVolley.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void fillInItems(List<Product> items){

        productList.clear();
        productList.addAll(items);

        // refreshing recycler view
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
