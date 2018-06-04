package com.google.android.gms.samples.vision.ocrreader;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.samples.vision.ocrreader.ui.fragments.HomeFragment;
import com.google.android.gms.samples.vision.ocrreader.ui.fragments.SettingsFragment;

public class NavigationActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            android.support.v4.app.Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, selectedFragment).commit();
                    return true;
                case R.id.navigation_camera:
                    Intent myIntent = new Intent(NavigationActivity.this, OcrCaptureActivity.class);
                    myIntent.putExtra("key", "value");
                    NavigationActivity.this.startActivity(myIntent);
                    break;
                case R.id.navigation_settings:
                    selectedFragment = new SettingsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, selectedFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragment()).commit();
    }

}
