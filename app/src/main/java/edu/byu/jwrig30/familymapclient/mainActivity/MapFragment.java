package edu.byu.jwrig30.familymapclient.mainActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.personActivity.PersonActivity;
import edu.byu.jwrig30.familymapclient.searchActivity.SearchActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private TextView markerDetails;
    private ImageView markerIcon;
    private LinearLayout mapTextView;
    private String clickedPersonID;
    private String clickedEventID;
    private boolean eventClicked;
    private Marker clickedMarker;
    private ArrayList<Polyline> lines;

    public MapFragment() {}

    public MapFragment(String eventID) {
        clickedEventID = eventID;
        eventClicked = (eventID != null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if(!eventClicked) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.options_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(getActivity(), "Search", Toast.LENGTH_LONG).show();
                Intent search = new Intent(getActivity(),SearchActivity.class);
                startActivity(search);
                return true;
            case R.id.settings:
                Toast.makeText(getActivity(), "Setting", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);

        clickedPersonID = null;
        clickedMarker = null;
        lines = new ArrayList<>();
        markerDetails = view.findViewById(R.id.detailsText);
        markerIcon = view.findViewById(R.id.detailsIcon);
        DataCache.getInstance().initEventColors();
        mapTextView = (LinearLayout) view.findViewById(R.id.mapTextView);
        mapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Person Activity", Toast.LENGTH_LONG).show();
                Intent person = new Intent(getActivity(), PersonActivity.class);
                person.putExtra("Person", clickedPersonID);
                startActivity(person);
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        addEvents();
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                markerClick(marker);
                return false;
            }
        });
        if(clickedMarker != null){
            map.moveCamera(CameraUpdateFactory.newLatLng(clickedMarker.getPosition()));
            markerClick(clickedMarker);
        }
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    private void addEvents(){
        DataCache data = DataCache.getInstance();
        ///Person user = data.getCurrentPerson();
        Map<String, Event> events = data.getEvents();

        for(Event event : events.values()){
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                .position(location)
                .title(event.getEventType()));
            marker.setTag(event);
            setSnippet(marker, event);
            setMarkerColor(marker, event);
            if(event.getEventID().equals(this.clickedEventID)){
                clickedMarker = marker;
            }
        }
    }

    private void setSnippet(Marker marker, Event event){
        StringBuilder sb = new StringBuilder();
        Person person = DataCache.getInstance().getPerson(event.getPersonID());
        sb.append(person.getFirstName()).append(" ").append(person.getLastName()).append("\n");
        sb.append(event.getEventType().toUpperCase()).append(": ");
        sb.append(event.getCity()).append(", ").append(event.getCountry()).append(" ");
        sb.append("(").append(event.getYear()).append(")");
        marker.setSnippet(sb.toString());
    }

    private void setIcon(Marker marker){
        DataCache data = DataCache.getInstance();
        Event e = (Event) marker.getTag();
        if(data.getPerson(e.getPersonID()).getGender().equals("f")){
            markerIcon.setImageResource(R.drawable.female);
            return;
        }
        else if(data.getPerson(e.getPersonID()).getGender().equals("m")){
            markerIcon.setImageResource(R.drawable.male);
            return;
        }
        else{
            System.out.println("****Gender is: " + data.getPerson(e.getPersonID()).getGender());
        }
    }

    private void setMarkerColor(Marker marker, Event event){
        float color = DataCache.getInstance().getEventColor(event.getEventType());
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(color));
    }

    private void markerClick(Marker marker){
        marker.showInfoWindow();
        clickedPersonID = ((Event) marker.getTag()).getPersonID();
        markerDetails.setText(marker.getSnippet());
        setIcon(marker);
        drawLines(marker);
    }

    private void drawLines(Marker marker){
         drawSpouseLines(marker);
        // drawFamilyLines(marker);
        // drawLifeLines(marker);
    }

    private void drawSpouseLines(Marker marker) {
        DataCache data = DataCache.getInstance();
        Event event = (Event) marker.getTag();
        Person currentPerson = data.getPerson(event.getPersonID());
        Person spouse = data.getPerson(currentPerson.getSpouseID());
        Event spouseBirth = data.getEventsForPerson(spouse.getPersonID()).get(0);

        Polyline line  = map.addPolyline(new PolylineOptions()
                .add(new LatLng(event.getLatitude(),event.getLongitude()), new LatLng(spouseBirth.getLatitude(),spouseBirth.getLongitude())));
        line.setColor(Color.WHITE);
        lines.add(line);

    }

}
