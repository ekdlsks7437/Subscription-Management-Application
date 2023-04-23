package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.model.service.observer.ServiceObserver;

public abstract class BasePresenter<T> {

    protected final T view;

    public interface BaseView {
        void displayFailureMessage(String message);

        void displayInformationMessage(String message);

        void displayExceptionMessage(String message, Exception exception);

        void clearInfoMessage();
    }

    public BasePresenter(T view){
        this.view = view;
    }

    protected abstract void failMessage(String failureType, String message);
    protected abstract void exceptionMessage(String exceptionType, Exception ex);


    public abstract class Observer implements ServiceObserver {
        protected abstract String getDescription();

        @Override
        public void handleFailure(String message) {
            failMessage(getDescription(), message);
        }

        @Override
        public void handleException(Exception ex) {
            exceptionMessage(getDescription(), ex);
        }
    }
}
