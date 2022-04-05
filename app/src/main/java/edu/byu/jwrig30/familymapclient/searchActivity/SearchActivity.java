package edu.byu.jwrig30.familymapclient.searchActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        FamilyMapAdapter adapter = new FamilyMapAdapter(DataCache.getInstance().getPeople(), DataCache.getInstance().getEvents());
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
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class FamilyMapHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView details;

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
            } else {
                name = itemView.findViewById(R.id.EventPerson);
                details = itemView.findViewById(R.id.EventDetails);
            }
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
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

                Toast.makeText(SearchActivity.this, String.format("Enjoy skiing %s!",
                        person.getFirstName() + " " + person.getLastName()), Toast.LENGTH_SHORT).show();
            } else {
                // This is were we could pass the hikingTrail to a hiking trail detail activity

                Toast.makeText(SearchActivity.this, String.format("Enjoy hiking %s. It's %s."), Toast.LENGTH_SHORT).show();
            }
        }
    }
}