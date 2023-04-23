package com.byu.cs428.subscriptionmanager.view.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.view.SubscriptionInterface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinancialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinancialFragment extends Fragment implements SubscriptionPresenter.SubscriptionView{

    private final String LOG_TAG = "SubscriptionFragment";
    private Toast infoToast;

    List<Subscription> subscriptions;

    View view;

    SubscriptionPresenter presenter;

    SubscriptionInterface subscriptionListener;

    PieChart pieChart;
    TextView tv_spentYtd_price;
    TextView tv_total_spent_price;
    TextView tv_total_spent_monthly_price;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FinancialFragment() {
        // Required empty public constructor
    }

    public FinancialFragment(SubscriptionInterface subscriptionListener) {
        this.subscriptionListener = subscriptionListener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinancialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinancialFragment newInstance(String param1, String param2) {
        FinancialFragment fragment = new FinancialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new SubscriptionPresenter(this, Cache.getInstance().getUser(),Cache.getInstance().getAuthToken());
        presenter.getSubscriptions(Cache.getInstance().getUser(),Cache.getInstance().getAuthToken());

        view = inflater.inflate(R.layout.fragment_financial, container, false);

        pieChart = view.findViewById(R.id.pie_chart);
        tv_spentYtd_price = view.findViewById(R.id.tv_spentYtd_price);
        tv_total_spent_price = view.findViewById(R.id.tv_total_spent_price);
        tv_total_spent_monthly_price = view.findViewById(R.id.tv_total_spent_monthly_price);

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
        this.subscriptions = subscriptions;

        if (subscriptions == null){
            return;
        }

        subscriptions = Cache.getInstance().getSubscriptionList();

        List<PieEntry> entries = new ArrayList<>();

        float totalSubscriptionCost = 0;
        for (int i=0; i<subscriptions.size(); i++) {
            totalSubscriptionCost += subscriptions.get(i).cost;
        }
        tv_total_spent_monthly_price.setText(Cache.getInstance().getSettings().getCurrency() + " " + Float.toString(totalSubscriptionCost));
        for (int i=0; i<subscriptions.size(); i++) {
            PieEntry pieEntry = new PieEntry((float) (subscriptions.get(i).cost / totalSubscriptionCost), subscriptions.get(i).getName());
            pieEntry.setData(subscriptions.get(i));
            entries.add(pieEntry);

        }

        int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY,Color.DKGRAY};
        PieDataSet dataSet = new PieDataSet(entries, "Pie Chart");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(20);

        PieData data = new PieData(dataSet);

        pieChart.setData(data);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                subscriptionListener.getSubscription((Subscription) e.getData());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        pieChart.invalidate(); // refresh the chart

    }

    @Override
    public void updateSubscription(Subscription subscription) {
        for(int i = 0 ; i < subscriptions.size(); i++){
            if(subscriptions.get(i).notificationID.equals(subscription.getNotificationID())){
                subscriptions.remove(i);
                subscriptions.add(subscription);
                break;
            }
        }
        loadedSubscriptions(subscriptions);
    }

    @Override
    public void removeSubscription(String subscriptionId) {
        for(int i = 0 ; i < subscriptions.size(); i++){
            if(subscriptions.get(i).notificationID.equals(subscriptionId)){
                subscriptions.remove(i);
                break;
            }
        }
        loadedSubscriptions(subscriptions);
    }

    public void addSubscription(Subscription subscription){
        if(subscriptions == null) {
            subscriptions = new ArrayList<>();
        }
        subscriptions.add(subscription);
        Cache.getInstance().setSubscriptionList(subscriptions);
        loadedSubscriptions(subscriptions);
    }

    public void updateSettings() {
        String test = Cache.getInstance().getSettings().getCurrency();
        if (!tv_total_spent_monthly_price.getText().toString().equals("")) {
            String spent = (String) tv_total_spent_monthly_price.getText().subSequence(1, tv_total_spent_monthly_price.getText().length());
            tv_total_spent_monthly_price.setText(Cache.getInstance().getSettings().getCurrency() + spent);
        }
        else {
            tv_total_spent_monthly_price.setText(Cache.getInstance().getSettings().getCurrency() + "0.00");
        }
    }
}