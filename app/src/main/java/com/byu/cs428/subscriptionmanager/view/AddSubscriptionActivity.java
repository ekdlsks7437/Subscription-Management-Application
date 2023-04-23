package com.byu.cs428.subscriptionmanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;
import com.byu.cs428.subscriptionmanager.view.fragment.SubscriptionFormsFragment;

public class AddSubscriptionActivity extends AppCompatActivity implements EditInterface {

    private FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_subscription_forms, new SubscriptionFormsFragment(new Subscription(), this));
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