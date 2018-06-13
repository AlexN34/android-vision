package com.google.android.gms.samples.vision.ocrreader.ui.fragments;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.samples.vision.ocrreader.R;
import com.google.android.gms.samples.vision.ocrreader.ui.preferences.SubstitutablePreference;

import java.util.Arrays;
import java.util.HashMap;

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
    String[] defaultCurrencies = {"AUD", "USD", "GBP", "EUR"};
    String[] defaultValues = {"1", "0.76", "0.56", "0.64"};
    private static final String DIALOG_FRAGMENT_TAG =
    "android.support.v7.preference.PreferenceFragment.DIALOG";

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
//        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.user_settings);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        HashMap<String, Float> currencyMap = new HashMap<>();
        PreferenceScreen screen = this.getPreferenceScreen();
        PreferenceCategory category = new PreferenceCategory(screen.getContext());
        category.setTitle("Currency values");
        screen.addPreference(category);
        for (int i = 0; i < defaultCurrencies.length; i++) {
            SubstitutablePreference curPref = new SubstitutablePreference(screen.getContext());
            curPref.setTitle(defaultCurrencies[i]);
            curPref.setKey(defaultCurrencies[i]);
               String prefValue = sharedPrefs.getString(defaultCurrencies[i], "");
            if (!prefValue.equals("")) {
                curPref.setText(prefValue);
            } else {
                curPref.setText(defaultValues[i]);
                sharedPrefs.edit().putString(defaultCurrencies[i], defaultValues[i]).apply();
            }
            curPref.setSummary("%s");
            screen.addPreference(curPref);
        }
        updateCurrentRate(sharedPrefs);
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
        if (key.equals("start_currency") || key.equals("end_currency") ) {
            android.support.v7.preference.Preference currentRatePref = findPreference("conversion_rate");

        }

        if (key.equals("start_currency") || key.equals("end_currency") || Arrays.asList(defaultCurrencies).contains(key)) {
            updateCurrentRate(sharedPreferences);
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

    public void updateCurrentRate(SharedPreferences sharedPreferences) {
        String startCurrency = sharedPreferences.getString("start_currency", "");
        String endCurrency = sharedPreferences.getString("end_currency", "");
        if (!endCurrency.equals("") && !startCurrency.equals("")) {
            if (!findPreference("conversion_rate").getSummary().equals("%s")) {
                findPreference("conversion_rate").setSummary("%s");
            }
            Float startRate = Float.parseFloat(sharedPreferences.getString(startCurrency, "-1"));
            Float endRate = Float.parseFloat(sharedPreferences.getString(endCurrency, "-1"));
            Float rate = Float.parseFloat("0");
            if (startRate != Float.parseFloat("-1") && endRate != Float.parseFloat("-1")) {
                rate = endRate / startRate;
            }
            sharedPreferences.edit().putString("conversion_rate", Float.toString(rate)).apply();
        }
    }


    @Override
    public void onDisplayPreferenceDialog(android.support.v7.preference.Preference preference) {
        // check if dialog is already showing
        if (getFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }

        EditTextPreferenceDialog f = null;
        if (preference instanceof SubstitutablePreference) {
            f = EditTextPreferenceDialog.newInstance(preference.getKey());
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
        if (f != null) {
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    public static class EditTextPreferenceDialog extends EditTextPreferenceDialogFragmentCompat {

        public static EditTextPreferenceDialog newInstance(String key) {
            final EditTextPreferenceDialog
                fragment = new EditTextPreferenceDialog();
            final Bundle b = new Bundle(1);
            b.putString(ARG_KEY, key);
            fragment.setArguments(b);
            return fragment;
        }

        @Override
        protected void onBindDialogView(View view) {
            super.onBindDialogView(view);
            ((EditText)view.findViewById(android.R.id.edit)).setInputType(InputType.TYPE_CLASS_NUMBER);
        }

    }
}
