package com.byu.cs428.subscriptionmanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;
import com.byu.cs428.subscriptionmanager.view.fragment.SubscriptionFormsFragment;

public class EditSubscriptionActivity extends AppCompatActivity implements EditInterface {

    private FragmentTransaction ft;
    private Subscription selectedSubscription;

    private EditText nameOfServiceET;
    private EditText costOfServiceET;
    // TODO: renewable period
    private TextView paymentPeriodTV;
    private TextView selectedFirstPaymentDateTV;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        selectedSubscription = (Subscription) getIntent().getSerializableExtra("subscription");

        ft = getSupportFragmentManager().beginTransaction();
        SubscriptionFormsFragment fragment = new SubscriptionFormsFragment(selectedSubscription, this);
        ft.replace(R.id.fl_subscription_forms, fragment);
        ft.commit();

    }

    @Override
    public void editSubscription(Subscription subscription) {
        Intent data = new Intent();
        String parsedSubscription = ConvertJson.Serialize(subscription);
        data.setData(Uri.parse(parsedSubscription));
        setResult(RESULT_OK, data);
        finish();
    }
}