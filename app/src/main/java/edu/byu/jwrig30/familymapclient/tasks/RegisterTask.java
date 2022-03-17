package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Handler;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask implements Runnable {
    private final Handler messageHandler;
    RegisterRequest request;
    RegisterResult result;

    public RegisterTask(Handler messageHandler, RegisterRequest request) {
        this.messageHandler = messageHandler;
        this.request = request;
    }

    @Override
    public void run() {
        result = new ServerProxy().register(request, "Server Host", "Server Port");
    }
}
