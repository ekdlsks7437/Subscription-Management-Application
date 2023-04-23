package com.byu.cs428.subscriptionmanager.view.fragment;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.byu.cs428.subscriptionmanager.R;

public class SubscriptionListViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView subscriptionNameTV;
    TextView subscriptionPriceTV;
    TextView subscriptionRenewalPeriodTV;
    ImageView dotsIV;
    Drawable rectangleDrawable;
    LinearLayout layout_background;
    View deleteItemPopupView;

    DisplayMetrics displayMetrics;

    TextView deleteServiceNameTV;
    TextView deleteConfirmButtonTV;
    TextView deleteCancelButtonTV;
    LinearLayout deleteClickedOffPopupLL;

    public SubscriptionListViewHolder(@NonNull View itemView, View deleteItemPopupView, DisplayMetrics displayMetrics) {
        super(itemView);

        // initialize variables
        cardView = itemView.findViewById(R.id.card_view);
        layout_background = itemView.findViewById(R.id.ll_bg);
        subscriptionNameTV = itemView.findViewById(R.id.tv_subscription_list_name);
        subscriptionPriceTV = itemView.findViewById(R.id.tv_subscription_list_price);
        subscriptionRenewalPeriodTV = itemView.findViewById(R.id.tv_subscription_list_renewal_date);
        dotsIV = itemView.findViewById(R.id.img_subscription_list_dots);
        deleteClickedOffPopupLL = deleteItemPopupView.findViewById(R.id.delete_background_dimmed);
        deleteServiceNameTV = deleteItemPopupView.findViewById(R.id.tv_delete_service_name);
        deleteConfirmButtonTV = deleteItemPopupView.findViewById(R.id.tv_delete_confirm_btn);
        deleteCancelButtonTV = deleteItemPopupView.findViewById(R.id.tv_delete_cancel_btn);
        rectangleDrawable = itemView.getBackground();
        this.deleteItemPopupView = deleteItemPopupView;
        this.displayMetrics = displayMetrics;
    }



}
