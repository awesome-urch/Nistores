package com.nistores.awesomeurch.nistores.Folders.Pages;

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
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.TopStores;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DisplayPortFragment extends Fragment {
    RadioButton radioPrice, radioContact;
    RadioGroup radioGroupPrice;
    TextInputLayout priceLayout;
    Spinner storeSpinner;
    ApiUrls apiUrls;
    String URL;
    SharedPreferences prefs;
    String userId;
    ArrayList<String> myStores;
    ImageView mainPhoto;
    TextView uploadClick;
    CoordinatorLayout coordinatorLayout;
    Bitmap bitmap;
    RecyclerView imageRecycler;
    private static final int SELECT_PHOTO = 1;

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

        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();
        myStores = new ArrayList<>();

        coordinatorLayout = view.findViewById(R.id.myCoordinatorLayout);
        mainPhoto = view.findViewById(R.id.main_photo);
        mainPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attachMainPhoto(view);
                openGallery();
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

        fetchItems();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void openGallery(){
        Toast.makeText(getContext(),"Opening images folder...",Toast.LENGTH_SHORT).show();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    if(selectedImage !=null){

                        try {
                            uploadClick.setVisibility(View.GONE);
                            //getting image from gallery
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                            //Setting image to ImageView
                            mainPhoto.setImageBitmap(bitmap);
                            bitmapToBase64(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //mainPhoto.setImageURI(selectedImage);
                    }
                }
        }
    }

    public void bitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.d("REZOT",imageString);
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

            storeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, myStores));

        }catch (JSONException e){e.printStackTrace();}

    }


}
