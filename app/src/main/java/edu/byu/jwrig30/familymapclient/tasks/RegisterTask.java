package edu.byu.jwrig30.familymapclient.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

import edu.byu.jwrig30.familymapclient.server.DataCache;
import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import model.Event;
import model.Person;
import request.EventRequest;
import request.PersonRequest;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;
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
        if(result.getSuccess()) {
            DataCache dataCache = DataCache.getInstance();

            // get people and store in dataCache
            PersonRequest getPeople = new PersonRequest(result.getAuthtoken());
            PersonResult peopleData = new ServerProxy().getPeople(getPeople, serverHost, serverPort);
            ArrayList<Person> people = peopleData.getPersons();
            dataCache.setPeople(people);

            // get events and store in dataCache
            EventRequest getEvents = new EventRequest(result.getAuthtoken());
            EventResult eventsData = new ServerProxy().getEvents(getEvents, serverHost, serverPort);
            ArrayList<Event> events = eventsData.getEvents();
            dataCache.setEvents(events);

            // store Person object for user
            Person currentPerson = dataCache.getPerson(result.getPersonID());
            dataCache.setCurrentPerson(currentPerson);

            // return message with first & last name of person logged in
            Bundle bundle = new Bundle();
            bundle.putBoolean("RegisterResult", result.getSuccess());
            bundle.putString("FirstName", currentPerson.getFirstName());
            bundle.putString("LastName", currentPerson.getLastName());
            Message message = Message.obtain();
            message.setData(bundle);
            messageHandler.sendMessage(message);
        }
        else {
            Message message = Message.obtain();
            messageHandler.sendMessage(message);
        }
    }
}
