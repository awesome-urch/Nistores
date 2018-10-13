package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nistores.awesomeurch.nistores.Folders.Adapters.PaymentPlanAdapter;
import com.nistores.awesomeurch.nistores.Folders.Helpers.ApiUrls;
import com.nistores.awesomeurch.nistores.Folders.Helpers.PaymentPlan;
import com.nistores.awesomeurch.nistores.Folders.Helpers.VolleyRequest;
import com.nistores.awesomeurch.nistores.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaymentInstructionAActivity extends AppCompatActivity {

    LinearLayout inBankLayout, agentLayout, mobileTransferLayout, cardLayout, networkErrorLayout;
    RecyclerView planRecycler;
    ProgressBar progressBar;
    List<PaymentPlan> paymentPlanList;
    PaymentPlanAdapter planAdapter;
    AppCompatButton callBtn, retryBtn;
    TextView agentPhoneView, titleCardView;
    String payMethod, URL, planString;
    static String ATM = "atm";
    static String IN_BANK = "in_bank";
    static String WITH_BANK = "with_bank";
    static String MOBILE = "mobile";
    String AGENT = "agent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_instruction_a);

        View.OnClickListener onPlaceCall = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAgent();
            }
        };
        URL = new ApiUrls().getApiUrl();

        agentPhoneView = findViewById(R.id.agent_phone);
        titleCardView = findViewById(R.id.title_card);

        progressBar = findViewById(R.id.loader);
        cardLayout = findViewById(R.id.card);
        networkErrorLayout = findViewById(R.id.network_error);
        inBankLayout = findViewById(R.id.in_bank);
        agentLayout = findViewById(R.id.agent);
        mobileTransferLayout = findViewById(R.id.mobile_transfer);
        callBtn = findViewById(R.id.btn_call);
        callBtn.setOnClickListener(onPlaceCall);

        retryBtn = findViewById(R.id.btn_retry);

        planRecycler = findViewById(R.id.payment_recycler);
        paymentPlanList = new ArrayList<>();
        planAdapter = new PaymentPlanAdapter(getApplicationContext(), paymentPlanList);

        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        gridLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        planRecycler.setLayoutManager(gridLayoutManager);

        planRecycler.setItemAnimator(new DefaultItemAnimator());
        planRecycler.setAdapter(planAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            payMethod = bundle.getString("method");
            setUI();
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }

    private void setUI() {
        switch (payMethod) {
            case "agent":
                agentLayout.setVisibility(View.VISIBLE);
                break;
            case "in_bank":
                inBankLayout.setVisibility(View.VISIBLE);
                break;
            case "mobile":
                mobileTransferLayout.setVisibility(View.VISIBLE);
                break;
            case "with_bank":
                cardLayout.setVisibility(View.VISIBLE);
                titleCardView.setText(getResources().getString(R.string.pay_with_bank));
                fetchPlans();
                break;
            case "atm":
                cardLayout.setVisibility(View.VISIBLE);
                titleCardView.setText(getResources().getString(R.string.pay_with_atm));
                fetchPlans();
                break;
        }
    }

    private void callAgent() {
        String number = agentPhoneView.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            startActivity(callIntent);
        }else{
            Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchPlans(){

        String newURL = URL + "request=pay_plans";
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
                        planString = data.toString();
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
        List<PaymentPlan> items = new Gson().fromJson(data.toString(), new TypeToken<List<PaymentPlan>>() {
        }.getType());
        paymentPlanList.clear();
        paymentPlanList.addAll(items);
        // refreshing recycler view
        planAdapter.notifyDataSetChanged();
    }

}
