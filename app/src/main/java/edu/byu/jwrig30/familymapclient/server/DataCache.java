package edu.byu.jwrig30.familymapclient.server;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Comparator;
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
    private final Float[] COLORS = {30.0f, 210.0f, 330.0f, 240.0f, 180.0f, 120.0f, 300.0f, 0.0f, 270.0f, 60.0f};
    Map<String, Person> people;
    Map<String, Event> events;
    Authtoken authtoken;
    Person currentPerson;
    HashMap<String, Float> eventColors;
    private boolean lifeLines = true;
    private boolean familyLines = true;
    private boolean spouseLines = true;
    private boolean paternalFilter = true;
    private boolean maternalFilter = true;
    private boolean maleEvents = true;
    private boolean femaleEvents = true;
    private boolean settingsChanged = false;

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
        if(isMaternalFilter() && isPaternalFilter()){
            return events;
        }
        else if(!isMaternalFilter() && isPaternalFilter()){
            HashMap<Person, String> paternal = getPaternalFamily();
            HashMap<String, Event> results = new HashMap<>();
            for(Event e : events.values()){
                Person person = getPerson(e.getPersonID());
                if(paternal.containsKey(person)){
                    results.put(e.getEventID(), e);
                }
            }
            return results;
        }
        else if(isMaternalFilter() && !isPaternalFilter()){
            HashMap<Person, String> maternal = getMaternalFamily();
            HashMap<String, Event> results = new HashMap<>();
            for(Event e : events.values()){
                Person person = getPerson(e.getPersonID());
                if(maternal.containsKey(person)){
                    results.put(e.getEventID(), e);
                }
            }
            return results;
        }
        else {
            Person spouse = getPerson(currentPerson.getSpouseID());
            HashMap<String, Event> results = new HashMap<>();
            for(Event e : events.values()){
                if(e.getPersonID().equals(spouse.getPersonID()) ||
                        e.getPersonID().equals(currentPerson.getPersonID())){
                    results.put(e.getEventID(), e);
                }
            }
            return results;
        }
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
        assert(people != null);
        return people.get(personID);
    }

    public Event getEvent(String eventID){
        return events.get(eventID);
    }

    public void initEventColors(){
        eventColors = new HashMap();
        int colorIndex = 0;
        for(Event event : events.values()){
            if(!eventColors.containsKey(event.getEventType().toLowerCase())){
                eventColors.put(event.getEventType().toLowerCase(), COLORS[colorIndex]);
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
        if(isPaternalFilter() && isMaternalFilter()) {
            for (Event event : events.values()) {
                if (event.getPersonID().equals(personID)) {
                    personEvents.add(event);
                }
            }
        }
        else if(!isPaternalFilter() && isMaternalFilter()){
            HashMap<Person, String> maternal = getMaternalFamily();
            for (Event event : events.values()) {
                if (event.getPersonID().equals(personID)) {
                    Person person = getPerson(event.getPersonID());
                    if(maternal.containsKey(person)){
                        personEvents.add(event);
                    }
                }
            }
        }
        else if(isPaternalFilter() && !isMaternalFilter()) {
            HashMap<Person, String> paternal = getPaternalFamily();
            for (Event event : events.values()) {
                if (event.getPersonID().equals(personID)) {
                    Person person = getPerson(event.getPersonID());
                    if(paternal.containsKey(person)){
                        personEvents.add(event);
                    }
                }
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


    @SuppressLint("NewApi")
    public ArrayList<Event> getEventsForPersonDefault(String personID){
        ArrayList<Event> personEvents = new ArrayList<Event>();

        for (Event event : events.values()) {
            if (event.getPersonID().equals(personID)) {
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

    public HashMap<Person, String> getImmediateFamily(String personID){
        HashMap<Person, String> family = new HashMap<>();
        Person root = getPerson(personID);
        getSpouse(family, root);
        Person mother = getMother(root);
        if(mother != null && isMaternalFilter()){
            family.put(getMother(root),"Mother");
        }
        Person father = getFather(root);
        if(father != null && isPaternalFilter()){
            family.put(getFather(root),"Father");
        }
        getChildren(family, root);
        return family;
    }

    private void getChildren(HashMap<Person, String> family, Person root) {
        for(Person p : people.values()){
            if(root.getGender().equals("m")){
                if(p.getFatherID() == null) continue;
                if(p.getFatherID().equals(root.getPersonID())){
                    family.put(p, "Child");
                }
            }
            else if(root.getGender().equals("f")){
                if(p.getMotherID() == null) continue;
                if(p.getMotherID().equals(root.getPersonID())){
                    family.put(p, "Child");
                }
            }
        }
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
        return eventColors.get(eventType.toLowerCase());
    }

    public ArrayList<Person> getPeopleSearch(String searchString) {
        searchString = searchString.toLowerCase();
        ArrayList<Person> result = new ArrayList<>();
        for(Person p : people.values()){
            if(p.getFirstName().toLowerCase().contains(searchString) || p.getLastName().toLowerCase().contains(searchString)){
                result.add(p);
            }
        }
        return result;
    }

    public ArrayList<Event> getEventSearch(String searchString) {
        searchString = searchString.toLowerCase();
        ArrayList<Event> result = new ArrayList<>();
        for(Event e : events.values()){
            if(e.getCountry().toLowerCase().contains(searchString) || e.getCity().toLowerCase().contains(searchString)
            || String.valueOf(e.getYear()).toLowerCase().contains(searchString) || e.getEventType().toLowerCase().contains(searchString)){
                result.add(e);
            }
        }
        return result;
    }

    public boolean isLifeLines() {
        return lifeLines;
    }

    public void setLifeLines(boolean lifeLines) {
        this.lifeLines = lifeLines;
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isPaternalFilter() {
        return paternalFilter;
    }

    public void setPaternalFilter(boolean paternalFilter) {
        this.paternalFilter = paternalFilter;
    }

    public boolean isMaternalFilter() {
        return maternalFilter;
    }

    public void setMaternalFilter(boolean maternalFilter) {
        this.maternalFilter = maternalFilter;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public boolean isSettingsChanged() {
        return settingsChanged;
    }

    public void setSettingsChanged(boolean settingsChanged) {
        this.settingsChanged = settingsChanged;
    }

    public Map<String, Event> getMaleEvents() {
        HashMap<String, Event> maleEvents = new HashMap<>();
        if(isPaternalFilter() && isMaternalFilter()) {
            for (Event e : events.values()) {
                if (getPerson(e.getPersonID()).getGender().equals("m")) {
                    maleEvents.put(e.getEventID(), e);
                }
            }
            return maleEvents;
        }
        else if(!isPaternalFilter() && isMaternalFilter()){
            HashMap<Person, String> maternal = getMaternalFamily();
            for (Event e : events.values()) {
                Person person = getPerson(e.getPersonID());
                if (person.getGender().equals("m")) {
                    if(maternal.containsKey(person)){
                        maleEvents.put(e.getEventID(), e);
                    }
                }
            }
            return maleEvents;
        }
        else if(isPaternalFilter() && !isMaternalFilter()){
            HashMap<Person, String> paternal = getPaternalFamily();
            for (Event e : events.values()) {
                Person person = getPerson(e.getPersonID());
                if (person.getGender().equals("m")) {
                    if(paternal.containsKey(person)){
                        maleEvents.put(e.getEventID(), e);
                    }
                }
            }
            return maleEvents;
        }
        else {
            return maleEvents;
        }
    }

    public Map<String, Event> getFemaleEvents() {
        HashMap<String, Event> femaleEvents = new HashMap<>();
        if(isPaternalFilter() && isMaternalFilter()) {
            for (Event e : events.values()) {
                if (getPerson(e.getPersonID()).getGender().equals("f")) {
                    femaleEvents.put(e.getEventID(), e);
                }
            }
            return femaleEvents;
        }
        else if(!isPaternalFilter() && isMaternalFilter()){
            HashMap<Person, String> maternal = getMaternalFamily();
            for (Event e : events.values()) {
                Person person = getPerson(e.getPersonID());
                if (person.getGender().equals("f")) {
                    if(maternal.containsKey(person)){
                        femaleEvents.put(e.getEventID(), e);
                    }
                }
            }
            return femaleEvents;
        }
        else if(isPaternalFilter() && !isMaternalFilter()){
            HashMap<Person, String> paternal = getPaternalFamily();
            for (Event e : events.values()) {
                Person person = getPerson(e.getPersonID());
                if (person.getGender().equals("f")) {
                    if(paternal.containsKey(person)){
                        femaleEvents.put(e.getEventID(), e);
                    }
                }
            }
            return femaleEvents;
        }
        else {
            return femaleEvents;
        }
    }

    private HashMap<Person, String> getPaternalFamily(){
        HashMap<Person, String> paternal = new HashMap<>();
        if(currentPerson.getFatherID() != null) {
            Person father = getFather(currentPerson);
            paternal = getFamilyForPerson(father.getPersonID());
        }
        paternal.put(getCurrentPerson(), "Self");
        return paternal;
    }

    private HashMap<Person, String> getMaternalFamily(){
        HashMap<Person, String> maternal = new HashMap<>();
        if(currentPerson.getMotherID() != null) {
            Person mother = getMother(currentPerson);
            maternal = getFamilyForPerson(mother.getPersonID());
        }
        maternal.put(getCurrentPerson(), "Self");
        return maternal;
    }
}
