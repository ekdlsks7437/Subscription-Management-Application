package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.R;

public class LoginPresenter extends AuthenticationPresenter{

    public LoginPresenter(AuthenticationView view, String email, String password) {
        super(view, email, password);
    }

    public boolean validateRegistration(String email, String password, String passwordVerify){
        // https://stackoverflow.com/questions/46155/how-can-i-validate-an-email-address-in-javascript
        // Simple regex check just to make sure it is probably a valid email.
        String regexPattern = "\\S+@\\S+\\.\\S+";
        if(email.length() == 0){
            return false;
        }
        else if(password.length() == 0){
            return false;
        }
        else if(passwordVerify.length() == 0){
            return false;
        }
        else if(!email.matches(regexPattern)) {
            return false;
        }
        else if (password.compareTo(passwordVerify) != 0){
            return false;
        }
        return true;
    }

    public int validateRequestStringValue(String email, String password, String passwordVerify){
        String regexPattern = "\\S+@\\S+\\.\\S+";
        if(email.length() == 0){
            return R.string.no_email;
        }
        else if(password.length() == 0){
            return R.string.no_password;
        }
        else if(passwordVerify.length() == 0){
            return R.string.no_password_verify;
        }
        else if(!email.matches(regexPattern)) {
            return R.string.invalid_email;
        }
        else if (password.compareTo(passwordVerify) != 0){
            return R.string.incorrect_verify;
        }
        //TODO: add one to check password length?
        return 0;
    }

    public boolean validateLogin(String email, String password){
        String regexPattern = "\\S+@\\S+\\.\\S+";
        if(email.length() == 0){
            return false;
        }
        else if(password.length() == 0) {
            return false;
        } else if(!email.matches(regexPattern)) {
                return false;
        }
        return true;
    }

    public void registerUser(String email, String password){
        view.displayInformationMessage("Registering Account...");
        userService().RegisterUser(new AuthenticatedObserver(),email,password);
    }

    public void loginUser(String email, String password){
        view.displayInformationMessage("Signing in...");
        userService().LoginUser(new AuthenticatedObserver(),email,password);
    }
}
