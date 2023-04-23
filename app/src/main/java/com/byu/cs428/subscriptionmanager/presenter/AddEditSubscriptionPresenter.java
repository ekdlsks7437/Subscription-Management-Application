package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.SubscriptionService;
import com.byu.cs428.subscriptionmanager.model.service.observer.UpdateSubscriptionObserver;

public class AddEditSubscriptionPresenter  extends BasePresenter<AddEditSubscriptionPresenter.AddEditSubscriptionView> {


    private User user;
    private AuthToken authToken;

    public AddEditSubscriptionPresenter(AddEditSubscriptionPresenter.AddEditSubscriptionView view, User user, AuthToken authToken) {
        super(view);
        this.user = user;
        this.authToken = authToken;
    }

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

    public static interface AddEditSubscriptionView extends BaseView{
        // This will be called when the subscription has been added or edited and will
        // close the activity.
        void addEditSubscription(Subscription subscription);
    }

    public SubscriptionService subscriptionService(){
        return new SubscriptionService();
    }

    public void addSubscription(User user, AuthToken authToken, Subscription subscription){
        subscriptionService().addSubscription(new EditSubscriptionObserver(), user, authToken, subscription);
    }

    public void updateSubscription(User user, AuthToken authToken, Subscription subscription){
        subscriptionService().editSubscription(new EditSubscriptionObserver(), user, authToken, subscription);
    }

    public class EditSubscriptionObserver extends Observer implements UpdateSubscriptionObserver {

        @Override
        public void handleSuccess(Subscription subscriptions) {
            view.addEditSubscription(subscriptions);
        }

        @Override
        protected String getDescription() {
            return "Subscriptions";
        }
    }

    public boolean validateSubscription(Subscription subscription){
        if(subscription.getName() == null){
            return false;
        }
        else if(subscription.getStartingDate() == null){
            return false;
        }
        //TODO: think if anything else needs to be checked, I think the user
        // should be able to enter a subscription of 0.0 for cost.
        return true;
    }
}
