package edu.byu.jwrig30.familymapclient.mainActivity;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.searchActivity.SearchActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;
import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private TextView markerDetails;
    private ImageView markerIcon;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);
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

        markerDetails = view.findViewById(R.id.EventDetails);
        markerIcon = view.findViewById(R.id.EventIcon);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //markerDetails.setText(marker.getSnippet());
                markerDetails.setText("Yay!");
                return false;
            }
        });

        // add event markers
        addEvents();
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
            StringBuilder details = new StringBuilder();
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions()
                .position(location)
                .title(event.getEventType()));
            marker.setTag(event);
            setSnippet(marker, event);
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

}
