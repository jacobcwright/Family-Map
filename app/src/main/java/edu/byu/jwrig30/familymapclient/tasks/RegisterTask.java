package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
        Bundle bundle = new Bundle();
        bundle.putBoolean("RegisterResult", result.getSuccess());
        Message message = Message.obtain();
        message.setData(bundle);
        messageHandler.sendMessage(message);
    }
}
