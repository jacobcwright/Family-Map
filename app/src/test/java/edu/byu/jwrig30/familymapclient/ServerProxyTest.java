package edu.byu.jwrig30.familymapclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class ServerProxyTest {
    @Test
    public void loginPass() {
        System.out.println("Running loginPass");
        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "parker");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        Assert.assertTrue(result.getSuccess());
    }

    @Test
    public void loginFail() {
        System.out.println("Running loginFail");

        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "WRONGPASSWORDLOLXD");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        Assert.assertFalse(result.getSuccess());
    }

    @Test
    public void registerPass() {
        System.out.println("Running registerPass");


    }

    @Test
    public void registerFail() {
        System.out.println("Running registerFail");


    }

    @Test
    public void retrievePeoplePass() {
        System.out.println("Running retrievePeoplePass");


    }

    @Test
    public void retrievePeopleFail() {
        System.out.println("Running retrievePeopleFail");


    }

    @Test
    public void retrieveEventsPass() {
        System.out.println("Running retrieveEventsPass");


    }

    @Test
    public void retrieveEventsFail() {
        System.out.println("Running retrieveEventsFail");


    }
}
