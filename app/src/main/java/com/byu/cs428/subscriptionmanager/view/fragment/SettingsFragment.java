package com.byu.cs428.subscriptionmanager.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.presenter.SettingsPresenter;

public class SettingsFragment extends Fragment implements SettingsPresenter.SettingsView {
    private final String LOG_TAG = "SubscriptionFragment";
    private Toast infoToast;
    View view;
    private Spinner notificationSpinner;
    private Spinner currencySpinner;
    private Button testNotificationButton;

    private SettingsPresenter presenter;

    private UpdateSettingsInterface settingsListener;

    public SettingsFragment(UpdateSettingsInterface settingsListener){
        this.settingsListener = settingsListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new SettingsPresenter(this);

        view = inflater.inflate(R.layout.activity_settings,container,false);

        testNotificationButton = (Button) view.findViewById(R.id.test_notification);
        notificationSpinner = (Spinner) view.findViewById(R.id.spinner_notification_time);
        currencySpinner = (Spinner) view.findViewById(R.id.selected_currency_spinner);

        ArrayAdapter<CharSequence> notificationAdapter = ArrayAdapter.createFromResource(getContext(),R.array.notification_times, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        notificationAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        notificationSpinner.setAdapter(notificationAdapter);

        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(getContext(),R.array.selected_currency, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        currencyAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);

        notificationSpinner.setSelection(notificationAdapter.getPosition(Cache.getInstance().getSettings().getNotificationTime()));
        currencySpinner.setSelection(currencyAdapter.getPosition(Cache.getInstance().getSettings().getCurrency()));

        notificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        testNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsListener.displayTestNotification();
            }
        });

        return view;

    }

    private void updateSettings(){
        presenter.editSettings(notificationSpinner.getSelectedItem().toString(),currencySpinner.getSelectedItem().toString());
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
    public void settingsFinished() {
        settingsListener.updateSettings();
    }
}
