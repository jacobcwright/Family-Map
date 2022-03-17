package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Handler;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask implements Runnable {
    private final Handler messageHandler;
    RegisterRequest request;
    RegisterResult result;
    String serverHost;
    String serverPort;

    public RegisterTask(Handler messageHandler, RegisterRequest request, String host, String port) {
        this.messageHandler = messageHandler;
        this.request = request;
        this.serverHost = host;
        this.serverPort = port;
    }

    @Override
    public void run() {
        result = new ServerProxy().register(request, serverHost, serverPort);
        // get result and send message using sendMessage()

    }
}
