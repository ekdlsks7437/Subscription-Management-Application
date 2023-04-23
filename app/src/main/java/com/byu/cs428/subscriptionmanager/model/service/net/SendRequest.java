package com.byu.cs428.subscriptionmanager.model.service.net;

import static com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson.Deserialize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class SendRequest{
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "application/json";
    private static final int TIMEOUT_MIL = 60000;
    private final String baseUrl;

    public SendRequest(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public <T>T doGet(String urlPath, Map<String, String> headers, Class<T> returnType){
        try {
            URL url = new URL(baseUrl+urlPath);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent",USER_AGENT);
            connection.setReadTimeout(TIMEOUT_MIL);
//            connection.setRequestProperty("Accept","text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
            connection.setRequestProperty( "accept" ,"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");
            if(headers != null){
                for(String headerKey: headers.keySet()){
                    connection.setRequestProperty(headerKey,headers.get(headerKey));
                }
            }

            int responseCode = connection.getResponseCode();
            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                    return Deserialize(getResponse(connection.getInputStream()), returnType);
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new RuntimeException("Bad request:" + getErrorResponse(connection.getErrorStream()));
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    throw new RuntimeException("Internal Server error:"+getErrorResponse(connection.getErrorStream()));
                default:
                    throw new RuntimeException("Invalid response code: " + responseCode);
            }
        }catch (IOException ex){
            System.out.println("Error with the http server: "+ ex);
        }

        return null;
    }

    public <T>T doPost(String urlPath, Object obj, Class<T> returnType) throws SubManagerRemoteException{
        try {
            URL url = new URL(baseUrl+urlPath);
            String reqBody = ConvertJson.Serialize(obj);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent",USER_AGENT);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setReadTimeout(TIMEOUT_MIL);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(reqBody.getBytes());
            outputStream.flush();
            outputStream.close();
            connection.connect();

            int responseCode = connection.getResponseCode();

            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_CREATED:
                    return Deserialize(getResponse(connection.getInputStream()), returnType);
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    throw new RuntimeException("Bad request:" + getErrorResponse(connection.getErrorStream()));
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    ErrorResponse errorResponse = getErrorResponse(connection.getErrorStream());
                    throw new SubManagerRemoteException(errorResponse.message,errorResponse.errorType);
                default:
                    throw new RuntimeException("Unknown error. http response code = " + connection.getResponseCode());
            }
        }catch (IOException ex){
            throw new RuntimeException("Error with the http server: " + ex.getMessage());
        }
    }


    private String getResponse(InputStream inputStream) throws IOException{
        if(inputStream == null){
            return null;
        }else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))){
                String inputLine;
                StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                return response.toString();
            }
        }
    }

    private ErrorResponse getErrorResponse(InputStream stream) throws IOException{
        String responseString = getResponse(stream);
        if(responseString != null){
            return Deserialize(responseString,ErrorResponse.class);
        }
        throw new RuntimeException("Unable to get response. No response message returned from the server.");
    }

    private static class ErrorResponse{
        private boolean success;
        private String message;
        private String errorType;
//        private List<String> stackTrace;
    }
}
