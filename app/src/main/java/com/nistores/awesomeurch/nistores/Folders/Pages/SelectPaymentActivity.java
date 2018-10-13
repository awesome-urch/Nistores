package com.nistores.awesomeurch.nistores.Folders.Pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nistores.awesomeurch.nistores.R;

public class SelectPaymentActivity extends AppCompatActivity {
    TextView uidView;
    String uid, selectedMethod;
    RadioGroup paymentGroup;
    static String ATM = "atm";
    static String IN_BANK = "in_bank";
    static String WITH_BANK = "with_bank";
    static String MOBILE = "mobile";
    static String AGENT = "agent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);

        View.OnClickListener onRadioButtonClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectPayMethod(view);
            }
        };

        paymentGroup = findViewById(R.id.paymentGroup);
        for(int x = 0; x < paymentGroup.getChildCount(); x++){
            View child = paymentGroup.getChildAt(x);
            child.setOnClickListener(onRadioButtonClicked);
        }

        uidView = findViewById(R.id.store_number);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            uid = bundle.getString("uid");
            uidView.setText(uid);
        }

    }

    private void onSelectPayMethod(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.atm:
                if (checked)
                    selectedMethod = ATM;
                    break;
            case R.id.with_bank:
                if (checked)
                    selectedMethod = WITH_BANK;
                    break;
            case R.id.in_bank:
                if (checked)
                    selectedMethod = IN_BANK;
                    break;
            case R.id.mobile:
                if (checked)
                    selectedMethod = MOBILE;
                    break;
            case R.id.agent:
                if (checked)
                    selectedMethod = AGENT;
                    break;
        }
        Toast.makeText(this,selectedMethod,Toast.LENGTH_SHORT).show();
    }
}
