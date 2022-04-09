package edu.byu.jwrig30.familymapclient.searchActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.eventActivity.EventActivity;
import edu.byu.jwrig30.familymapclient.mainActivity.MainActivity;
import edu.byu.jwrig30.familymapclient.personActivity.PersonActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    private Button searchButton;
    private EditText searchText;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        searchText = findViewById(R.id.search);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchString = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FamilyMapAdapter adapter = searchResults();
                recyclerView.setAdapter(adapter);
            }
        });


        FamilyMapAdapter adapter = new FamilyMapAdapter(new ArrayList<Person>(), new ArrayList<Event>());
        recyclerView.setAdapter(adapter);
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

    private class FamilyMapAdapter extends RecyclerView.Adapter<FamilyMapHolder> {
        private final ArrayList<Person> people;
        private final ArrayList<Event> events;

        FamilyMapAdapter(ArrayList<Person> people, ArrayList<Event> events) {
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
                holder.bind((Person) people.get(position));
            } else {
                holder.bind((Event) events.get(position - people.size()));
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
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("Person", person.getPersonID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("Event", event.getEventID());
                startActivity(intent);
            }
        }
    }

    private FamilyMapAdapter searchResults(){
        ArrayList<Person> people = DataCache.getInstance().getPeopleSearch(this.searchString);
        ArrayList<Event> events = DataCache.getInstance().getEventSearch(this.searchString);
        FamilyMapAdapter adapter = new FamilyMapAdapter(people, events);
        return adapter;
    }
}