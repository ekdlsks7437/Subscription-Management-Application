package com.byu.cs428.subscriptionmanager.model.domain;

import org.junit.Test;

import static org.junit.Assert.*;

import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SubscriptionUnitTest extends Subscription {

    RenewalType typeC = new RenewalType(RenewalType.RenewalPeriod.CUSTOM);
    RenewalType typeM = new RenewalType(RenewalType.RenewalPeriod.MONTHLY);
    RenewalType typeY = new RenewalType(RenewalType.RenewalPeriod.YEARLY);

    DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
    LocalDateTime currentTime = LocalDateTime.now();
    String startingDate = currentTime.format(format);
    String notificationDate = currentTime.plusMonths(1).format(format);

    Subscription testSub = new Subscription("Netflix",15.49,0,
            startingDate,"0D1M0Y",typeM,
            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",0.0);
    Subscription testSub1 = new Subscription("Youtube",11.99,11.99,
            currentTime.minusMonths(1).format(format),"0D1M0Y",typeM,
            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",11.99);
    Subscription testSub2 = new Subscription("Hulu",95.88,0,
            currentTime.minusMonths(3).format(format),"0D0M1Y",typeY,
            currentTime.plusMonths(8).format(format), UUID.randomUUID().toString(),"TempUserId1",0.0);
    Subscription testSub3 = new Subscription("Amazon Prime",14.99,29.98,
            currentTime.minusMonths(2).format(format),"0D1M0Y",typeM,
            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",29.98);
    Subscription testSub4 = new Subscription("World of warcraft",77.94,77.94,
            currentTime.minusMonths(7).format(format),"0D6M0Y",typeC,
            currentTime.plusMonths(5).format(format), UUID.randomUUID().toString(),"TempUserId1",77.94);
    Subscription testSub5 = new Subscription("Microsoft 365 Personal",69.99,0,
            currentTime.minusYears(2).format(format), "0D0M1Y",typeY,
            currentTime.plusYears(1).format(format), UUID.randomUUID().toString(),"TempUserId1",139.98);

    @Test
    public void subscriptionGetUpdatedRenewalDateCorrect(){
        //since we are using the RenewalDate to schedule notifications we can check that it will properly update to a new
        // month when given a new date.
        String renewalDay = testSub.getUpdatedRenewalDate();
        String renewalDayPlus1 = currentTime.plusMonths(1).format(format);
        assertEquals(renewalDay, renewalDayPlus1);
    }

    @Test
    public void subscriptionGetDaysTillRenewalCorrect(){
        // test it against the length of the month -1 to get how many days it will take to renew since
        // it is set as a one month subscription right now.
        assertEquals(testSub.getDaysTillRenewal(), LocalDate.now().lengthOfMonth()-1);
    }

    @Test
    public void subscriptionCalculateNumberOfPaymentsCorrect(){
        testSub.setStartingDate(currentTime.minusMonths(6).format(format));
        testSub.calculateNumberOfPayments();
        assertEquals(89.94,testSub.getTotalSpent(), 0.0001);

        testSub.setStartingDate(currentTime.minusMonths(1).format(format));
        testSub.setTotalSpent(0);
        testSub.calculateNumberOfPayments();
        assertEquals(14.99,testSub.getTotalSpent(), 0.0001);
    }

//    @Test
//    public void createJsonString(){
//        System.out.println(ConvertJson.Serialize(testSub));
//        System.out.println(ConvertJson.Serialize(testSub1));
//        System.out.println(ConvertJson.Serialize(testSub2));
//        System.out.println(ConvertJson.Serialize(testSub3));
//        System.out.println(ConvertJson.Serialize(testSub4));
//        System.out.println(ConvertJson.Serialize(testSub5));
//    }

    @Test
    public void addSubscription(){
        System.out.println(ConvertJson.Serialize(testSub));
        System.out.println(ConvertJson.Serialize(testSub1));
        System.out.println(ConvertJson.Serialize(testSub2));
        System.out.println(ConvertJson.Serialize(testSub3));
        System.out.println(ConvertJson.Serialize(testSub4));
        System.out.println(ConvertJson.Serialize(testSub5));
    }

    @Test
    public void getSubscriptions(){
        System.out.println(ConvertJson.Serialize(testSub));
        System.out.println(ConvertJson.Serialize(testSub1));
        System.out.println(ConvertJson.Serialize(testSub2));
        System.out.println(ConvertJson.Serialize(testSub3));
        System.out.println(ConvertJson.Serialize(testSub4));
        System.out.println(ConvertJson.Serialize(testSub5));
    }
}
