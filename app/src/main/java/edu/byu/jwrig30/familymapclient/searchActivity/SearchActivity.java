package edu.byu.jwrig30.familymapclient.searchActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        Map<String, Person> people = DataCache.getInstance().getPeople();
        Map<String, Event> events =  DataCache.getInstance().getEvents();
        FamilyMapAdapter adapter = new FamilyMapAdapter(people, events);
        recyclerView.setAdapter(adapter);
    }

    private class FamilyMapAdapter extends RecyclerView.Adapter<FamilyMapHolder> {
        private final Map<String, Person> people;
        private final Map<String, Event> events;

        FamilyMapAdapter(Map<String, Person> people, Map<String, Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public FamilyMapHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new FamilyMapHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull FamilyMapHolder holder, int position) {
            if(position < people.values().size()) {
                holder.bind((Person) people.values().toArray()[position]);
            } else {
                holder.bind((Event) events.values().toArray()[position - people.size()]);
            }
        }

        @Override
        public int getItemCount() {
            return people.values().size() + events.values().size();
        }
    }

    private class FamilyMapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView details;
        private final ImageView icon;

        private final int viewType;
        private Person person;
        private Event event;

        FamilyMapHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.PersonName);
                details = null;
                icon = itemView.findViewById(R.id.PersonIcon);
            } else {
                name = itemView.findViewById(R.id.EventPerson);
                details = itemView.findViewById(R.id.EventDetails);
                icon = null;
            }
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
            if(person.getGender().equals("m")){
                icon.setImageResource(R.drawable.male);
            }
            else{
                icon.setImageResource(R.drawable.female);
            }
        }

        private void bind(Event event) {
            this.event = event;
            details.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() +")");
            Person p = DataCache.getInstance().getPerson(event.getPersonID());
            name.setText(p.getFirstName() + " " + p.getLastName());
        }

        @Override
        public void onClick(View view) {
            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                // This is were we could pass the skiResort to a ski resort detail activity

                Toast.makeText(SearchActivity.this, "Person", Toast.LENGTH_SHORT).show();
            } else {
                // This is were we could pass the hikingTrail to a hiking trail detail activity

                Toast.makeText(SearchActivity.this, "Event", Toast.LENGTH_SHORT).show();
            }
        }
    }
}