package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.selectCategoryAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.InitiateVolley;
import com.nistores.awesomeurch.nistores.Folders.Helpers.selectCategory;
import com.nistores.awesomeurch.nistores.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    EditText firstNameView, lastNameView, emailView;
    ImageView picView;
    RecyclerView categoryRecycler;
    String firstname, lastname, email, picture, URL, interests;
    AppCompatButton btnSaveProfile, btnEditProfilePic, btnUpdateInterest;
    List<selectCategory> selectCategoryList;
    selectCategoryAdapter categoryAdapter;
    ApiUrls apiUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        apiUrls = new ApiUrls();
        URL = apiUrls.getApiUrl();

        firstNameView = findViewById(R.id.first_name);
        lastNameView = findViewById(R.id.last_name);
        emailView = findViewById(R.id.input_email);
        picView = findViewById(R.id.profile_picture);
        categoryRecycler = findViewById(R.id.recycler_category);

        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnEditProfilePic = findViewById(R.id.edit_profile_pic);
        btnUpdateInterest = findViewById(R.id.update_interest);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle!=null) {
            firstname = bundle.getString("firstname");
            lastname = bundle.getString("lastname");
            email = bundle.getString("email");
            picture = bundle.getString("picture");
            interests = bundle.getString("interests");

            selectCategoryList = new ArrayList<>();
            categoryAdapter = new selectCategoryAdapter(getApplicationContext(), selectCategoryList);
            categoryAdapter.setPreSelect(interests);
            RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
            //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            categoryRecycler.setLayoutManager(cLayoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(categoryRecycler.getContext(),
                    DividerItemDecoration.VERTICAL);
            categoryRecycler.addItemDecoration(dividerItemDecoration);
            categoryRecycler.setAdapter(categoryAdapter);

            setInitialValues();
            fetchCategories();
        }

    }

    private void setInitialValues(){
        firstNameView.setText(firstname);
        lastNameView.setText(lastname);
        emailView.setText(email);

        if(!picture.isEmpty()){
            final String STRING_BASE_URL = "https://www.nistores.com.ng/";
            Picasso.with(getApplicationContext()).load(STRING_BASE_URL + picture).placeholder(R.drawable.ic_person_default).into(picView);
        }

    }

    private void fetchCategories(){

        String originURL = URL + "request=categories";
        Log.d("CHECK",originURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, originURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RTN",response.toString());
                        try {

                            Integer err = response.getInt("error");
                            JSONArray storeCategories = response.getJSONArray("data");
                            if(err==0){
                                List<selectCategory> items = new Gson().fromJson(storeCategories.toString(), new TypeToken<List<selectCategory>>() {
                                }.getType());
                                fillInCategories(items);
                                //fillInItems(storeCategories);

                            }else{
                                Toast.makeText(getApplicationContext(),"Sorry an error occurred",Toast.LENGTH_SHORT).show();
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

                Log.d("VOLLEY",error.toString());

            }
        });
        //jsonObjectRequest.setShouldCache(false);
        InitiateVolley.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void fillInCategories(List<selectCategory> items){
        selectCategoryList.clear();
        selectCategoryList.addAll(items);
        // refreshing recycler view
        categoryAdapter.notifyDataSetChanged();
    }
}
