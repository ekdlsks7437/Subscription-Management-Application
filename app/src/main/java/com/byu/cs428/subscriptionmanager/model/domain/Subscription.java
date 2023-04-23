package com.byu.cs428.subscriptionmanager.model.domain;

import static java.time.LocalDate.parse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class Subscription implements Serializable {

    /**
     * Unique User id to relate to User
     */
    public String userId;

    /**
     * The name of the subscription
     */
    public String name;
    /**
     * how much the subscription costs for the given renewal period
     */
    public double cost;
    /**
     *  how much has been spent from renewing
     */
    public double totalSpent;
    /**
     * String representation of the starting date of the subscription
     *
     */
    public String startingDate;
    /**
     * number of days/months/years between renewal on the subscription
     * Stored as int then letter, ex. "1D2M3Y" would be 1 day, 2 months, 3 years.
     */
    public String renewalPeriod;
    /**
     * type of renewal as a MONTHLY, YEARLY, or CUSTOM
     */
    public RenewalType renewalType;

    /**
     * String representation of the date and time to send the notification.
     */
    public String renewalDate;
    /**
     * The Unique identifier used to schedule the notification with the device
     */
    public String notificationID;

    //TODO: add Spent YTD
    public Double spentYTD;

    public Subscription(){}

    /***
     *
     * @param name name of the subscription
     * @param cost cost of the subscription
     * @param totalSpent amount spent on the subscription
     * @param startingDate datetime as a string for the initial starting date of the subscription
     * @param renewalPeriod String representing days/months/years till renewal as "0D1M0Y" being 0 days, 1 month, o years.
     * @param renewalType type of renewal period being MONTHLY, YEARLY, and CUSTOM
     * @param renewalDate date for the notification with the specified notification time added to it
     * @param notificationID Unique id for the notification so it can be edited/canceled
     */
    public Subscription(String name, double cost, double totalSpent, String startingDate,
                        String renewalPeriod, RenewalType renewalType, String renewalDate, String notificationID, String userId, double spentYTD) {
        this.name = name;
        this.cost = cost;
        this.totalSpent = totalSpent;
        this.startingDate = startingDate;
        this.renewalPeriod = renewalPeriod;
        this.renewalType = renewalType;
        this.renewalDate = renewalDate;
        this.notificationID = notificationID;
        this.userId = userId;
        this.spentYTD = spentYTD;
    }

    /**
     * Gets the renewal date for the subscription.
     *
     * @return string containing the date of the renewal
     */
    public String getUpdatedRenewalDate(){
        // get the date of the notification
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(renewalDate,format);
        int[] renewalTimes = getRenewalTimes();
        // return the date as the string for now.
        if(renewalTimes[2]!=0) {
            date.plusYears(renewalTimes[2]);
        }
        if(renewalTimes[1]!=0) {
            date.plusMonths(renewalTimes[1]);
        }
        if(renewalTimes[0]!=0) {
            date.plusDays(renewalTimes[0]);
        }

        return date.toString();
    }

    /***
     * Calculates the number of payments that have been made since the initial start date to the
     * current local time.
     */
    public void calculateNumberOfPayments(){
        // format the date and time
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(startingDate,format);
        int[] renewalTimes = getRenewalTimes();
        // while given starting date is less then the current date to the day, iterate through adding
        // adding the cost till its all added.
        while (date.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))){
            this.totalSpent += cost;
            this.totalSpent = Math.round(this.totalSpent * 100.0) / 100.0;
            if(renewalTimes[2]!=0) {
                date = date.plusYears(renewalTimes[2]);
            }
            if(renewalTimes[1]!=0) {
                date = date.plusMonths(renewalTimes[1]);
            }
            if(renewalTimes[0]!=0) {
                date = date.plusDays(renewalTimes[0]);
            }
        }
    }

    /**
     * Updates the renewalDate to the new renewal date.
     */
    public void SetUpdatedRenewalDate(){
        this.renewalDate = getUpdatedRenewalDate();
    }

    /***
     * Splits up the RenewalPeriod from days, months, and years into their respective values
     * @return int[] with it in the order of Days / Months / Years
     */
    public int[] getRenewalTimes(){
        int[] time = new int[3];
        String tempDate = renewalPeriod;
        // splits the string and takes the first part that matches the regex for D then M then Y and
        // cuts off the first date each time, then splits it off the tempDate to iterate through it all
        time[0] =Integer.parseInt( tempDate.split("D")[0]);
        tempDate = tempDate.split("D")[1];
        time[1] =Integer.parseInt( tempDate.split("M")[0]);
        tempDate = tempDate.split("M")[1];
        time[2] =Integer.parseInt( tempDate.split("Y")[0]);
        return time;
    }

    public int getDaysTillRenewal(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(renewalDate,format);
        return (int) LocalDateTime.now().until(date, ChronoUnit.DAYS);
    }

    public String getTimeTillRenewalString(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(renewalDate,format);
        LocalDateTime currentDate = LocalDateTime.now();
        String renewalString = "Renews in ";
        int years = (int) LocalDateTime.now().until(date, ChronoUnit.YEARS);
        if(years > 0){
            if (years > 1){
                renewalString += years + " years ";
            }
            else {
                renewalString += years + " year ";
            }
        }
        date = date.minusYears(years);
        int months = (int) LocalDateTime.now().until(date, ChronoUnit.MONTHS);
        if(months > 0){
            if (months > 1){
                renewalString += months + " months ";
            }
            else {
                renewalString += months + " month ";
            }
        }
        date = date.minusMonths(months);
        int days = (int) LocalDateTime.now().until(date, ChronoUnit.DAYS);
        if(days > 0){
            if ((int) LocalDateTime.now().until(date, ChronoUnit.DAYS) > 1){
                renewalString += days + " days";
            }
            else {
                renewalString += days + " day";
            }
        }

        return renewalString;
    }

    public int getCostMonthly(){
        int totalCost = 0;
        int[] renewalTimes = getRenewalTimes();
        if(renewalTimes[1]  >0){
            totalCost += cost / renewalTimes[1];
        }
        if(renewalTimes[2]  >0){
            totalCost += cost / (renewalTimes[2] * 12);
        }
        return totalCost;
    }

    public String getTimeTillRenewalStringNoRenew(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(renewalDate,format);
        LocalDateTime currentDate = LocalDateTime.now();
        String renewalString = "";
        int years = (int) LocalDateTime.now().until(date, ChronoUnit.YEARS);
        if(years > 0){
            if (years > 1){
                renewalString += years + " years ";
            }
            else {
                renewalString += years + " year ";
            }
        }
        date = date.minusYears(years);
        int months = (int) LocalDateTime.now().until(date, ChronoUnit.MONTHS);
        if(months > 0){
            if (months > 1){
                renewalString += months + " months ";
            }
            else {
                renewalString += months + " month ";
            }
        }
        date = date.minusMonths(months);
        int days = (int) LocalDateTime.now().until(date, ChronoUnit.DAYS);
        if(days > 0){
            if ((int) LocalDateTime.now().until(date, ChronoUnit.DAYS) > 1){
                renewalString += days + " days";
            }
            else {
                renewalString += days + " day";
            }
        }

        return renewalString;
    }
    public String getRenewalPeriod() {
        return renewalPeriod;
    }

    public void setRenewalPeriod(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(renewalDate,format);
        LocalDateTime currentDate = LocalDateTime.parse(startingDate,format);
        String renewalString = "";
        int years = (int) currentDate.until(date, ChronoUnit.YEARS);
        if(years > 0){
                renewalString += years + "D";
        }
        date = date.minusYears(years);
        int months = (int) currentDate.until(date, ChronoUnit.MONTHS);
        if(months > 0){
                renewalString += months + "M";
        }
        date = date.minusMonths(months);
        int days = (int) currentDate.until(date, ChronoUnit.DAYS);
        if(days > 0){
                renewalString += days + "Y";
        }
        setRenewalPeriod(renewalString);
    }

    public void setRenewalPeriod(String days, String months, String years){
        this.renewalPeriod = days+"D"+months+"M"+years+"Y";
    }

    public void setNewRenewalDate(String oldRenewalDate){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime startingDate = LocalDateTime.parse(getStartingDate(),format);
        LocalDateTime oldRenewalDatetime = LocalDateTime.parse(oldRenewalDate,format);
        LocalDateTime curRenewalDate = LocalDateTime.parse(getRenewalDate(),format);
        LocalDateTime currentDate = LocalDateTime.now();

        int[] times = getRenewalTimes();

        curRenewalDate = curRenewalDate.minusDays(currentDate.until(oldRenewalDatetime,ChronoUnit.DAYS));
        curRenewalDate = currentDate.plusDays(times[0]).plusMonths(times[1]).plusYears(times[2]);
        this.renewalDate = curRenewalDate.format(format);
        /*
         * current date 2022-3-23
         * started      2022-1-1
         * renews       2023-4-1
         * daystillren  renewal - current = 8
         * 0 d 1 m 0 y
         *
         * so curRenewalDate - daystillrenewal =
         *
         */
    }

    public void setRenewalDate(String days, String months, String years){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        LocalDateTime date = LocalDateTime.parse(startingDate,format);
        date = date.plusDays((Integer.parseInt(days))).plusMonths((Integer.parseInt(months))).plusYears((Integer.parseInt(years)));

        this.renewalDate = date.toString();
    }

    public void setRenewalPeriod(String renewalPeriod) {
        this.renewalPeriod = renewalPeriod;
    }

    public RenewalType getRenewalType() {
        return renewalType;
    }

    public void setRenewalType(RenewalType renewalType) {
        this.renewalType = renewalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(String renewalDate) {
        this.renewalDate = renewalDate;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getSpentYTD() {
        return spentYTD;
    }

    public void setSpentYTD(Double spentYTD) {
        this.spentYTD = spentYTD;
    }
}

