package com.byu.cs428.subscriptionmanager.model.domain;

import java.io.Serializable;

public class RenewalType implements Serializable {

    /***
     * renewal period type, Monthly, Yearly, or Custom
     */
    public RenewalPeriod period;

    public enum RenewalPeriod{
        MONTHLY,
        YEARLY,
        CUSTOM
    }

    /***
     * Creates a Renewal object with the given period type.
     * @param period MONTHLY/DAILY/YEARLY time period.
     */
    public RenewalType(RenewalPeriod period){
        this.period = period;
    }

    public RenewalType(String renewalTypeString){
        //TODO: remove this null check, only here for test data.
        if(renewalTypeString != null) {
            this.period = stringToPeriod(renewalTypeString);
        }
       // this.period = stringToPeriod(renewalTypeString);

    }

    public RenewalType.RenewalPeriod getPeriod() {
        return period;
    }

    public void setPeriod(RenewalType.RenewalPeriod period) {
        this.period = period;
    }

    public RenewalPeriod stringToPeriod(String stringPeriod){
        if(stringPeriod.equals("MONTHLY")){
            return RenewalPeriod.MONTHLY;
        }
        if(stringPeriod.equals("YEARLY")){
            return RenewalPeriod.YEARLY;
        }
        if(stringPeriod.equals("CUSTOM")){
            return RenewalPeriod.CUSTOM;
        }
        return null;
    }
}
