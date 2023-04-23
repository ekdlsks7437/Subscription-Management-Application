package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.UserService;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;
import com.byu.cs428.subscriptionmanager.model.service.observer.AuthenticationObserver;

import java.util.Objects;

public class AuthenticationPresenter extends BasePresenter<AuthenticationPresenter.AuthenticationView>{

    private String email;
    private String password;

    public static interface AuthenticationView extends BaseView{
        void authenticated();
        void displayInvalidLogin();
        void updateErrorTv(String errorString);
    }

    @Override
    protected void failMessage(String failureType, String message) {
        String errorMessage = "Failed to" + failureType + ":" + message;
        view.displayFailureMessage(errorMessage);
    }

    @Override
    protected void exceptionMessage(String exceptionType, Exception ex) {
        String exceptionMessage = "Failed to " + exceptionType + " because of exception: " + ex.getMessage();
        System.out.println(ConvertJson.Serialize(ex));
        if(Objects.equals(ex.getMessage(), "Incorrect email or password")){
            view.displayInvalidLogin();
            return;
        }

        if(Objects.equals( ex.getMessage(), "email already in use.")){
         view.updateErrorTv(ex.getMessage());
         view.displayInvalidLogin();
         return;
        }


        view.displayExceptionMessage(exceptionMessage, ex);
    }

    public AuthenticationPresenter(AuthenticationView view, String email, String password) {
        super(view);
        this.email = email;
        this.password = password;
    }

    public class AuthenticatedObserver extends Observer implements AuthenticationObserver {

        @Override
        public void handleSuccess(User user, AuthToken authToken) {

            //TODO: cache or store the authToken in someway that
            // it can be used in other places
            Cache.getInstance().setUser(user);
            Cache.getInstance().setAuthToken(authToken);

            view.authenticated();
        }

        @Override
        protected String getDescription() {
            return "authenticate";
        }
    }

    public UserService userService(){
        return new UserService();
    }
}
