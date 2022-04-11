package edu.byu.jwrig30.familymapclient;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

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

        ServerProxy server = new ServerProxy();
        // change username each time you run  test
        RegisterRequest req = new RegisterRequest("newUser1234", "parker", "email", "Test", "Person", "m");
        RegisterResult result = server.register(req, "127.0.0.1", "8080");

        Assert.assertTrue(result.getSuccess());

    }

    @Test
    public void registerFail() {
        System.out.println("Running registerFail");

        ServerProxy server = new ServerProxy();
        RegisterRequest req = new RegisterRequest("sheila", "parker", "email", "Test", "Person", "m");
        RegisterResult result = server.register(req, "127.0.0.1", "8080");

        Assert.assertFalse(result.getSuccess());

    }

    @Test
    public void retrievePeoplePass() {
        System.out.println("Running retrievePeoplePass");

        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "parker");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        PersonRequest personReq = new PersonRequest(result.getAuthtoken());
        PersonResult personResult = server.getPeople(personReq, "127.0.0.1", "8080");

        Assert.assertTrue(personResult.getSuccess());
        Assert.assertEquals(8, personResult.getPersons().size());
    }

    @Test
    public void retrievePeopleFail() {
        System.out.println("Running retrievePeopleFail");

        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "parker");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        PersonRequest personReq = new PersonRequest(result.getAuthtoken() + "abc");
        PersonResult personResult = server.getPeople(personReq, "127.0.0.1", "8080");

        Assert.assertFalse(personResult.getSuccess());
    }

    @Test
    public void retrieveEventsPass() {
        System.out.println("Running retrieveEventsPass");

        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "parker");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        EventRequest eventReq = new EventRequest(result.getAuthtoken());
        EventResult eventResult = server.getEvents(eventReq, "127.0.0.1", "8080");

        Assert.assertTrue(eventResult.getSuccess());
        Assert.assertEquals(16, eventResult.getEvents().size());
    }

    @Test
    public void retrieveEventsFail() {
        System.out.println("Running retrieveEventsFail");

        ServerProxy server = new ServerProxy();
        LoginRequest req = new LoginRequest("sheila", "parker");
        LoginResult result = server.login( req, "127.0.0.1", "8080");

        EventRequest eventReq = new EventRequest(result.getAuthtoken() + "abc");
        EventResult eventResult = server.getEvents(eventReq, "127.0.0.1", "8080");

        Assert.assertFalse(eventResult.getSuccess());
    }

}
