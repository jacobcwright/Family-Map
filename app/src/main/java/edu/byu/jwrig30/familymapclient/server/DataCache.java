package edu.byu.jwrig30.familymapclient.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Authtoken;
import model.Event;
import model.Person;

/**
 * Data cache for Family Map Server
 * Singleton
 */
public class DataCache {
    private static DataCache instance = new DataCache();

    Map<String, Person> people;
    Map<String, Event> events;
    Authtoken authtoken;
    Person currentPerson;

    public static DataCache getInstance(){
        return instance;
    }

    private DataCache(){}

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(ArrayList<Person> people) {
        HashMap<String, Person> peopleMap = new HashMap<>();
        for(Person person : people){
            peopleMap.put(person.getPersonID(), person);
        }
        this.people = peopleMap;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        HashMap<String, Event> eventMap = new HashMap<>();
        for(Event event : events){
            eventMap.put(event.getEventID(), event);
        }
        this.events = eventMap;
    }

    public Authtoken getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(Authtoken authtoken) {
        this.authtoken = authtoken;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Person getPerson(String personID){
        return people.get(personID);
    }

    public Event getEvent(String eventID){
        return events.get(eventID);
    }
}
