package com.byu.cs428.subscriptionmanager.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.presenter.AddEditSubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.view.EditInterface;
import com.byu.cs428.subscriptionmanager.view.HomeActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubscriptionFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionFormsFragment extends Fragment implements AddEditSubscriptionPresenter.AddEditSubscriptionView, AdapterView.OnItemSelectedListener, EditInterface {

    private final String LOG_TAG = "SubscriptionForumsFragment";
    private Toast infoToast;

    private boolean isEdit;
    private boolean isCustom;
    private View view;
    private EditText et_serviceName;
    private EditText et_cost;
    private TextView tv_monthly;
    private TextView tv_yearly;
    private TextView tv_custom;

    private LinearLayout ll_custom_date_spinners;
    private Spinner spinner_custom_years;
    private Spinner spinner_custom_months;
    private Spinner spinner_custom_days;

    private String selected_custom_years = "0";
    private String selected_custom_months = "0";
    private String selected_custom_days = "0";

    private TextView tv_selected_date;
    private TextView tv_bt_select_date;
    private Button btn_cancel;
    private Button btn_accept;

    private String serviceName;
    private String serviceCost;
    private String selectedRenewablePeriod = "Monthly";
    private String selectedPeriod;

    private Subscription subscription;

    private String oldRenewalPeriod;
    private String oldRenewalDate;

    private AddEditSubscriptionPresenter presenter;

    private EditInterface listener;

    public SubscriptionFormsFragment(Subscription subscription, EditInterface listener) {
        this.listener = listener;
        this.subscription = subscription;
        // Required empty public constructor
    }

    public SubscriptionFormsFragment(Subscription subscription) {
        this.listener = listener;
        this.subscription = subscription;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionFormsFragment.
     */
    public static SubscriptionFormsFragment newInstance(String param1, String param2) {
        SubscriptionFormsFragment fragment = new SubscriptionFormsFragment(new Subscription());
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
        isEdit = subscription.getName() != null;
        if(isEdit) {
            isCustom = subscription.getRenewalType().period.equals(RenewalType.RenewalPeriod.CUSTOM);
        }
        else {
            isCustom = false;
        }

        view = inflater.inflate(R.layout.fragment_subscription_forms, container, false);

        et_serviceName = (EditText) view.findViewById(R.id.edt_service_name);
        et_cost = (EditText) view.findViewById(R.id.edt_service_cost);
        tv_monthly = (TextView) view.findViewById(R.id.tv_period_monthly_button);
        tv_yearly = (TextView) view.findViewById(R.id.tv_period_yearly_button);
        tv_custom = (TextView) view.findViewById(R.id.tv_period_custom_button);
        ll_custom_date_spinners = (LinearLayout) view.findViewById(R.id.ll_custom_date_spinners);
        spinner_custom_years = (Spinner) view.findViewById(R.id.spinner_custom_years);
        spinner_custom_months = (Spinner) view.findViewById(R.id.spinner_custom_months);
        spinner_custom_days = (Spinner) view.findViewById(R.id.spinner_custom_days);
        tv_selected_date = (TextView) view.findViewById(R.id.tv_service_selected_date);
        tv_bt_select_date = (TextView) view.findViewById(R.id.tv_select_date_button);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_accept = (Button) view.findViewById(R.id.btn_accept);

        presenter = new AddEditSubscriptionPresenter(this, Cache.getInstance().getUser(),Cache.getInstance().getAuthToken());

        // Set selectedLister for spinners
        spinner_custom_years.setOnItemSelectedListener(this);
        spinner_custom_months.setOnItemSelectedListener(this);
        spinner_custom_days.setOnItemSelectedListener(this);

        // Setup spinners
        ArrayAdapter<CharSequence> years_adapter = ArrayAdapter.createFromResource(getContext(), R.array.custom_years, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        years_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_custom_years.setAdapter(years_adapter);

        ArrayAdapter<CharSequence> months_adapter = ArrayAdapter.createFromResource(getContext(), R.array.custom_months, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        months_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_custom_months.setAdapter(months_adapter);

        ArrayAdapter<CharSequence> days_adapter = ArrayAdapter.createFromResource(getContext(), R.array.custom_days, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        days_adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_custom_days.setAdapter(days_adapter);
        if(isCustom){
            int[] times = subscription.getRenewalTimes();
            CharSequence days = String.valueOf(times[0]);
            CharSequence months = String.valueOf(times[1]);
            CharSequence years = String.valueOf(times[2]);

            spinner_custom_days.setSelection(days_adapter.getPosition(days));
            spinner_custom_months.setSelection(months_adapter.getPosition(months));
            spinner_custom_years.setSelection(years_adapter.getPosition(years));

            ll_custom_date_spinners.setVisibility(View.VISIBLE);
        }


        // Check for null for the case that they are just adding a new subscription, not
        // editing a already existing subscription.
        et_serviceName.setText(subscription.getName(), TextView.BufferType.EDITABLE);
        et_cost.setText(String.valueOf(subscription.getCost()), TextView.BufferType.EDITABLE);
        tv_selected_date.setText(subscription.getStartingDate());
        resetRenewalTVSelected();
        if (!isEdit){
            DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
            LocalDateTime currentTime = LocalDateTime.now();
            String startingDate = currentTime.format(format);

            subscription.setTotalSpent(0.00f);
            subscription.setSpentYTD(0.0);
            subscription.setUserId(Cache.getInstance().getUser().userId);
            subscription.setStartingDate(startingDate);
            selectedRenewablePeriod = "Monthly";
            subscription.setRenewalType(new RenewalType(RenewalType.RenewalPeriod.MONTHLY));
            subscription.setRenewalPeriod("0D1M0Y");
            subscription.setNotificationID(UUID.randomUUID().toString());

            subscription.setRenewalDate("0","1","0");
        }
        oldRenewalDate = subscription.renewalDate;
        oldRenewalPeriod = subscription.renewalPeriod;

        // Click listeners for 'renewable period' buttons
        tv_monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the spinners
                ll_custom_date_spinners.setVisibility(View.GONE);

                selectedRenewablePeriod = "Monthly";
                subscription.setRenewalType(new RenewalType(RenewalType.RenewalPeriod.MONTHLY));
                subscription.setRenewalPeriod("0D1M0Y");

//                subscription.setRenewalDate("0","1","0");
                resetRenewalTVSelected();
            }
        });

        tv_yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the spinners
                ll_custom_date_spinners.setVisibility(View.GONE);

                selectedRenewablePeriod = "Yearly";
                subscription.setRenewalType(new RenewalType(RenewalType.RenewalPeriod.YEARLY));
                //TODO: maybe show an error if they set the renewal period as empty, right now it
                // will just default to a month even in custom.
                subscription.setRenewalPeriod("0D0M1Y");
                subscription.setRenewalDate("0","0","1");
                resetRenewalTVSelected();
            }
        });

        tv_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the spinners
                ll_custom_date_spinners.setVisibility(View.VISIBLE);

                isCustom = true;
                selectedRenewablePeriod = "Custom";
                subscription.setRenewalType(new RenewalType(RenewalType.RenewalPeriod.CUSTOM));
                resetRenewalTVSelected();

            }
        });

        // Click listener for the 'select date' button
        tv_bt_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tv_selected_date.setText(year + "/" + (month + 1) + "/" + day);
                                selectedPeriod = year + "/" + (month + 1) + "/" + day;
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });

        // Click listeners for the buttons
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName = et_serviceName.getText().toString();
                serviceCost = et_cost.getText().toString();

                subscription.setName(serviceName);
                subscription.setCost(Double.parseDouble(serviceCost));

                // set the custom period
                LocalDateTime currentTime = LocalDateTime.now();
//                subscription.setRenewalPeriod();
                if(isCustom){
                    selected_custom_years = spinner_custom_years.getSelectedItem().toString();
                    selected_custom_months = spinner_custom_months.getSelectedItem().toString();
                    selected_custom_days = spinner_custom_days.getSelectedItem().toString();
                    subscription.setRenewalDate(selected_custom_days,selected_custom_months,selected_custom_years);
                    subscription.setRenewalPeriod(selected_custom_days,selected_custom_months,selected_custom_years);
                }

                if(presenter.validateSubscription(subscription)){
                    if(isEdit){
                        subscription.setNewRenewalDate(oldRenewalDate);
                        presenter.updateSubscription(Cache.getInstance().getUser(), Cache.getInstance().getAuthToken(), subscription);
                    }
                    else {
                        presenter.addSubscription(Cache.getInstance().getUser(), Cache.getInstance().getAuthToken(), subscription);
                    }
                }

                Toast.makeText(getActivity().getApplicationContext(), "Successfully added!", Toast.LENGTH_SHORT).show();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    private void resetRenewalTVSelected(){
        tv_monthly.setTypeface(null, Typeface.NORMAL);
        tv_monthly.setTextColor(Color.BLACK);
        tv_yearly.setTypeface(null, Typeface.NORMAL);
        tv_yearly.setTextColor(Color.BLACK);
        tv_custom.setTypeface(null, Typeface.NORMAL);
        tv_custom.setTextColor(Color.BLACK);
        selectRenewalTV();
    }

    private void selectRenewalTV(){
        RenewalType type = subscription.getRenewalType();
        // if the subscription is null we also se it to the default being monthly, but before we check the period or else it crashes since it is null
        if (type == null || type.getPeriod() == RenewalType.RenewalPeriod.MONTHLY) {
            tv_monthly.setTypeface(null, Typeface.BOLD);
            tv_monthly.setTextColor(ContextCompat.getColor(getContext(),R.color.renewal_period__selected_color));
        } else if (type.getPeriod() == RenewalType.RenewalPeriod.YEARLY) {
            tv_yearly.setTypeface(null, Typeface.BOLD);
            tv_yearly.setTextColor(ContextCompat.getColor(getContext(),R.color.renewal_period__selected_color));
        } else {
            tv_custom.setTypeface(null, Typeface.BOLD);
            tv_custom.setTextColor(ContextCompat.getColor(getContext(),R.color.renewal_period__selected_color));
        }
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
    public void addEditSubscription(Subscription subscription) {

        if (isEdit) {
            editSubscription(subscription);
        } else {
            editSubscription(subscription);
        }
//        getActivity().finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_custom_years:
                selected_custom_years = parent.getSelectedItem().toString();
                break;
            case R.id.spinner_custom_months:
                selected_custom_months = parent.getSelectedItem().toString();
                break;
            case R.id.spinner_custom_days:
                selected_custom_days = parent.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void editSubscription(Subscription subscription) {
        listener.editSubscription(subscription);
    }
}