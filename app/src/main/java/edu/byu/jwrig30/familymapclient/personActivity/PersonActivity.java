package edu.byu.jwrig30.familymapclient.personActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.eventActivity.EventActivity;
import edu.byu.jwrig30.familymapclient.mainActivity.MainActivity;
import edu.byu.jwrig30.familymapclient.searchActivity.SearchActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {
    Person person;
    TextView firstName;
    TextView lastName;
    TextView gender;
    HashMap<Person, String> people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        ExpandableListView lifeEvents  = findViewById(R.id.lifeEvents);

        String personID = getIntent().getExtras().getString("Person");
        person = DataCache.getInstance().getPerson(personID);

        firstName = this.findViewById(R.id.FirstName);
        firstName.setText(person.getFirstName());
        lastName = this.findViewById(R.id.LastName);
        lastName.setText(person.getLastName());
        gender = this.findViewById(R.id.Gender);
        gender.setText(person.getGender().equals("m") ? "Male" : "Female");


        DataCache data = DataCache.getInstance();
        ArrayList<Event> events = new ArrayList<>();
        events = DataCache.getInstance().getEventsForPerson(personID);
        people = DataCache.getInstance().getImmediateFamily(personID);

        // both maternal & paternal
        if(data.isMaternalFilter() && data.isPaternalFilter()){
            // both female & male events
            if(data.isFemaleEvents() && data.isMaleEvents()){

            }
            // male events only
            else if(!data.isFemaleEvents() && data.isMaleEvents()){

            }
            // female events only
            else if(data.isFemaleEvents() && !data.isMaleEvents()){

            }
            // no events
            else {

            }
        }
        // Paternal side only
        else if(!data.isMaternalFilter() && data.isPaternalFilter()){
            // both female & male events
            if(data.isFemaleEvents() && data.isMaleEvents()){


            }
            // male events only
            else if(!data.isFemaleEvents() && data.isMaleEvents()){

            }
            // female events only
            else if(data.isFemaleEvents() && !data.isMaleEvents()){

            }
            // no events
            else {

            }
        }
        // Maternal side only
        else if(data.isMaternalFilter() && !data.isPaternalFilter()){
            // both female & male events
            if(data.isFemaleEvents() && data.isMaleEvents()){


            }
            // male events only
            else if(!data.isFemaleEvents() && data.isMaleEvents()){

            }
            // female events only
            else if(data.isFemaleEvents() && !data.isMaleEvents()){

            }
            // no events
            else {

            }
        }
        lifeEvents.setAdapter(new ExpandableListAdapter(events, new ArrayList<Person>(people.keySet())));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter{
        private static final int LIFE_EVENTS_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final List<Event> events;
        private final List<Person> family;

        ExpandableListAdapter(List<Event> events, List<Person> family) {
            this.events = events;
            this.family = family;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    return events.size();
                case FAMILY_GROUP_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            // Not used
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // Not used
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    titleView.setText("LIFE EVENTS");
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText("FAMILY");
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case LIFE_EVENTS_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.family_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventView, final int childPosition) {
            Event event = events.get(childPosition);
            TextView EventDetails = eventView.findViewById(R.id.EventDetails);
            EventDetails.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() +")");

            TextView eventPerson = eventView.findViewById(R.id.EventPerson);
            Person p = DataCache.getInstance().getPerson(event.getPersonID());
            eventPerson.setText(p.getFirstName() + " " + p.getLastName());

            eventView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(PersonActivity.this, "clicked on event", Toast.LENGTH_SHORT).show();
                    Intent event = new Intent(PersonActivity.this, EventActivity.class);
                    event.putExtra("Event", events.get(childPosition).getEventID());
                    startActivity(event);
                }
            });
        }

        private void initializePersonView(View personView, final int childPosition) {
            ImageView icon = personView.findViewById(R.id.FamilyIcon);
            if(family.get(childPosition).getGender().equals("f")){
                icon.setImageResource(R.drawable.female);
            }
            TextView nameView = personView.findViewById(R.id.PersonName);
            nameView.setText(family.get(childPosition).getFirstName() + " " + family.get(childPosition).getLastName());

            TextView relationshipView = personView.findViewById(R.id.Relationship);
            relationshipView.setText(people.get(family.get(childPosition)));

            personView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent person = new Intent(PersonActivity.this, PersonActivity.class);
                    person.putExtra("Person", family.get(childPosition).getPersonID());
                    startActivity(person);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}