package com.byu.cs428.subscriptionmanager.model.service.net;

import com.google.gson.Gson;

public class ConvertJson {
    public static String Serialize(Object requestInfo) {
        return (new Gson()).toJson(requestInfo);
    }

    public static <T> T Deserialize(String value, Class<T> returnType) {
        return (new Gson()).fromJson(value, returnType);
    }
}
