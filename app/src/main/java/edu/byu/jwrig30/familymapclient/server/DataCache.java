package edu.byu.jwrig30.familymapclient.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import model.Authtoken;
import model.Event;
import model.Person;

/**
 * Data cache for Family Map Server
 * Singleton
 */
public class DataCache {
    private static DataCache instance = new DataCache();
    private final Float[] COLORS = {30.0f, 210.0f, 330.0f, 240.0f, 180.0f, 120.0f, 300.0f, 0.0f, 270.0f, 60.0f};
    Map<String, Person> people;
    Map<String, Event> events;
    Authtoken authtoken;
    Person currentPerson;
    HashMap<String, Float> eventColors;

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

    public void initEventColors(){
        eventColors = new HashMap();
        int colorIndex = 0;
        for(Event event : events.values()){
            if(!eventColors.containsKey(event.getEventType())){
                eventColors.put(event.getEventType(), COLORS[colorIndex]);
                if(colorIndex == COLORS.length - 1){
                    colorIndex = 0;
                }
                else {
                    colorIndex++;
                }
            }
        }
    }

    public ArrayList<Event> getEventsForPerson(String personID){
        ArrayList<Event> personEvents = new ArrayList<Event>();
        for (Event event : events.values()) {
            if(event.getPersonID().equals(personID)){
                if(event.getEventType().toLowerCase().equals("birth")){
                    personEvents.add(0, event);
                }
                else {
                    personEvents.add(event);
                }
            }
        }

        return personEvents;
    }

    public TreeSet<Person> getFamilyForPerson(String personID){
        TreeSet<Person> family = new TreeSet<>();


        return family;
    }

    public float getEventColor(String eventType){
        return eventColors.get(eventType);
    }
}
