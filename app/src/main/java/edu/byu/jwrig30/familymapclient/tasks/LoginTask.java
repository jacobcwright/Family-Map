package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Handler;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {
    private final Handler messageHandler;
    LoginRequest request;
    LoginResult result;
    String serverHost;
    String serverPort;

    public LoginTask(Handler messageHandler, LoginRequest req, String host, String port) {
        this.messageHandler = messageHandler;
        this.request = req;
        this.serverHost = host;
        this.serverPort = port;
    }

    @Override
    public void run() {
        // Create Login Request and call login
         result = new ServerProxy().login(request, serverHost, serverPort);
    }
}
