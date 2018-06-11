package com.google.android.gms.samples.vision.ocrreader.ui.preferences;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.util.AttributeSet;

public class SubstitutablePreference extends EditTextPreference {
    public SubstitutablePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SubstitutablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubstitutablePreference(Context context) {
        super(context);
    }

    @Override
    public CharSequence getSummary() {
        String summary = super.getSummary().toString();
//        return String.format(summary, getSharedPreferences().getString(getKey(), ""));
        return String.format(summary, getText());
    }
}
