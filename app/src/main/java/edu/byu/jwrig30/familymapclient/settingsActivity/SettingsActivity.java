package edu.byu.jwrig30.familymapclient.settingsActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Map;

import edu.byu.jwrig30.familymapclient.R;
import edu.byu.jwrig30.familymapclient.mainActivity.MainActivity;
import edu.byu.jwrig30.familymapclient.server.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SwitchPreferenceCompat lifeLines = findPreference("life");
            SwitchPreferenceCompat familyLines = findPreference("family");
            SwitchPreferenceCompat spouseLines = findPreference("spouse");
            SwitchPreferenceCompat paternalSide = findPreference("paternal");
            SwitchPreferenceCompat maternalSide = findPreference("maternal");
            SwitchPreferenceCompat maleEvents = findPreference("male");
            SwitchPreferenceCompat femaleEvents = findPreference("female");
            DataCache.getInstance().setPreferences(lifeLines.isChecked(), familyLines.isChecked(), spouseLines.isChecked(),
                    paternalSide.isChecked(),maternalSide.isChecked(), maleEvents.isChecked(), femaleEvents.isChecked());
        }

    }
}