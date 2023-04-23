package com.byu.cs428.subscriptionmanager.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.view.EditInterface;
import com.byu.cs428.subscriptionmanager.view.SubscriptionActivity;
import com.byu.cs428.subscriptionmanager.view.SubscriptionInterface;

import java.util.List;
import java.util.Locale;

public class SubscriptionListAdapter extends RecyclerView.Adapter<SubscriptionListViewHolder> implements RemoveInterface {


    Context context;
    List<Subscription> subscriptionListItems;
    Locale locale = Locale.getDefault();
    SubscriptionPresenter subscriptionPresenter;

    private EditInterface listener;
    private RemoveInterface removeListener;

    private SubscriptionInterface subscriptionListener;

    public SubscriptionListAdapter(Context context, List<Subscription> subscriptionListItems, SubscriptionPresenter subscriptionPresenter, EditInterface listener,RemoveInterface removeListener,SubscriptionInterface subscriptionListener) {
        this.context = context;
        this.subscriptionListItems = subscriptionListItems;
        this.subscriptionPresenter = subscriptionPresenter;
        this.listener = listener;
        this.removeListener = removeListener;
        this.subscriptionListener = subscriptionListener;
    }

    @NonNull
    @Override
    public SubscriptionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return new SubscriptionListViewHolder(LayoutInflater.from(context).inflate(R.layout.subscription_item_view, parent, false),
                                                LayoutInflater.from(context).inflate(R.layout.delete_popup_window, parent, false),
                                                displayMetrics);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionListViewHolder holder, int position) {
        int correctedPosition = holder.getBindingAdapterPosition();
        //TODO: remove the tempname, using it to test for positions. When you delete one subscription from the second to last item
        // then try and delete the last item the app crashes. I am notifying of the removal I think, but
        // it might need to be updated here.
        String tempName = subscriptionListItems.get(correctedPosition).getName();
        holder.subscriptionNameTV.setText(subscriptionListItems.get(correctedPosition).getName());
        holder.subscriptionPriceTV.setText(Cache.getInstance().getSettings().getCurrency() + " " + String.format(locale,"%.2f",subscriptionListItems.get(position).getCost()));
        holder.subscriptionRenewalPeriodTV.setText(subscriptionListItems.get(correctedPosition).getTimeTillRenewalString());

        if(subscriptionListItems.get(correctedPosition).getRenewalType().getPeriod() == RenewalType.RenewalPeriod.CUSTOM){
            holder.layout_background.setBackgroundColor(context.getResources().getColor(R.color.custom_sub_color));
        } else if (subscriptionListItems.get(correctedPosition).getRenewalType().getPeriod() == RenewalType.RenewalPeriod.YEARLY) {
            holder.layout_background.setBackgroundColor(context.getResources().getColor(R.color.yearly_sub_color));
        } else if (subscriptionListItems.get(correctedPosition).getRenewalType().getPeriod() == RenewalType.RenewalPeriod.MONTHLY) {
            holder.layout_background.setBackgroundColor(context.getResources().getColor(R.color.monthly_sub_color));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscriptionListener.getSubscription(subscriptionListItems.get(position));
//                Intent intent = new Intent(context, SubscriptionActivity.class);
//                intent.putExtra("selectedSubscription", subscriptionListItems.get(position));
//                context.startActivity(intent);
            }
        });

        holder.dotsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context.getApplicationContext(), holder.dotsIV);
                popupMenu.setForceShowIcon(true);

                popupMenu.getMenuInflater().inflate(R.menu.edit_delete_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Edit")) {
//                            Intent editActivityIntent = new Intent(context, EditSubscriptionActivity.class);
//                            editActivityIntent.putExtra("subscription", subscriptionListItems.get(position));
//                            context.startActivity(editActivityIntent);
                            listener.editSubscription(subscriptionListItems.get(correctedPosition));
                        } else if (item.getTitle().equals("Delete")) {
                            PopupWindow popupWindow = new PopupWindow(holder.deleteItemPopupView);
                            holder.deleteServiceNameTV.setText(subscriptionListItems.get(correctedPosition).name);
                            popupWindow.setHeight(holder.displayMetrics.heightPixels);
                            popupWindow.setWidth(holder.displayMetrics.widthPixels);
                            ColorDrawable dw = new ColorDrawable(0xb0000000);
                            ColorDrawable dwOff = new ColorDrawable(0x00000000);
                            popupWindow.setBackgroundDrawable(dw);
                            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER,0,0);


                            holder.deleteConfirmButtonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.setBackgroundDrawable(dwOff);
                                    popupWindow.dismiss();
                                    //TODO: call the method from the subscription presenter to remove the subscription
                                    removeListener.RemoveSubscription(subscriptionListItems.get(correctedPosition).getNotificationID());

                                    //subscriptionPresenter
                                }
                            });

                            holder.deleteCancelButtonTV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.setBackgroundDrawable(dwOff);
                                    popupWindow.dismiss();
                                }
                            });

                            holder.deleteClickedOffPopupLL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.setBackgroundDrawable(dwOff);
                                    popupWindow.dismiss();
                                }
                            });
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(subscriptionListItems == null){
            return 0;
        }
        return subscriptionListItems.size();
    }

    @Override
    public void RemoveSubscription(String notificationId) {

    }
}
