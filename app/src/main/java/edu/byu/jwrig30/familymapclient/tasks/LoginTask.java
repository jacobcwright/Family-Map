package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Handler;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {
    private final Handler messageHandler;
    LoginRequest request;
    LoginResult result;

    public LoginTask(Handler messageHandler, LoginRequest req) {
        this.messageHandler = messageHandler;
        this.request = req;
    }

    @Override
    public void run() {
        // Create Login Request and call login
         result = new ServerProxy().login(request, "Server Host", "Server Name");
    }
}
