package com.byu.cs428.subscriptionmanager.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;

public class SubscriptionActivity extends AppCompatActivity {

    private Subscription selectedSubscription;
    private TextView startingDateTV;
    private TextView renewalDateTV;
    private TextView renewsInTV;
    private TextView costTV;
    private TextView spentYTDTV;
    private TextView totalSpentTV;

    private Button deleteBtn;
    private Button editBtn;

    TextView deleteServiceNameTV;
    TextView deleteConfirmButtonTV;
    TextView deleteCancelButtonTV;
    LinearLayout deleteClickedOffPopupLL;

    private boolean isEdited = false;
    private boolean isDelete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        View deletePopupWindowView = getLayoutInflater().inflate(R.layout.delete_popup_window,null);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

//        if(getIntent().getExtras() != null) {
//            if (getIntent().getExtras().containsKey("selectedSubscription")) {
//                selectedSubscription = (Subscription) getIntent().getExtras().get("selectedSubscription");
////                getIntent().removeExtra("selectedSubscription");
//            }else {
//                selectedSubscription = null;
//            }
//        }

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                selectedSubscription = null;
            }
            selectedSubscription = (Subscription) extras.get("selectedSubscription");
        }

        if (getIntent().getExtras().containsKey("selectedSubscription")) {
            getIntent().removeExtra("selectedSubscription");
        }

        deleteClickedOffPopupLL = deletePopupWindowView.findViewById(R.id.delete_background_dimmed);
        deleteServiceNameTV = deletePopupWindowView.findViewById(R.id.tv_delete_service_name);
        deleteConfirmButtonTV = deletePopupWindowView.findViewById(R.id.tv_delete_confirm_btn);
        deleteCancelButtonTV = deletePopupWindowView.findViewById(R.id.tv_delete_cancel_btn);


        // initialize variables
//        toolbar = findViewById(R.id.toolbar);
        startingDateTV = findViewById(R.id.tv_starting_date);
        renewalDateTV = findViewById(R.id.tv_renewal_date);
        renewsInTV = findViewById(R.id.tv_renews_in);
        costTV = findViewById(R.id.tv_subscription_cost);
        spentYTDTV = findViewById(R.id.tv_spent_ytd);
        totalSpentTV = findViewById(R.id.tv_total_spent);
        deleteBtn = findViewById(R.id.btn_subscription_delete);
        editBtn = findViewById(R.id.btn_subscription_edit);
        PopupWindow popupWindow = new PopupWindow(deletePopupWindowView);

        loadData();

        // click listener for buttons
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteServiceNameTV.setText(selectedSubscription.getName());
                popupWindow.setHeight(displayMetrics.heightPixels);
                popupWindow.setWidth(displayMetrics.widthPixels);
                ColorDrawable dw = new ColorDrawable(0xb0000000);
                ColorDrawable dwOff = new ColorDrawable(0x00000000);
                popupWindow.setBackgroundDrawable(dw);
                popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER,0,0);
//                finish();
                deleteConfirmButtonTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.setBackgroundDrawable(dwOff);
                        popupWindow.dismiss();
                        Intent data = new Intent();
                        data.setData(Uri.parse(selectedSubscription.getNotificationID()));
                        setResult(RESULT_FIRST_USER, data);
                        finish();
                    }
                });

                deleteCancelButtonTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.setBackgroundDrawable(dwOff);
                        popupWindow.dismiss();
                    }
                });

                deleteClickedOffPopupLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.setBackgroundDrawable(dwOff);
                        popupWindow.dismiss();
                    }
                });
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchActivityIntent = new Intent(getApplicationContext(), EditSubscriptionActivity.class);
                switchActivityIntent.putExtra("subscription", selectedSubscription);
                someActivityResultLauncher.launch(switchActivityIntent);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Bundle extras = intent.getExtras();
//        if (extras != null) {
//            if (extras.containsKey("selectedSubscription")) {
//                setContentView(R.layout.activity_subscription);
//                // extract the extra-data in the Notification
//                String msg = extras.getString("selectedSubscription");
////                txtView = (TextView) findViewById(R.id.txtMessage);
////                txtView.setText(msg);
//            }
//        }
//    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        selectedSubscription = ConvertJson.Deserialize(data.getDataString(),Subscription.class);
                        loadData();
                        isEdited = true;
                    }
                }
            });

    public void loadData(){
        //TODO: maybe don't split the time here, but it works for now.
        startingDateTV.setText(selectedSubscription.getStartingDate().split("T")[0]);
        renewalDateTV.setText(selectedSubscription.getRenewalDate().split("T")[0]);
        renewsInTV.setText(selectedSubscription.getTimeTillRenewalStringNoRenew());
        costTV.setText(String.valueOf(selectedSubscription.getCost()));
        spentYTDTV.setText(String.valueOf(selectedSubscription.getSpentYTD()));
        totalSpentTV.setText(String.valueOf(selectedSubscription.getTotalSpent()));
        getSupportActionBar().setTitle(selectedSubscription.getName());
    }

    @Override
    public void onBackPressed() {
        if (!isEdited) {
            super.onBackPressed();
        } else {
            Intent data = new Intent();
            String parsedSubscription = ConvertJson.Serialize(selectedSubscription);
            data.setData(Uri.parse(parsedSubscription));
            setResult(RESULT_OK, data);
            finish();
        }
        if (!isDelete) {
            super.onBackPressed();
        } else {
            Intent data = new Intent();
            data.setData(Uri.parse(selectedSubscription.getNotificationID()));
            setResult(RESULT_FIRST_USER, data);
            finish();
        }
    }
}