package edu.byu.jwrig30.familymapclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import android.provider.ContactsContract;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import edu.byu.jwrig30.familymapclient.server.DataCache;
import edu.byu.jwrig30.familymapclient.server.ServerProxy;
import model.Event;
import model.Person;
import request.LoginRequest;
import result.LoginResult;

public class ModelTest {
    @Test
    public void calculateRelationshipPass(){
        ArrayList<Person> family = new ArrayList<>();
        DataCache data = DataCache.getInstance();
        Person self = new Person("Jacob_Wright","sheila","Jacob", "Wright", "m");
        Person father = new Person("Jacob_Dad","sheila","Father", "Wright", "m");
        Person mother = new Person("Jacob_Mom","sheila","Momma", "Wright", "f");
        self.setFatherID("Jacob_Dad");
        self.setMotherID("Jacob_Mom");
        Person father_father = new Person("Jacob_Grandpa","Grandpa","Jacob", "Wright", "m");
        Person father_mother = new Person("Jacob_Grandma","Grandma","Jacob", "Wright", "f");
        father.setFatherID("Jacob_Grandpa");
        father.setMotherID("Jacob_Grandma");

        family.add(self);
        family.add(father);
        family.add(mother);
        family.add(father_father);
        family.add(father_mother);

        data.setPeople(family);
        HashMap<Person, String> results = data.getFamilyForPerson(self.getPersonID());

        Assert.assertEquals("Father", results.get(father));
        Assert.assertEquals("Grandfather", results.get(father_father));
        Assert.assertEquals("Mother", results.get(mother));
        Assert.assertEquals("Grandmother", results.get(father_mother));
    }

    @Test
    public void calculateRelationshipFail(){
        ArrayList<Person> family = new ArrayList<>();
        DataCache data = DataCache.getInstance();
        Person self = new Person("Jacob_Wright","sheila","Jacob", "Wright", "m");
        Person father = new Person("Jacob_Dad","sheila","Father", "Wright", "m");
        Person mother = new Person("Jacob_Mom","sheila","Momma", "Wright", "f");
        self.setFatherID("Jacob_Dad");
        self.setMotherID("Jacob_Mom");
        Person father_father = new Person("Jacob_Grandpa","Grandpa","Jacob", "Wright", "m");
        Person father_mother = new Person("Jacob_Grandma","Grandma","Jacob", "Wright", "f");
        father.setFatherID("Jacob_Grandpa");
        father.setMotherID("Jacob_Grandma");

        Person notRelated = new Person("random","sheila","not", "related", "m");


        family.add(self);
        family.add(father);
        family.add(mother);
        family.add(father_father);
        family.add(father_mother);
        family.add(notRelated);

        data.setPeople(family);
        HashMap<Person, String> results = data.getFamilyForPerson(self.getPersonID());

        Assert.assertEquals("Father", results.get(father));
        Assert.assertEquals("Grandfather", results.get(father_father));
        Assert.assertNull(results.get(notRelated));
    }

    @Test
    public void filterEventsPass(){

    }

    @Test
    public void filterEventsFail(){

    }

    @Test
    public void sortEventsPass(){
        DataCache data = DataCache.getInstance();
        ArrayList<Event> events = new ArrayList<Event>();

        Person self = new Person("Jacob_Wright","jacob","Jacob", "Wright", "m");
        // String eventID, String username, String personID, float latitude, float longitude, String country, String city, String eventType, int year
        Event event1 = new Event("event1", "jacob", "Jacob_Wright",50.0f, 50.0f, "Chicken", "Nugget", "Event Number 1", 1900);
        Event event2 = new Event("event2", "jacob", "Jacob_Wright",100.0f, 50.0f, "Chicken", "Nugget", "Event Number 2", 1950);
        Event event3 = new Event("event3", "jacob", "Jacob_Wright",50.0f, 100.0f, "Chicken", "Nugget", "Event Number 3", 2000);

        events.add(event3);
        events.add(event1);
        events.add(event2);

        ArrayList<Person> people = new ArrayList<>();
        people.add(self);
        data.setPeople(people);
        data.setEvents(events);
        ArrayList<Event> eventsResults = data.getEventsForPerson("Jacob_Wright");

        assertEquals(event1, eventsResults.get(0));
        assertEquals(event2, eventsResults.get(1));
        assertEquals(event3, eventsResults.get(2));
    }

    @Test
    public void sortEventsFail(){
        DataCache data = DataCache.getInstance();
        ArrayList<Event> events = new ArrayList<Event>();

        Person self = new Person("Jacob_Wright","jacob","Jacob", "Wright", "m");
        // String eventID, String username, String personID, float latitude, float longitude, String country, String city, String eventType, int year
        Event event1 = new Event("event1", "jacob", "Jacob_Wright",50.0f, 50.0f, "Chicken", "Nugget", "Event Number 1", 1900);
        Event event2 = new Event("event2", "jacob", "Jacob_Wright",100.0f, 50.0f, "Chicken", "Nugget", "Event Number 2", 1950);
        Event event3 = new Event("event3", "jacob", "Jacob_Wright",50.0f, 100.0f, "Chicken", "Nugget", "Event Number 3", 2000);

        events.add(event3);
        events.add(event1);
        events.add(event2);

        ArrayList<Person> people = new ArrayList<>();
        people.add(self);
        data.setPeople(people);
        data.setEvents(events);
        ArrayList<Event> eventsResults = data.getEventsForPerson("Jacob_Wright");

        assertNotEquals(event3, eventsResults.get(0));
        assertNotEquals(event1, eventsResults.get(1));
        assertNotEquals(event2, eventsResults.get(2));
    }

    @Test
    public void searchPeoplePass(){
        ArrayList<Person> family = new ArrayList<>();
        DataCache data = DataCache.getInstance();
        Person self = new Person("Jacob_Wright","sheila","Jacob", "Wright", "m");
        Person spouse = new Person("Jacob_Spouse","sheila","Spouse", "Wright", "f");
        Person child = new Person("Jacob_Child","sheila","Child", "Wright", "m");
        Person father = new Person("Jacob_Dad","sheila","Father", "Wright", "m");
        Person mother = new Person("Jacob_Mom","sheila","Momma", "Wright", "f");

        self.setFatherID("Jacob_Dad");
        self.setMotherID("Jacob_Mom");
        self.setSpouseID("Jacob_Spouse");
        spouse.setSpouseID("Jacob_Wright");
        child.setFatherID("Jacob_Wright");

        Person father_father = new Person("Jacob_Grandpa","Grandpa","Jacob", "Wright", "m");
        Person father_mother = new Person("Jacob_Grandma","Grandma","Jacob", "Wright", "f");
        father.setFatherID("Jacob_Grandpa");
        father.setMotherID("Jacob_Grandma");


        family.add(self);
        family.add(father);
        family.add(mother);
        family.add(father_father);
        family.add(father_mother);
        family.add(spouse);
        family.add(child);

        data.setPeople(family);
        HashMap<Person, String> results = data.getImmediateFamily("Jacob_Wright");

        assertEquals("Spouse", results.get(spouse));
        assertEquals("Child", results.get(child));
        assertEquals("Father", results.get(father));
        assertEquals("Mother", results.get(mother));
    }

    @Test
    public void searchPeopleFail(){
        ArrayList<Person> family = new ArrayList<>();
        DataCache data = DataCache.getInstance();
        Person self = new Person("Jacob_Wright","sheila","Jacob", "Wright", "m");
        Person spouse = new Person("Jacob_Spouse","sheila","Spouse", "Wright", "f");
        Person child = new Person("Jacob_Child","sheila","Child", "Wright", "m");
        Person father = new Person("Jacob_Dad","sheila","Father", "Wright", "m");
        Person mother = new Person("Jacob_Mom","sheila","Momma", "Wright", "f");

        self.setFatherID("Jacob_Dad");
        self.setMotherID("Jacob_Mom");
        self.setSpouseID("Jacob_Spouse");
        spouse.setSpouseID("Jacob_Wright");
        child.setFatherID("Jacob_Wright");

        Person father_father = new Person("Jacob_Grandpa","Grandpa","Jacob", "Wright", "m");
        Person father_mother = new Person("Jacob_Grandma","Grandma","Jacob", "Wright", "f");
        father.setFatherID("Jacob_Grandpa");
        father.setMotherID("Jacob_Grandma");

        Person notRelated = new Person("random","sheila","not", "related", "m");

        family.add(self);
        family.add(father);
        family.add(mother);
        family.add(father_father);
        family.add(father_mother);
        family.add(spouse);
        family.add(child);
        family.add(notRelated);

        data.setPeople(family);
        HashMap<Person, String> results = data.getImmediateFamily("Jacob_Wright");

        assertFalse(results.containsKey(father_father));
        assertFalse(results.containsKey(father_mother));
        assertFalse(results.containsKey(notRelated));
    }

    @Test
    public void searchEventPass(){

    }

    @Test
    public void searchEventFail(){

    }



}
