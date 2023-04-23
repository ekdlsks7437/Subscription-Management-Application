package com.byu.cs428.subscriptionmanager.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.view.EditInterface;
import com.byu.cs428.subscriptionmanager.view.EditSubscriptionActivity;
import com.byu.cs428.subscriptionmanager.view.SubscriptionInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class SubscriptionFragment extends Fragment implements SubscriptionPresenter.SubscriptionView, EditInterface, RemoveInterface, SubscriptionInterface {
    private final String LOG_TAG = "SubscriptionFragment";
    private Toast infoToast;
    // For the header
    private TextView spentYTDTV_price;
    private TextView sortingTV_renewal;
    private TextView sortingTV_cost;

    // For the subscription list
    List<Subscription> subscriptions;

    View view;

    SubscriptionPresenter presenter;

    SubscriptionListAdapter adapter;

    RecyclerView recyclerView;

    private SubscriptionInterface subscriptionListener;
    public EditInterface editListener;
    public RemoveInterface removeListener;

    public SubscriptionFragment(SubscriptionInterface subscriptionListener) {
        this.subscriptionListener = subscriptionListener;
        // Required empty public constructor
    }

    public SubscriptionFragment(EditInterface editListener, RemoveInterface removeListener, SubscriptionInterface subscriptionListener) {
        this.editListener = editListener;
        this.removeListener = removeListener;
        this.subscriptionListener =subscriptionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new SubscriptionPresenter(this, Cache.getInstance().getUser(), Cache.getInstance().getAuthToken());
        presenter.getSubscriptions(Cache.getInstance().getUser(), Cache.getInstance().getAuthToken());

        view = inflater.inflate(R.layout.fragment_subscription,container,false);
        // initialize variables
        spentYTDTV_price = (TextView) view.findViewById(R.id.tv_spentYtd_price);
        sortingTV_renewal = (TextView) view.findViewById(R.id.tv_sorting_renewal);
        sortingTV_cost = (TextView) view.findViewById(R.id.tv_sorting_cost);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // initialize click listener
        sortingTV_renewal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the styles of labels
                sortingTV_renewal.setTypeface(null, Typeface.BOLD);
                sortingTV_cost.setTypeface(null, Typeface.NORMAL);

                // Sort the subscription array by renewal
                Collections.sort(subscriptions, (s1, s2) -> {
                    return s2.getDaysTillRenewal() - s1.getDaysTillRenewal();
                });
                adapter.notifyDataSetChanged();
            }
        });

        sortingTV_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the styles of labels
                sortingTV_renewal.setTypeface(null, Typeface.NORMAL);
                sortingTV_cost.setTypeface(null, Typeface.BOLD);

                // TODO: Sort the subscription array by cost
                Collections.sort(subscriptions, (s1, s2) -> {
                    return (int) (s2.getCost() - s1.getCost());
                });
                adapter.notifyDataSetChanged();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void displayFailureMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG, message);
    }

    @Override
    public void displayInformationMessage(String message) {
        infoToast = Toast.makeText(getContext(),message,Toast.LENGTH_LONG);
        infoToast.show();
    }

    @Override
    public void displayExceptionMessage(String message, Exception exception) {
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG,message,exception);
    }

    @Override
    public void clearInfoMessage() {
        infoToast.cancel();
    }

    @Override
    public void loadedSubscriptions(List<Subscription> subscriptions) {

        double totalSpentYTD = 0.0;
        if(subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                totalSpentYTD += subscription.getSpentYTD();
            }
        }
        spentYTDTV_price.setText(String.valueOf(String.format("%.2f", totalSpentYTD)));

        this.subscriptions = subscriptions;
        Cache.getInstance().setSubscriptionList(subscriptions);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new SubscriptionListAdapter(view.getContext(),subscriptions, presenter, this,this,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateSubscription(Subscription subscription) {
        for (int i = 0; i < subscriptions.size(); i++) {
            if(subscription.getNotificationID().equals(subscriptions.get(i).getNotificationID())){
                subscriptions.remove(i);
                subscriptions.add(i,subscription);
                Cache.getInstance().setSubscriptionList(subscriptions);
                adapter.notifyItemChanged(i);
                editListener.editSubscription(subscription);
                break;
            }
        }
    }

    public void addSubscription(Subscription subscription){
        if(subscriptions == null){
            subscriptions = new ArrayList<>();
        }
        subscriptions.add(subscription);
        adapter.notifyItemInserted(subscriptions.size());
    }

    @Override
    public void removeSubscription(String subscriptionId) {
        for (int i = 0; i < subscriptions.size(); i++) {
            if(subscriptionId.equals(subscriptions.get(i).getNotificationID())){
                //TODO: verify that this works
                subscriptions.remove(i);
                Cache.getInstance().setSubscriptionList(subscriptions);
                //Calling notifyITemRemoved isn't updating the adapter index, so I decided to just
                // notifyDataSetChanged since it works.
//                adapter.notifyItemRemoved(i);
                adapter.notifyDataSetChanged();
                removeListener.RemoveSubscription(subscriptionId);
                break;
            }
        }
    }

    @Override
    public void editSubscription(Subscription subscription) {
        Intent editActivityIntent = new Intent(getContext(), EditSubscriptionActivity.class);
        editActivityIntent.putExtra("subscription", subscription);
        someActivityResultLauncher.launch(editActivityIntent);
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Subscription subscription = ConvertJson.Deserialize(data.getDataString(),Subscription.class);
                        updateSubscription(subscription);
                    }
                }
            });

    @Override
    public void RemoveSubscription(String notificationId) {
        presenter.removeSubscriptions(Cache.getInstance().getUser(), Cache.getInstance().getAuthToken(), notificationId);
    }

    @Override
    public void getSubscription(Subscription subscription) {
        subscriptionListener.getSubscription(subscription);
    }

    @Override
    public void deleteSubscription(String subscriptionId) {
        removeSubscription(subscriptionId);
        subscriptionListener.deleteSubscription(subscriptionId);
    }
}