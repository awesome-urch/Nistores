package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.MorePhotoAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.MorePhoto;
import com.nistores.awesomeurch.nistores.Folders.Helpers.VolleyRequest;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class deliveredOrderActivity extends AppCompatActivity {

    TextView orderIdView, orderDescView, priceView, myLocationView, receiverLocationView, storeNameView, storeNumberView,
            receiverUsernameView, receiverFullNameView, confirmOrderView, orderStatusView, confirmReceiveView;
    RecyclerView photoRecyclerView;
    List<MorePhoto> morePhotos;
    MorePhotoAdapter mAdapter;
    VideoView videoView;
    ImageView playImage, stopImage;
    AppCompatButton payBtn, retryBtn;
    ConstraintLayout networkErrorLayout, loaderLayout;
    LinearLayout infoLayout, controlLayout;
    String userId, URL, id, number, storeNumber, desc, photosString, video, totalPrice, loc_from, loc_to, storeName,
            receiverFullName, receiverUsername, initiatedDate, savedVideo;
    ApiUrls apiUrls;
    JSONArray photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_order);

        orderIdView = findViewById(R.id.order_id);
        orderDescView = findViewById(R.id.order_desc);
        priceView = findViewById(R.id.total_price);
        myLocationView = findViewById(R.id.your_location);
        receiverLocationView = findViewById(R.id.receiver_location);
        storeNameView = findViewById(R.id.store_name);
        storeNumberView = findViewById(R.id.store_number);
        receiverUsernameView = findViewById(R.id.receiver_username);
        receiverFullNameView = findViewById(R.id.receiver_full_name);
        confirmOrderView = findViewById(R.id.confirm_order);
        orderStatusView = findViewById(R.id.order_status);
        confirmReceiveView = findViewById(R.id.confirm_receiving);
        videoView = findViewById(R.id.video);
        controlLayout = findViewById(R.id.controls);
        playImage = findViewById(R.id.play);
        stopImage = findViewById(R.id.stop);

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });
        stopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();
            }
        });

        networkErrorLayout = findViewById(R.id.network_error_layout);
        loaderLayout = findViewById(R.id.loader_layout);
        infoLayout = findViewById(R.id.info_layout);

        photoRecyclerView = findViewById(R.id.recycler_photo_view);
        morePhotos = new ArrayList<>();
        mAdapter = new MorePhotoAdapter(getApplicationContext(), morePhotos);
        //mAdapter.setServer(false);

        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        photoRecyclerView.setLayoutManager(cLayoutManager);

        photoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        photoRecyclerView.setAdapter(mAdapter);

        payBtn = findViewById(R.id.btn_pay);
        retryBtn = findViewById(R.id.btn_retry);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userId = prefs.getString("user",null);

        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null){
            id = bundle.getString("id");
            number = bundle.getString("number");
            if(number != null){
                getInfo();
            }

        }

    }

    private void getInfo(){

        String pURL = URL + "request=state_order&order_no=" + number;
        Log.d("myURL",pURL);
        VolleyRequest volleyRequest = new VolleyRequest(getApplicationContext(), pURL) {
            @Override
            public void onProcess() {
                networkErrorLayout.setVisibility(View.GONE);
                loaderLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JSONObject response) {
                Log.d("MPUTA",response.toString());
                loaderLayout.setVisibility(View.GONE);
                try {

                    Integer err = response.getInt("error");
                    if(err==0){
                        JSONObject data = response.getJSONObject("data");
                        fillInItems(data);

                    }else{
                        Toast.makeText(getApplicationContext(),"Network Error Occurred",Toast.LENGTH_SHORT).show();
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
        volleyRequest.setCache(false);
        volleyRequest.fetchResources();
    }

    public void fillInItems(JSONObject data){
        infoLayout.setVisibility(View.VISIBLE);

        try {
            storeNumber = data.getString("number");
            desc = data.getString("description");
            photos = data.getJSONArray("photos");
            video = data.getString("videos");
            totalPrice = data.getString("total_price");
            loc_from = data.getString("loc_from");
            loc_to = data.getString("loc_to");
            storeName = data.getString("store_name");
            receiverFullName = data.getString("receiver_fullname");
            receiverUsername = data.getString("receiver_username");
            initiatedDate = data.getString("initiated_date");

            orderIdView.setText(number);
            storeNumberView.setText(storeNumber);
            orderDescView.setText(desc);
            priceView.setText(totalPrice);
            myLocationView.setText(loc_from);
            receiverLocationView.setText(loc_to);
            storeNameView.setText(storeName);
            receiverFullNameView.setText(receiverFullName);
            receiverUsernameView.setText(receiverUsername);

            //Loop through photos array and display in recyclerView
            for(int i = 0; i < photos.length(); i++){
                final String serverPic = photos.getString(i);
                new Handler()
                        .postDelayed(new Runnable(){
                            public void run(){
                                appendMorePhotos(serverPic);
                            }
                        }, 2000);
            }

            //get video uri and set the video
            String vidStr = new ApiUrls().getOffline() + video;
            Uri vidUri = Uri.parse(vidStr);
            setVideo(vidUri);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            //final int curSize = mAdapter.getItemCount();
            morePhotos.addAll(items);
            photoRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    //scroller.resetState();
                    //photoRecycler.getRecycledViewPool().clear();
                    mAdapter.notifyItemInserted(morePhotos.size() - 1);
                    //mAdapter.notifyItemRangeInserted(curSize, morePhotos.size() - 1);

                }
            });

        }catch(JSONException e){
            Log.d("ERR",e.toString());
            e.printStackTrace();
        }
    }

    private void setVideo(Uri videoUri){
        if(videoUri != null){
            savedVideo = videoUri+"";
        }

        Log.d("REZOT",videoUri+"");
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
        controlLayout.setVisibility(View.VISIBLE);
    }

}
