package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.MorePhotoAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.FileUpload;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.MorePhoto;
import com.nistores.awesomeurch.nistores.Folders.Helpers.TopStores;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DisplayPortFragment extends Fragment {
    RadioButton radioPrice, radioContact;
    RadioGroup radioGroupPrice;
    AppCompatButton btnMorePhotos;
    TextInputLayout priceLayout;
    Spinner storeSpinner;
    ApiUrls apiUrls;
    String URL, imgURL;
    SharedPreferences prefs;
    String userId;
    ArrayList<String> myStores;
    ImageView mainPhoto;
    TextView uploadClick;
    CoordinatorLayout coordinatorLayout;
    Bitmap bitmap;
    RecyclerView imageRecycler;
    List<MorePhoto> morePhotos;
    MorePhotoAdapter mAdapter;
    ProgressBar uploadingMainPhoto, uploadingMorePhoto;
    ProgressDialog progressDialog;
    HomeActivity homeActivity;
    private static final int SELECT_PHOTO = 1;
    private static final int SELECT_MORE_PHOTO = 2;
    private static final String JPG = "jpg";

    public DisplayPortFragment() {
        // Required empty public constructor
    }

    public static DisplayPortFragment newInstance() {
        DisplayPortFragment fragment = new DisplayPortFragment();
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
        return inflater.inflate(R.layout.fragment_display_port, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
        }

        homeActivity = new HomeActivity();
        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();
        imgURL = apiUrls.getApiURL2();
        myStores = new ArrayList<>();

        uploadingMainPhoto = view.findViewById(R.id.progress_upload);
        coordinatorLayout = view.findViewById(R.id.myCoordinatorLayout);
        mainPhoto = view.findViewById(R.id.main_photo);
        mainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attachMainPhoto(view);
                openGallery(SELECT_PHOTO);
            }
        });

        btnMorePhotos = view.findViewById(R.id.btn_more_photos);
        btnMorePhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attachMainPhoto(view);
                openGallery(SELECT_MORE_PHOTO);
            }
        });

        uploadClick = view.findViewById(R.id.click_upload);
        storeSpinner = view.findViewById(R.id.select_store);
        priceLayout = view.findViewById(R.id.input_layout_price);
        radioPrice = view.findViewById(R.id.radio_price);
        radioContact = view.findViewById(R.id.radio_contact);
        radioPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        radioContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });

        imageRecycler = view.findViewById(R.id.recycler_img_view);
        morePhotos = new ArrayList<>();
        mAdapter = new MorePhotoAdapter(getContext(), morePhotos);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        imageRecycler.setLayoutManager(mLayoutManager);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(imageRecycler.getContext(),
                DividerItemDecoration.VERTICAL);
        imageRecycler.addItemDecoration(dividerItemDecoration);*/
        imageRecycler.setItemAnimator(new DefaultItemAnimator());
        imageRecycler.setAdapter(mAdapter);

        fetchItems();

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop(){
        super.onStop();
        //TODO: stop all volleys using their tags
    }

    private void openGallery(int requestType){
        Toast.makeText(getContext(),"Opening images folder...",Toast.LENGTH_SHORT).show();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, requestType);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            if(selectedImage !=null){

                try {
                    //getting image from gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    String resourceBase = bitmapToBase64(bitmap);
                    //Setting image to ImageView
                    switch (requestCode){
                        case SELECT_PHOTO:

                            //uploadImage(String.valueOf(imageObject));
                            //uploadImage(resourceBase, "jpg", SELECT_PHOTO);

                            //mainPhoto.setImageBitmap(bitmap);
                            uploadMainFile(resourceBase, JPG);

                            break;
                        case SELECT_MORE_PHOTO:
                            //appendMorePhotos(resourceBase);
                            //uploadImage(resourceBase, "jpg", SELECT_MORE_PHOTO);
                            uploadOtherFiles(resourceBase, JPG);

                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //method for uploading port main photo
    private void uploadMainFile(final String fileEncoded, final String ext){
        FileUpload fileUpload = new FileUpload(getContext()) {
            @Override
            public void onProcess() {
                preventInteraction();
                uploadClick.setVisibility(View.GONE);
                uploadingMainPhoto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(String imgPath) {
                enableUserInteraction();
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                uploadingMainPhoto.setVisibility(View.GONE);
                Picasso.with(getContext()).load(imgPath).placeholder(R.drawable.ic_crop_image).into(mainPhoto);
            }

            @Override
            public void onServerError() {
                enableUserInteraction();
                Toast.makeText(context, "Server error occurred. Try again", Toast.LENGTH_LONG).show();
                uploadingMainPhoto.setVisibility(View.GONE);
                uploadClick.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkError() {
                enableUserInteraction();
                Toast.makeText(context, "Network error occurred. Try again", Toast.LENGTH_LONG).show();
                uploadClick.setVisibility(View.VISIBLE);
                uploadingMainPhoto.setVisibility(View.GONE);
            }
        };
        fileUpload.uploadImage(fileEncoded, ext);
    }

    //method for uploading other photos
    private void uploadOtherFiles(final String fileEncoded, final String ext){
        FileUpload fileUpload = new FileUpload(getContext()) {
            @Override
            public void onProcess() {
                preventInteraction();
                btnMorePhotos.setText(getResources().getString(R.string.uploading___));
            }

            @Override
            public void onSuccess(String imgPath) {
                enableUserInteraction();
                Toast.makeText(context, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                btnMorePhotos.setText(getResources().getString(R.string.click_to_upload_more));
                appendMorePhotos(imgPath);
            }

            @Override
            public void onServerError() {
                enableUserInteraction();
                Toast.makeText(context, "Server error occurred. Try again", Toast.LENGTH_LONG).show();
                btnMorePhotos.setText(getResources().getString(R.string.click_to_upload_more));
            }

            @Override
            public void onNetworkError() {
                enableUserInteraction();
                Toast.makeText(context, "Network error occurred. Try again", Toast.LENGTH_LONG).show();
                btnMorePhotos.setText(getResources().getString(R.string.click_to_upload_more));
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

            Log.d("ARR",array.toString());

            morePhotos.addAll(items);
            final int curSize = mAdapter.getItemCount();
            imageRecycler.post(new Runnable() {
                @Override
                public void run() {
                    //scroller.resetState();
                    mAdapter.notifyItemInserted(morePhotos.size() - 1);
                    //mAdapter.notifyItemRangeInserted(curSize, productList.size() - 1);
                }
            });

        }catch(JSONException e){
            Log.d("ERR",e.toString());
            e.printStackTrace();
        }
    }

    public String bitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, 0);
        //Log.d("REZOT",imageString);
        return imageString;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_price:
                if (checked)
                priceLayout.setVisibility(View.VISIBLE);
                    // Pirates are the best
                    break;
            case R.id.radio_contact:
                if (checked)
                priceLayout.setVisibility(View.GONE);
                    // Ninjas rule
                    break;
        }
    }

    private void fetchItems(){
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
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
                            JSONArray data = response.getJSONArray("data");
                            if(err==0){

                                fillInItems(data);

                            }else{
                                Toast.makeText(getContext(),"Sorry an error occurred",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e("ERR",e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Sorry an error occurred. Try again",Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.network_error,
                        Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fetchItems();
                    }
                });
                snackbar.show();

                Log.d("VOLLEY",error.toString());

            }
        });
        //jsonObjectRequest.setShouldCache(true);
        InitiateVolley.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void fillInItems(JSONArray items){

        try{
            for(int i=0;i<items.length();i++){
                JSONObject jsonObject1=items.getJSONObject(i);
                String storeName=jsonObject1.getString("sname");
                myStores.add(storeName);
            }

            storeSpinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, myStores));

        }catch (JSONException e){e.printStackTrace();}

    }

    public void preventInteraction(){
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void enableUserInteraction(){
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


}
