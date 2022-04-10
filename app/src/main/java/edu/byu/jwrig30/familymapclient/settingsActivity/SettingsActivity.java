package edu.byu.jwrig30.familymapclient.settingsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.mainActivity.MainActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat lifeLines;
    private SwitchCompat familyLines;
    private SwitchCompat spouseLines;
    private SwitchCompat paternal;
    private SwitchCompat maternal;
    private SwitchCompat maleEvents;
    private SwitchCompat femaleEvents;
    private LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lifeLines = findViewById(R.id.lifeLines);
        familyLines = findViewById(R.id.familyLines);
        spouseLines = findViewById(R.id.spouseLines);
        paternal = findViewById(R.id.paternal);
        maternal = findViewById(R.id.maternal);
        maleEvents = findViewById(R.id.maleEvents);
        femaleEvents = findViewById(R.id.femaleEvents);

        DataCache data = DataCache.getInstance();
        lifeLines.setChecked(data.isLifeLines());
        familyLines.setChecked(data.isFamilyLines());
        spouseLines.setChecked(data.isSpouseLines());
        paternal.setChecked(data.isPaternalFilter());
        maternal.setChecked(data.isMaternalFilter());
        maleEvents.setChecked(data.isMaleEvents());
        femaleEvents.setChecked(data.isFemaleEvents());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        lifeLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setLifeLines(lifeLines.isChecked());
            }
        });

        familyLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setFamilyLines(familyLines.isChecked());
            }
        });
        spouseLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setSpouseLines(spouseLines.isChecked());
            }
        });
        paternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setPaternalFilter(paternal.isChecked());
            }
        });
        maternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setMaternalFilter(maternal.isChecked());
            }
        });
        maleEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setMaleEvents(maleEvents.isChecked());
            }
        });
        femaleEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSettingsChanged(true);
                data.setFemaleEvents(femaleEvents.isChecked());
            }
        });
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