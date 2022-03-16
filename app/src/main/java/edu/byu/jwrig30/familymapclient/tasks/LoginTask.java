package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Handler;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {
    private final Handler messageHandler;

    public LoginTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        // Create Login Request and call login
        LoginRequest request = null;
        LoginResult result = new ServerProxy().login(request, "Server Host", "Server Name");
    }
}
