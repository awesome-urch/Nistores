package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.MorePhotoAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.FileUpload;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.MorePhoto;
import com.nistores.awesomeurch.nistores.Folders.Helpers.Utility;
import com.nistores.awesomeurch.nistores.Folders.Helpers.WrapContentLinearLayoutManager;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitiateDeliveryActivity extends AppCompatActivity {
    Spinner storeSpinner;
    ArrayList<String> myStores;
    SharedPreferences prefs;
    String userId;
    ApiUrls apiUrls;
    String URL, postURL, storeID;
    JSONArray userStores;
    CoordinatorLayout coordinatorLayout;
    RecyclerView photoRecycler, videoRecycler;
    List<MorePhoto> morePhotos;
    MorePhotoAdapter mAdapter;
    AppCompatButton initDeliveryBtn, btnLivePhotos, btnLiveVideos;
    ImageView mImageView;
    VideoView videoView;
    String orderId;
    EditText orderNumberView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    static final String JPG = "jpg";
    Bitmap bitmap;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_delivery);

        photoRecycler = findViewById(R.id.recycler_photo_view);
        morePhotos = new ArrayList<>();
        mAdapter = new MorePhotoAdapter(getApplicationContext(), morePhotos);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        photoRecycler.setLayoutManager(mLayoutManager);
        //photoRecycler.setLayoutManager(new WrapContentGridLayoutManager(getApplicationContext(), 2));

        //photoRecycler.setHasFixedSize(true);

        photoRecycler.setItemAnimator(new DefaultItemAnimator());
        photoRecycler.setAdapter(mAdapter);

        mImageView = findViewById(R.id.profile_picture);
        videoView = findViewById(R.id.video);
        btnLivePhotos = findViewById(R.id.btn_live_photos);
        btnLiveVideos = findViewById(R.id.btn_live_videos);
        btnLivePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        btnLiveVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });


        orderNumberView = findViewById(R.id.order_id);
        myStores = new ArrayList<>();
        storeSpinner = findViewById(R.id.select_store);
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selStore = adapterView.getItemAtPosition(i).toString();
                Log.d("SEL_ST",selStore+" "+i);
                loadCategories(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("SEL_ST","nothing selected");
            }
        });

        coordinatorLayout = findViewById(R.id.myCoordinatorLayout);
        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();
        postURL = apiUrls.getProcessPost();
        utility = new Utility(getApplicationContext());

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            orderId = bundle.getString("orderNumber");
            orderNumberView.setText(orderId);
            //fetchItems();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle state) {

        super.onSaveInstanceState(state);
    }

    private void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){


            if(requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String resourceBase = utility.bitmapToBase64(imageBitmap);
                Log.d("REZOT",resourceBase);
                uploadOtherFiles(resourceBase, JPG);
                mImageView.setImageBitmap(imageBitmap);
            }else if(requestCode == REQUEST_VIDEO_CAPTURE){
                Uri videoUri = data.getData();
                Log.d("REZOT",videoUri+"");
                videoView.setVideoURI(videoUri);

            }

        }

        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String resourceBase = utility.bitmapToBase64(imageBitmap);
            uploadOtherFiles(resourceBase, JPG);
            Log.d("REZOT",resourceBase);
            mImageView.setImageBitmap(imageBitmap);
        }*/
    }

    //method for uploading other photos
    private void uploadOtherFiles(final String fileEncoded, final String ext){
        FileUpload fileUpload = new FileUpload(getApplicationContext()) {
            @Override
            public void onProcess() {
                preventInteraction();
                btnLivePhotos.setText(getResources().getString(R.string.uploading___));
            }

            @Override
            public void onSuccess(String imgPath) {
                Log.d("PTH",imgPath);
                enableUserInteraction();
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                btnLivePhotos.setText(getResources().getString(R.string.open_camera));
                appendMorePhotos(apiUrls.getUploadsFolder()+imgPath);
            }

            @Override
            public void onServerError() {
                enableUserInteraction();
                Toast.makeText(context, "Server error occurred. Try again", Toast.LENGTH_LONG).show();
                btnLivePhotos.setText(getResources().getString(R.string.open_camera));
            }

            @Override
            public void onNetworkError() {
                enableUserInteraction();
                Toast.makeText(context, "Network error occurred. Try again", Toast.LENGTH_LONG).show();
                btnLivePhotos.setText(getResources().getString(R.string.open_camera));
            }
        };
        fileUpload.uploadImage(fileEncoded, ext);
    }


    private void appendMorePhotos(String res){
        Log.d("iPATH",""+res);
        try{
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            object.put("image",res);
            array.put(object);

            List<MorePhoto> items = new Gson().fromJson(array.toString(), new TypeToken<List<MorePhoto>>() {
            }.getType());

            /*if (!morePhotos.isEmpty())

                morePhotos.clear(); //The list for update recycle view

            mAdapter.notifyDataSetChanged();*/
            final int curSize = mAdapter.getItemCount();
            morePhotos.addAll(items);
            photoRecycler.post(new Runnable() {
                @Override
                public void run() {
                    //scroller.resetState();
                    //photoRecycler.getRecycledViewPool().clear();
                    //mAdapter.notifyItemInserted(morePhotos.size() - 1);
                    mAdapter.notifyItemRangeInserted(curSize, morePhotos.size() - 1);

                }
            });

        }catch(JSONException e){
            Log.d("ERR",e.toString());
            e.printStackTrace();
        }
    }


    private void networkError(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.network_error,
                Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchItems();
                    }
                });
        snackbar.show();
    }

    private void fetchItems(){
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getString("user",null);
        String originURL = URL + "request=my_stores&id=" + userId;
        Log.d("CHECK",originURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, originURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RTN",response.toString());
                        try {

                            Integer err = response.getInt("error");
                            userStores = response.getJSONArray("data");
                            if(err==0){

                                fillInItems(userStores);

                            }else{
                                Toast.makeText(getApplicationContext(),"Sorry an error occurred",Toast.LENGTH_SHORT).show();
                                networkError();
                            }
                        } catch (JSONException e) {
                            Log.e("ERR",e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Sorry an error occurred. Try again",Toast.LENGTH_SHORT).show();
                networkError();
                Log.d("VOLLEY",error.toString());

            }
        });
        jsonObjectRequest.setShouldCache(false);
        InitiateVolley.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void fillInItems(JSONArray items){
        try{
            for(int i=0;i<items.length();i++){
                JSONObject jsonObject1=items.getJSONObject(i);
                String storeName=jsonObject1.getString("sname");
                myStores.add(storeName);
            }

            storeSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, myStores));

        }catch (JSONException e){e.printStackTrace();}

    }

    private void loadCategories(final int index){
        //String storeId = null;
        try {
            JSONObject storeObj = userStores.getJSONObject(index);
            String storeCatId = storeObj.getString("scat_id");
            storeID = storeObj.getString("store_id");
            Log.d("storeID", storeID);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("storeCatID", ""+e);
        }

    }

    private void updateChanges(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getString("user",null);

        preventInteraction();
        //btnSaveProfile.setText(getResources().getString(R.string.loading));
        StringRequest request = new StringRequest(Request.Method.POST, postURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //btnSaveProfile.setText(getResources().getString(R.string.save_changes));
                enableUserInteraction();
                Log.d("DFILE",s);

                if(s.equals("error")){

                    Toast.makeText(getApplicationContext(),"Sorry an error occurred. Retry",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Changes have been saved!",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //btnSaveProfile.setText(getResources().getString(R.string.save_changes));
                Toast.makeText(getApplicationContext(),"Network error occurred. Please retry!",Toast.LENGTH_SHORT).show();

                Log.d("ERR",volleyError.toString());


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> parameters = new HashMap<String, String>();
                //parameters.put("Content-Type", "application/form-data");
                //parameters.put("Content-Length", ""+97957);
                parameters.put("Connection", "Keep-Alive");
                return parameters;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
                //return "application/x-www-form-urlencoded";
            }

            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError  {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("request", "edit_profile");
                //parameters.put("picture", serverImg);
                parameters.put("id", userId);

                return parameters;
            }
        };

        //RequestQueue rQueue = Volley.newRequestQueue(getContext());
        request.setShouldCache(false);
        InitiateVolley.getInstance().addToRequestQueue(request);
    }

    public void preventInteraction(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableUserInteraction(){
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
