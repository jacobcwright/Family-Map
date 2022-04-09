package edu.byu.jwrig30.familymapclient.server;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

    @SuppressLint("NewApi")
    public ArrayList<Event> getEventsForPerson(String personID){
        ArrayList<Event> personEvents = new ArrayList<Event>();
        for (Event event : events.values()) {
            if(event.getPersonID().equals(personID)){
                    personEvents.add(event);
            }
        }

        personEvents.sort(new Comparator<Event>() {
            @Override
            public int compare(Event t1, Event t2) {
                if(t1.getYear() <= t2.getYear()){
                    return -1;
                }
                else if(t1.getYear() > t2.getYear()){
                    return 1;
                }
                else return 0;
            }
        });
        return personEvents;
    }

    public HashMap<Person, String> getFamilyForPerson(String personID){
        // breadth-first search so we can use list for simplicity
        HashMap<Person, String> family = new HashMap<>();
        Person root = getPerson(personID);
        getParents(family, root, 1);
        getSpouse(family, root);
        return family;
    }

    private void getSpouse(HashMap<Person, String> family, Person currentPerson) {
        String spouseID = currentPerson.getSpouseID();
        if(spouseID != null){
            Person spouse = getPerson(spouseID);
            if(spouse != null) {
                family.put(spouse, "Spouse");
            }
        }
    }

    private void getParents(HashMap<Person, String> family, Person currentPerson, int generations) {
        Person father = getFather(currentPerson);
        if (father != null) {
            family.put(father, getRelationship(generations, "m"));
        }
        Person mother = getMother(currentPerson);
        if (mother != null) {
            family.put(mother, getRelationship(generations, "f"));
        }
        if (father!= null){
            getParents(family, father, generations+1);
        }
        if (mother != null){
            getParents(family, mother, generations+1);
        }
    }

    private Person getFather(Person currentPerson){
        Person father = null;
        String fatherID = currentPerson.getFatherID();
        if(fatherID != null){
            father = getPerson(fatherID);
        }
        return father;
    }

    private Person getMother(Person currentPerson){
        Person mother = null;
        String motherID = currentPerson.getMotherID();
        if(motherID != null){
            mother = getPerson(motherID);
        }
        return mother;
    }

    private String getRelationship(int generations, String gender){
        switch(gender) {
            case "m":
                if(generations >= 5) return "Great...Grandfather";
                switch (generations) {
                    case 1:
                        return "Father";
                    case 2:
                        return "Grandfather";
                    case 3:
                        return "Great-Grandfather";
                    case 4:
                        return "Great-Great-Grandfather";
                    default:
                        System.out.println("BAD ARGUMENT");
                        return "BAD ARGUMENT";
                }
            case "f":
                if(generations >= 5) return "Great...Grandmother";
                switch (generations) {
                    case 1:
                        return "Mother";
                    case 2:
                        return "Grandmother";
                    case 3:
                        return "Great-Grandmother";
                    case 4:
                        return "Great-Great-Grandmother";
                    default:
                        System.out.println("BAD ARGUMENT");
                        return "BAD ARGUMENT";
                }
            default:
                return "BAD ARGUMENT";

        }
    }

    public float getEventColor(String eventType){
        return eventColors.get(eventType);
    }
}
