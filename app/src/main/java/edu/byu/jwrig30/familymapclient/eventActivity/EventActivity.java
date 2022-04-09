package edu.byu.jwrig30.familymapclient.eventActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.mainActivity.MainActivity;
import edu.byu.jwrig30.familymapclient.mainActivity.MapFragment;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        String eventID = getIntent().getExtras().getString("Event");

        FragmentManager frag = getSupportFragmentManager();
        Fragment map = new MapFragment(eventID);

        FragmentTransaction fragmentTransaction = frag.beginTransaction();
        fragmentTransaction.add(R.id.eventMap, map);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
}