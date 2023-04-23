package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.SubscriptionService;
import com.byu.cs428.subscriptionmanager.model.service.observer.RemoveObserver;
import com.byu.cs428.subscriptionmanager.model.service.observer.SubscriptionsObserver;
import com.byu.cs428.subscriptionmanager.model.service.observer.UpdateSubscriptionObserver;

import java.util.List;

public class SubscriptionPresenter extends BasePresenter<SubscriptionPresenter.SubscriptionView> {


    private User user;
    private AuthToken authToken;

    @Override
    protected void failMessage(String failureType, String message) {
        String errorMessage = "Failed to" + failureType + ":" + message;
        view.displayFailureMessage(errorMessage);
    }

    @Override
    protected void exceptionMessage(String exceptionType, Exception ex) {
        String exceptionMessage = "Failed to " + exceptionType + " because of exception: " + ex.getMessage();
        view.displayExceptionMessage(exceptionMessage, ex);
    }

    public static interface SubscriptionView extends BaseView{
        void loadedSubscriptions(List<Subscription> subscriptions);
        void updateSubscription(Subscription subscription);
        void removeSubscription(String subscriptionId);
    }

    public SubscriptionPresenter(SubscriptionView view, User user, AuthToken authToken) {
        super(view);
        this.user = user;
        this.authToken = authToken;
    }

    public class SubscriptionObserver extends Observer implements SubscriptionsObserver {

        @Override
        public void handleSuccess(List<Subscription> subscriptions) {
            view.loadedSubscriptions(subscriptions);
        }

        @Override
        protected String getDescription() {
            return "Subscriptions";
        }
    }



    public class RemoveSubscriptionObserver extends Observer implements RemoveObserver {

        @Override
        public void handleSuccess(String subscriptionId) {
            view.removeSubscription(subscriptionId);
        }

        @Override
        protected String getDescription() {
            return "Subscriptions";
        }
    }

    public SubscriptionService subscriptionService(){
        return new SubscriptionService();
    }

    public void getSubscriptions(User user, AuthToken authToken){
        subscriptionService().getSubscription(new SubscriptionObserver(),user,authToken);
    }

    public void removeSubscriptions(User user, AuthToken authToken, String notificationId){
        subscriptionService().removeSubscription(new RemoveSubscriptionObserver(),user,authToken,notificationId);
    }
}
