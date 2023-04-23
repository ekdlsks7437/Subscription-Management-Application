package com.byu.cs428.subscriptionmanager.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.net.SendRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.SubManagerRemoteException;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.EditSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.GetSubscriptionsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.LoginRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.RegisterRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.RemoveSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.SettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.SubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.AddSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.EditSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.GetSubscriptionsResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LoginResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RegisterResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RemoveSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.SettingsResponse;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class SendRequestTest {

    RegisterRequest registerRequest = new RegisterRequest("test@test.com","1");
    RegisterResponse registerResponse = new RegisterResponse(new AuthToken("25736","02-22-2023","UserIdTestThing"),new User("TotallyAwesomeEmail@gmail.com","ThisIsAUniqueID"));
    LoginRequest loginRequest = new LoginRequest("test@test.com","1");

    User responseUser;
    AuthToken responseAuthtoken;

    /*Subscription--------------------------------------------------------------------------------*/
    RenewalType typeC = new RenewalType(RenewalType.RenewalPeriod.CUSTOM);
    RenewalType typeM = new RenewalType(RenewalType.RenewalPeriod.MONTHLY);
    RenewalType typeY = new RenewalType(RenewalType.RenewalPeriod.YEARLY);

    DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
    LocalDateTime currentTime = LocalDateTime.now();
    String startingDate = currentTime.format(format);
    String notificationDate = currentTime.plusMonths(1).format(format);

    UserSettings settings = new UserSettings("7:00","$");

//    Subscription testSub = new Subscription("Netflix",15.49,0,
//            startingDate,"0D1M0Y",typeM,
//            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",0.0);
//    Subscription testSub1 = new Subscription("Youtube",11.99,11.99,
//            currentTime.minusMonths(1).format(format),"0D1M0Y",typeM,
//            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",11.99);
//    Subscription testSub2 = new Subscription("Hulu",95.88,0,
//            currentTime.minusMonths(3).format(format),"0D0M1Y",typeY,
//            currentTime.plusMonths(8).format(format), UUID.randomUUID().toString(),"TempUserId1",0.0);
//    Subscription testSub3 = new Subscription("Amazon Prime",14.99,29.98,
//            currentTime.minusMonths(2).format(format),"0D1M0Y",typeM,
//            currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),"TempUserId1",29.98);
//    Subscription testSub4 = new Subscription("World of warcraft",77.94,77.94,
//            currentTime.minusMonths(7).format(format),"0D6M0Y",typeC,
//            currentTime.plusMonths(5).format(format), UUID.randomUUID().toString(),"TempUserId1",77.94);
//    Subscription testSub5 = new Subscription("Microsoft 365 Personal",69.99,0,
//            currentTime.minusYears(2).format(format), "0D0M1Y",typeY,
//            currentTime.plusYears(1).format(format), UUID.randomUUID().toString(),"TempUserId1",139.98);
    /*End Subscription----------------------------------------------------------------------------*/
    /*
    // This is the simple node server I used to test this first case
    var http = require('http');
    // create a server object:
    http.createServer(function (req, res) {
      console.log(req.headers);
      res.write('{"authToken":{"token":"25736","dateTime":"02-22-2023"},"user":{"email":"TotallyAwesomeEmail@gmail.com","id":"ThisIsAUniqueID"}}'); //write a response to the client
      res.end(); //end the response
    }).listen(8080); // the server object listens on port 8080
     */

    @Test
    public void SendRegisterRequestTest() throws SubManagerRemoteException, IOException {
        SendRequest request = new SendRequest("http://localhost:3001");
        RegisterResponse serverResponse = request.doPost("/user/create", registerRequest, RegisterResponse.class);
        //Auth tokens are now generated randomly on the server side so we can only verify it is not null, as well as
        assertNotNull(serverResponse.getAuthToken().getToken());
        // AuthTokens time is generated on the server, so we can only verify that we got it not its value to a specific time.
        assertNotNull(serverResponse.getAuthToken().getExpireDate());
        assertEquals(registerResponse.getUser().getEmail(),serverResponse.getUser().getEmail());
        // UserId is generated on the server, so we cannot check for a specific value only that we have a complete id.
        assertNotNull(serverResponse.getUser().getId());
        responseUser = serverResponse.getUser();
        responseAuthtoken = serverResponse.getAuthToken();
    }

    @Test
    public void SendLoginRequestTest() throws SubManagerRemoteException, IOException {
        SendRequest request = new SendRequest("http://localhost:3001");
        LoginResponse serverResponse = request.doPost("/user/signin", loginRequest, LoginResponse.class);
        //Auth tokens are now generated randomly on the server side so we can only verify it is not null, as well as
        assertNotNull(serverResponse.getAuthToken().getToken());
        // AuthTokens time is generated on the server, so we can only verify that we got it not its value to a specific time.
        assertNotNull(serverResponse.getAuthToken().getExpireDate());
        assertEquals(registerResponse.getUser().getEmail(),serverResponse.getUser().getEmail());
        // UserId is generated on the server, so we cannot check for a specific value only that we have a complete id.
        assertNotNull(serverResponse.getUser().getId());
//        responseUser = serverResponse.getUser();
//        responseAuthtoken = serverResponse.getAuthToken();
    }

    @Test
    public void GetSubscriptionTest() throws SubManagerRemoteException {
        loginUser();
        GetSubscriptionsRequest subscriptionsRequest = new GetSubscriptionsRequest(responseUser,responseAuthtoken);
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        GetSubscriptionsResponse response = serverRequest.doGet("/subscription/subscriptions",subscriptionsRequest.getHeaders(), GetSubscriptionsResponse.class);
        assertNotNull(response.getSubscriptions());
    }

    @Test
    public void GetAllSubscriptions() throws SubManagerRemoteException {
        addSubscriptions();
        addSettings();
//        loginUser();
        GetSubscriptionsRequest subscriptionsRequest = new GetSubscriptionsRequest(responseUser,responseAuthtoken);
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        GetSubscriptionsResponse response = serverRequest.doGet("/subscription/subscriptions",subscriptionsRequest.getHeaders(), GetSubscriptionsResponse.class);
        List<Subscription> subscriptions = response.getSubscriptions();
        assertNotNull(response.getSubscriptions());
        assertEquals("Netflix",subscriptions.get(0).getName());
        assertEquals("Youtube",subscriptions.get(1).getName());
        assertEquals("Hulu",subscriptions.get(2).getName());
        assertEquals("Amazon Prime",subscriptions.get(3).getName());
        assertEquals("World of warcraft",subscriptions.get(4).getName());
        assertEquals("Microsoft 365 Personal",subscriptions.get(5).getName());
    }

    @Test
    public void editSubscriptionTest() throws SubManagerRemoteException {
        Subscription subscription = addSubscription();
        subscription.setRenewalType(new RenewalType(RenewalType.RenewalPeriod.YEARLY));
        subscription.setName("TestFlix");
        subscription.setCost(50.00);
        subscription.setRenewalPeriod("0","0","1");

        LocalDateTime date = LocalDateTime.parse(subscription.getRenewalDate(), format);
        date = date.plusYears(1).minusDays(24);
        subscription.setRenewalDate(date.format(format).toString());

        SendRequest serverRequest = new SendRequest("http://localhost:3001");

        EditSubscriptionRequest request = new EditSubscriptionRequest(responseAuthtoken,responseUser,subscription);
        EditSubscriptionResponse response = serverRequest.doPost("/subscription/update",request,EditSubscriptionResponse.class);

        assertNotNull(response.getSubscription().getSubscription().userId);
        assertNotNull(response.getSubscription().getSubscription().notificationID);
        assertEquals(subscription.renewalType.period,response.getSubscription().getSubscription().renewalType.period);
        assertEquals(subscription.startingDate,response.getSubscription().getSubscription().startingDate);
        assertEquals(subscription.renewalPeriod,response.getSubscription().getSubscription().renewalPeriod);
        assertEquals(subscription.cost,response.getSubscription().getSubscription().cost,0.0001);
        assertEquals(subscription.spentYTD,response.getSubscription().getSubscription().spentYTD,0.0001);
        assertEquals(subscription.totalSpent,response.getSubscription().getSubscription().totalSpent,0.0001);
    }

    @Test
    public void deleteSubscriptionTest() throws SubManagerRemoteException {
        Subscription subscription = addSubscription();
        SendRequest serverRequest = new SendRequest("http://localhost:3001");

        RemoveSubscriptionRequest request = new RemoveSubscriptionRequest(responseAuthtoken,responseUser,subscription.notificationID);
        RemoveSubscriptionResponse response = serverRequest.doPost("/subscription/delete",request,RemoveSubscriptionResponse.class);
        assertTrue(response.isSuccess());
        assertNotNull(response.getMessage());

//        assertNotNull(response.getSubscription().getSubscription().userId);
//        assertNotNull(response.getSubscription().getSubscription().notificationID);
//        assertEquals(subscription.renewalType.period,response.getSubscription().getSubscription().renewalType.period);
//        assertEquals(subscription.startingDate,response.getSubscription().getSubscription().startingDate);
//        assertEquals(subscription.renewalPeriod,response.getSubscription().getSubscription().renewalPeriod);
//        assertEquals(subscription.cost,response.getSubscription().getSubscription().cost,0.0001);
//        assertEquals(subscription.spentYTD,response.getSubscription().getSubscription().spentYTD,0.0001);
//        assertEquals(subscription.totalSpent,response.getSubscription().getSubscription().totalSpent,0.0001);
    }

    @Test
    public void SettingsAddTest() throws SubManagerRemoteException {
        addSubscriptions();
        SendRequest serverRequest = new SendRequest("http://localhost:3001");

        AddSettingsRequest request = new AddSettingsRequest(responseUser,responseAuthtoken, settings);
        SettingsResponse response = serverRequest.doPost("/setting/create",request, SettingsResponse.class);

        assertTrue(response.isSuccess());
        assertEquals("Successfully added the user's settings", response.getMessage());
        assertEquals(settings,response.getUserSettings().getUserSettings());
    }

    @Test
    public void SettingsGetTest() throws SubManagerRemoteException {
        addSubscriptions();
        addSettings();
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        SettingsRequest request = new SettingsRequest(responseUser,responseAuthtoken);
        SettingsResponse response = serverRequest.doGet("/setting/settings",request.getHeaders(), SettingsResponse.class);


        assertTrue(response.isSuccess());
        assertEquals("Successfully got the user's setting", response.getMessage());
        assertEquals(settings.getNotificationTime(),response.getUserSettings().getUserSettings().getNotificationTime());
        assertEquals(settings.getCurrency(),response.getUserSettings().getUserSettings().getCurrency());
    }

    @Test
    public void SettingsUpdateTest() throws SubManagerRemoteException {
        addSubscriptions();
        addSettings();
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        UserSettings settings1 = new UserSettings("8:00", "#");
        AddSettingsRequest request = new AddSettingsRequest(responseUser,responseAuthtoken, settings1);
        SettingsResponse response = serverRequest.doPost("/setting/update",request, SettingsResponse.class);

        assertTrue(response.isSuccess());
        assertEquals("Successfully updated the user's setting", response.getMessage());
        assertEquals(settings1.getNotificationTime(),response.getUserSettings().getUserSettings().getNotificationTime());
        assertEquals(settings1.getCurrency(),response.getUserSettings().getUserSettings().getCurrency());
    }

    @Test
    public void SendSubscriptionRequestTest() throws SubManagerRemoteException {
        //loginUser();
        registerUser();
        Subscription testSubAdd = new Subscription("Netflix",15.49,0,
                startingDate,"0D1M0Y",typeM,
                currentTime.plusMonths(1).format(format), UUID.randomUUID().toString(),responseUser.getId(),0.0);
        AddSubscriptionRequest subRequest = new AddSubscriptionRequest(responseAuthtoken,responseUser,testSubAdd);
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        AddSubscriptionResponse response = serverRequest.doPost("/subscription/create",subRequest, AddSubscriptionResponse.class);
        assertEquals(testSubAdd.name,response.getSubscription().name);
        //assertEquals(testSub.userId,response.getSubscription().userId);
        assertNotNull(response.getSubscription().userId);
        assertNotNull(response.getSubscription().notificationID);
        assertEquals(testSubAdd.renewalType.period,response.getSubscription().renewalType.period);
        assertEquals(testSubAdd.startingDate,response.getSubscription().startingDate);
        assertEquals(testSubAdd.renewalPeriod,response.getSubscription().renewalPeriod);
        assertEquals(testSubAdd.cost,response.getSubscription().cost,0.0001);
        assertEquals(testSubAdd.spentYTD,response.getSubscription().spentYTD,0.0001);
        assertEquals(testSubAdd.totalSpent,response.getSubscription().totalSpent,0.0001);

    }

    private void addSettings() throws SubManagerRemoteException {
        SendRequest serverRequest = new SendRequest("http://localhost:3001");

        AddSettingsRequest request = new AddSettingsRequest(responseUser,responseAuthtoken, settings);
        SettingsResponse response = serverRequest.doPost("/setting/create",request, SettingsResponse.class);
    }

    private void loginUser() throws SubManagerRemoteException {
        SendRequest request = new SendRequest("http://localhost:3001");
        LoginResponse serverResponse = request.doPost("/user/signin", loginRequest, LoginResponse.class);
        responseUser = serverResponse.getUser();
        responseAuthtoken = serverResponse.getAuthToken();
    }

    private void registerUser() throws SubManagerRemoteException {
        SendRequest request = new SendRequest("http://localhost:3001");
        RegisterResponse serverResponse = request.doPost("/user/create", registerRequest, RegisterResponse.class);
        responseUser = serverResponse.getUser();
        responseAuthtoken = serverResponse.getAuthToken();
    }

    private void addSubscriptions() throws SubManagerRemoteException {
        registerUser();
        Subscription testSub = new Subscription("Netflix",15.49,0,
                currentTime.minusMonths(1).minusDays(6).format(format),"0D1M0Y",typeM,
                currentTime.plusMonths(1).minusDays(6).format(format), UUID.randomUUID().toString(),responseUser.getId(),0.0);
        Subscription testSub1 = new Subscription("Youtube",11.99,11.99,
                currentTime.minusMonths(1).minusDays(2).format(format),"0D1M0Y",typeM,
                currentTime.plusMonths(1).minusDays(2).format(format), UUID.randomUUID().toString(),responseUser.getId(),11.99);
        Subscription testSub2 = new Subscription("Hulu",95.88,0,
                currentTime.minusMonths(3).minusDays(18).format(format),"0D0M1Y",typeY,
                currentTime.plusMonths(8).minusDays(18).format(format), UUID.randomUUID().toString(),responseUser.getId(),0.0);
        Subscription testSub3 = new Subscription("Amazon Prime",14.99,29.98,
                currentTime.minusMonths(2).minusDays(22).format(format),"0D1M0Y",typeM,
                currentTime.plusMonths(1).minusDays(22).format(format), UUID.randomUUID().toString(),responseUser.getId(),29.98);
        Subscription testSub4 = new Subscription("World of warcraft",77.94,77.94,
                currentTime.minusMonths(7).minusDays(15).format(format),"0D6M0Y",typeC,
                currentTime.plusMonths(5).minusDays(15).format(format), UUID.randomUUID().toString(),responseUser.getId(),77.94);
        Subscription testSub5 = new Subscription("Microsoft 365 Personal",69.99,0,
                currentTime.minusYears(2).format(format), "0D0M1Y",typeY,
                currentTime.plusYears(1).format(format), UUID.randomUUID().toString(),responseUser.getId(),139.98);
        Subscription[] subscriptions = new Subscription[6];
        subscriptions[0] = testSub;
        subscriptions[1] = testSub1;
        subscriptions[2] = testSub2;
        subscriptions[3] = testSub3;
        subscriptions[4] = testSub4;
        subscriptions[5] = testSub5;
        for( int i = 0; i <subscriptions.length; i++){
            AddSubscriptionRequest subRequest = new AddSubscriptionRequest(responseAuthtoken,responseUser,subscriptions[i]);
            SendRequest serverRequest = new SendRequest("http://localhost:3001");
            AddSubscriptionResponse response = serverRequest.doPost("/subscription/create",subRequest, AddSubscriptionResponse.class);
        }
    }

    private Subscription addSubscription() throws SubManagerRemoteException {
        registerUser();
        Subscription testSub = new Subscription("Netflix",15.49,0,
                currentTime.minusMonths(1).minusDays(6).format(format),"0D1M0Y",typeM,
                currentTime.plusMonths(1).minusDays(6).format(format), UUID.randomUUID().toString(),responseUser.getId(),0.0);
        AddSubscriptionRequest subRequest = new AddSubscriptionRequest(responseAuthtoken,responseUser,testSub);
        SendRequest serverRequest = new SendRequest("http://localhost:3001");
        AddSubscriptionResponse response = serverRequest.doPost("/subscription/create",subRequest, AddSubscriptionResponse.class);
        return response.getSubscription();
    }

//    @Test
//    public void SendLoginRequestTest(){
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put()
//
//        SendRequest request = new SendRequest("http://localhost:3001");
//        LoginRepsonse loginRepsonse = request.doGet("/signin", loginRequest, LoginRepsonse.class);
//        assertNotNull(loginRepsonse.getAuthToken().getToken());
//        // AuthTokens time is generated on the server, so we can only verify that we got it not its value to a specific time.
//        assertNotNull(loginRepsonse.getAuthToken().getExpireDate());
//        assertEquals(registerResponse.getUser().getEmail(),loginRepsonse.getUser().getEmail());
//        // UserId is generated on the server, so we cannot check for a specific value only that we have a complete id.
//        assertNotNull(loginRepsonse.getUser().getId());
//    }
}
