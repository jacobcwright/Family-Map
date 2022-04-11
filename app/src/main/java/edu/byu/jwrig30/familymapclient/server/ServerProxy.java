package edu.byu.jwrig30.familymapclient.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

/**
 * Proxy for Family Map Server
 */
public class ServerProxy {
    // implement services using HttpURLConnection

    /*
     * Serialize request as JSON
     * Send HTTP request to server
     * Receive Response as JSON
     * De-serialize to result and return it
     */

    public LoginResult login(LoginRequest req, String serverHost, String serverPort){
        Gson gson = new Gson();
        try{
            // create url and open connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Post request and has request body
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqData = gson.toJson(req);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Login Success");
                // get result and return it
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getInputStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                LoginResult result = gson.fromJson(respData, LoginResult.class);
                return result;

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                // get result and return it
                LoginResult result = gson.fromJson(respData, LoginResult.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in catch statement");
            System.out.println(e.getMessage());
            LoginResult result = new LoginResult(false, "Failed to login");
            return result;
        }
    }

    public RegisterResult register(RegisterRequest req, String serverHost, String serverPort){
        Gson gson = new Gson();
        try{
            // create url and open connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Post request and has request body
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            // accepts json
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            // serialize object into json and send request
            String reqData = gson.toJson(req);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            // get results
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Register success");
                // get result and return it
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getInputStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                RegisterResult result = gson.fromJson(respData, RegisterResult.class);
                return result;

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                // get result and return it
                RegisterResult result = gson.fromJson(respData, RegisterResult.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in catch statement");
            System.out.println(e.getMessage());
            RegisterResult result = new RegisterResult(false, "Failed to register");
            return result;
        }
    }

    public PersonResult getPeople(PersonRequest req, String serverHost, String serverPort){
        Gson gson = new Gson();
        try{
            // create url and open connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Post request and no request body
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            // add authtoken
            http.setRequestProperty("Authorization", req.getAuthToken());

            // accepts json & connect
            http.addRequestProperty("Accept", "application/json");
            http.connect();

//            String reqData = gson.toJson(req);
//            OutputStream reqBody = http.getOutputStream();
//            writeString(reqData, reqBody);
//            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Person data success");
                // get result and return it
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getInputStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                PersonResult result = gson.fromJson(respData, PersonResult.class);
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                // get result and return it
                PersonResult result = gson.fromJson(respData, PersonResult.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in catch statement");
            System.out.println(e.getMessage());
            PersonResult result = new PersonResult(false, "Failed to login");
            return result;
        }
    }

    public EventResult getEvents(EventRequest req, String serverHost, String serverPort){
        Gson gson = new Gson();
        try{
            // create url and open connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Post request and no request body
            http.setRequestMethod("GET");
            http.setDoOutput(false);

            // add authtoken
            http.setRequestProperty("Authorization", req.getAuthToken());

            // accepts json & connect
            http.addRequestProperty("Accept", "application/json");
            http.connect();

//            String reqData = gson.toJson(req);
//            OutputStream reqBody = http.getOutputStream();
//            writeString(reqData, reqBody);
//            reqBody.close();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Person data success");
                // get result and return it
                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getInputStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                EventResult result = gson.fromJson(respData, EventResult.class);
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                // get result and return it
                EventResult result = gson.fromJson(respData, EventResult.class);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("in catch statement");
            System.out.println(e.getMessage());
            EventResult result = new EventResult(false, "Failed to login");
            return result;
        }
    }


    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
