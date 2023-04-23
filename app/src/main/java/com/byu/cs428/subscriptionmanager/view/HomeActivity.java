package com.byu.cs428.subscriptionmanager.view;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;
import com.byu.cs428.subscriptionmanager.view.fragment.FinancialFragment;
import com.byu.cs428.subscriptionmanager.view.fragment.RemoveInterface;
import com.byu.cs428.subscriptionmanager.view.fragment.SettingsFragment;
import com.byu.cs428.subscriptionmanager.view.fragment.SubscriptionFragment;
import com.byu.cs428.subscriptionmanager.view.fragment.UpdateSettingsInterface;
import com.byu.cs428.subscriptionmanager.view.fragment.adaptor.TabNavbarAdaptor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HomeActivity extends AppCompatActivity implements EditInterface, RemoveInterface, SubscriptionInterface, UpdateSettingsInterface {
    private String LOG_TAG = "HOME";
    private String CHANNEL_ID = "subscription_notification";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FloatingActionButton floatingActionButton;

    private boolean launchSubPage = false;
    private Subscription selectedSubscription;
    TabNavbarAdaptor vpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        setContentView(R.layout.activity_home);
        if (extra != null) {
            if (extra.containsKey("cachedUser")) {
                User user = (User) extra.get("cachedUser");
                UserSettings settings = (UserSettings) extra.get("cachedSettings");
                AuthToken authToken = (AuthToken) extra.get("cachedAuthToken");
                selectedSubscription = ConvertJson.Deserialize(extra.getString("cachedSubscription"), Subscription.class);
                Cache.getInstance().setSettings(settings);
                Cache.getInstance().setAuthToken(authToken);
                Cache.getInstance().setUser(user);
                getIntent().removeExtra("cachedUser");
                getIntent().removeExtra("cachedSettings");
                getIntent().removeExtra("cachedAuthToken");
                getIntent().removeExtra("cachedSubscription");
                launchSubPage = true;
            }
        }

        //https://stackoverflow.com/questions/4116110/clearing-intent
        getIntent().replaceExtras(new Bundle());
        getIntent().setAction("");
        getIntent().setData(null);
        getIntent().setFlags(0);

        tabLayout = findViewById(R.id.tl_main_menu);
        viewPager = findViewById(R.id.vp_tab_content);
        tabLayout.setupWithViewPager(viewPager);
        floatingActionButton = findViewById(R.id.fab);

        vpAdapter = new TabNavbarAdaptor(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new SettingsFragment(this), "Settings");
        vpAdapter.addFragment(new FinancialFragment(this), "Finance");
        vpAdapter.addFragment(new SubscriptionFragment(this,this,this), "Home");
        viewPager.setAdapter(vpAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_setting);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_finance);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_home);

        viewPager.setCurrentItem(2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), AddSubscriptionActivity.class);
                someActivityResultLauncher.launch(switchActivityIntent);
            }
        });

        if(launchSubPage){
            getSubscription(selectedSubscription);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Subscription subscription = ConvertJson.Deserialize(data.getDataString(),Subscription.class);

                        // subscription fragment is in the 2nd place in the array, not the 0th. 0th is settings.
                        SubscriptionFragment subscriptionFragment = (SubscriptionFragment) vpAdapter.getItem(2);
                        subscriptionFragment.addSubscription(subscription);

                        FinancialFragment financialFragment = (FinancialFragment) vpAdapter.getItem(1);
                        financialFragment.addSubscription(subscription);
                    }
                }
            });

    @Override
    public void getSubscription(Subscription subscription) {

        Intent subscriptionActivityIntent = new Intent(getApplicationContext(), SubscriptionActivity.class);
        subscriptionActivityIntent.putExtra("selectedSubscription", subscription);
        getSubscriptionUpdate.launch(subscriptionActivityIntent);
    }

    @Override
    public void deleteSubscription(String notificationId) {
        FinancialFragment fragment2 = (FinancialFragment) vpAdapter.getItem(1);
        fragment2.removeSubscription(notificationId);
    }

    @Override
    public void RemoveSubscription(String notificationId){
        FinancialFragment fragment2 = (FinancialFragment) vpAdapter.getItem(1);
        fragment2.removeSubscription(notificationId);
    }

    public void editSubscription(Subscription subscription) {
        FinancialFragment fragment2 = (FinancialFragment) vpAdapter.getItem(1);
        fragment2.updateSubscription(subscription);
    }

    ActivityResultLauncher<Intent> getSubscriptionUpdate = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Subscription subscription = ConvertJson.Deserialize(data.getDataString(),Subscription.class);
                        SubscriptionFragment fragment1 = (SubscriptionFragment) vpAdapter.getItem(2);
                        fragment1.updateSubscription(subscription);

                        FinancialFragment fragment2 = (FinancialFragment) vpAdapter.getItem(1);
                        fragment2.updateSubscription(subscription);
                    }
                    if(result.getResultCode() == Activity.RESULT_FIRST_USER){
                        Intent data = result.getData();
                        String subscriptionId = data.getDataString();
                        SubscriptionFragment fragment1 = (SubscriptionFragment) vpAdapter.getItem(2);
                        fragment1.RemoveSubscription(subscriptionId);

//                        FinancialFragment fragment2 = (FinancialFragment) vpAdapter.getItem(1);
//                        fragment2.updateSubscription(subscription);
                    }
                }
            });

    @Override
    public void updateSettings() {
        FinancialFragment financialFragment = (FinancialFragment) vpAdapter.getItem(1);
        financialFragment.updateSettings();
    }

    @Override
    public void displayTestNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int random = ThreadLocalRandom.current().nextInt(0,Cache.getInstance().getSubscriptionList().size());
            Subscription subscription = Cache.getInstance().getSubscriptionList().get(random);
            showNotification(subscription);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void showNotification(Subscription subscription) {

        createNotificationChannel(subscription.getNotificationID());

// Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeActivity.class);
        //TODO: bundle the intent
        intent.putExtra("cachedUser", Cache.getInstance().getUser());
        intent.putExtra("cachedSettings", Cache.getInstance().getSettings());
//        intent.putExtra("cachedSubscriptions", ConvertJson.Serialize(Cache.getInstance().getSubscriptionList()));
        intent.putExtra("cachedAuthToken", Cache.getInstance().getAuthToken());
        intent.putExtra("cachedSubscription", ConvertJson.Serialize(subscription));
//        intent.putExtra("selectedSubscription", subscription);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE | FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(subscription.getName() + " is about to renew!")
                .setContentText(subscription.getName() + " will renew on " + subscription.getRenewalDate().split("T")[0] + " for " + Cache.getInstance().getSettings().getCurrency() + " " + subscription.getCost())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(subscription.getNotificationID().hashCode(), builder.build());
    }

    private void createNotificationChannel(String notificationId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Subscription Notification";
            String description = "Displays subscription notifications when they are about to renew";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}