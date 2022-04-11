package edu.byu.jwrig30.familymapclient;

import static org.junit.Assert.assertEquals;

import android.provider.ContactsContract;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Person;

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

    }

    @Test
    public void sortEventsFail(){

    }

    @Test
    public void searchPeoplePass(){

    }

    @Test
    public void searchPeopleFail(){

    }

    @Test
    public void searchEventPass(){

    }

    @Test
    public void searchEventFail(){

    }



}
