package com.google.android.gms.samples.vision.ocrreader.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
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
public class SettingsFragment extends PreferenceFragmentCompat {
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_settings, container, false);
//    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
//        super.onCreate(bundle);
        setPreferencesFromResource(R.xml.user_settings, s);
    }


}
