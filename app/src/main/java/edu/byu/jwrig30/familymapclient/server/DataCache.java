package edu.byu.jwrig30.familymapclient.server;

import java.util.Map;

import model.Authtoken;
import model.Event;
import model.Person;

/**
 * Data cache for Family Map Server
 */
class DataCache {
    private static DataCache instance = new DataCache();

    Map<String, Person> people;
    Map<String, Event> events;
    Authtoken authtoken;

    public static DataCache getInstance(){
        return instance;
    }

    private DataCache(){}


}
