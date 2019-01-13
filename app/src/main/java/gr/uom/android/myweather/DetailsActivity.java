package gr.uom.android.myweather;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredetails);

        // Get info from MainActivity
        Intent intent = getIntent();
        WeatherEntry weatherEntry = (WeatherEntry) intent.getSerializableExtra("weatherObject");


        // Fragment doesn't like waiting for information... ( 2nd time's the charm)
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        WeatherFragment fragment = new WeatherFragment();
        fragment.setWeatherEntry(weatherEntry);

        //Replace fragment with new fragment
        ft.replace(R.id.fragment, fragment);
        ft.commit();

    }

}
