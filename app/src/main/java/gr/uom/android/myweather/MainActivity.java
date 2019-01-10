package gr.uom.android.myweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //logt --> creates this
    private static final String TAG = "MainActivity";

    private String cityName;
    private ForecastAdapter forecastAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.searchButton);
        b.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.searchButton){

            // Getting the name of the city from user input
            EditText nameText = findViewById(R.id.citynameText);
            String cityName = nameText.getText().toString();

            if (cityName != null){

//                boolean flag = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
//                if (flag) {
                    getWeather("Thessaloniki");
//                }

//                // Hide keyboard after button is pressed. WORKS ONLY ON ANDROID 7.0
//                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

            }



        }
    }



    private void getWeather(String cityName){

        ListView listView = findViewById(R.id.forecastListView);

        forecastAdapter = new ForecastAdapter(MainActivity.this, R.layout.forecast_list_item, new ArrayList<DayForecast>());
        listView.setAdapter(forecastAdapter);

        // GET/POST COMMANDS
        Log.d(TAG , "onCreate : Starting an async Task,.... ");

        FetchWeatherTask weatherTask = new FetchWeatherTask(forecastAdapter , cityName);
        weatherTask.execute();

    }

    //TODO :  USER LOGIN
    //TODO :  BASH DEDOMENWN = save the DayForecast objects
    //TODO :  Fragments : idk ισως τον current καιρο

    /*
    //TODO
    USER LOGIN -- STILL TO FIND OUT
    LOCAL DATABASE -- STILL TO FIND OUT
    >1 ACTIVITIES -- CLICK ON FORECAST --> MORE DETAILED VIEW IN A NEW WINDOW
    DONE ASYNC TASKS -- FETCHING DATA FROM API
    INTENTS -- CLICK ON FORECAST --> MORE DETAILED VIEW ( EXPLICIT)
    FRAGMENTS -- CURRENT WEATHER IS A FRAGMENT
    */
    /*


    TODO
    MAKE API KEYS ENCRYPTED
    INTENT SHOWS YOU MORE DETAILED STATISTICS FROM 3-HOUR PERIOD
    ADAPTER FOR CURRENT WEATHER - TOGETHER WITH FRAGMENT
    ICON MAPPED FROM WEATHER?

    TODO
     */

    // GIA EPIKOINWNIA ACTIVITY / FRAGMENT DES
    // ONPOSTEXECUTE APO TO LESSON09-WEATHER


}
