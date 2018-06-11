package com.google.android.gms.samples.vision.ocrreader.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.samples.vision.ocrreader.R;

/*
TODO: Show settings using  custom methods
http://codetheory.in/saving-user-settings-with-android-preferences/
TODO: make current_rate into user editable field and make that edit preferences
TODO: set start_currencyToend_currency as keys to be stored - update on change
TODO: handle potential bugs if we change current_rate without having both currencies
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }

    SharedPreferences sharedPrefs;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
//        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.user_settings);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        android.support.v7.preference.Preference preference = findPreference(key);
        if (preference instanceof android.support.v7.preference.ListPreference) {
            android.support.v7.preference.ListPreference listPreference = (android.support.v7.preference.ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
