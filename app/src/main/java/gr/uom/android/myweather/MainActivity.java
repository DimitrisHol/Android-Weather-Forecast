package gr.uom.android.myweather;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;




import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //logt --> creates this
    private static final String TAG = "MainActivity";

    private String cityName;
    private ForecastAdapter forecastAdapter;
    private WeatherAdapter weatherAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.searchButton);
        b.setOnClickListener(this);
        AutoCompleteTextView locationSearch = findViewById(R.id.citynameText);
        locationSearch.setThreshold(1);
        locationSearch.setOnClickListener(this);


        ListView forecastlistView = findViewById(R.id.forecastListView);
        forecastlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                WeatherEntry weatherEntry = forecastAdapter.getDayForecast(position);

                Intent moreDetails = new Intent(MainActivity.this , DetailsActivity.class);
                moreDetails.putExtra("weatherObject" , weatherEntry);

                startActivity(moreDetails);


            }
        });


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.searchButton) {

            // Getting the name of the city from user input
            EditText nameText = findViewById(R.id.citynameText);
            String cityName = nameText.getText().toString();

            if (cityName != null) {

                getWeather("Thessaloniki,GR");
                getWeatherForecast("Thessaloniki,GR");

//                getWeather(cityName);
//                getWeatherForecast(cityName);


                //Graphics
                ListView listWeather = findViewById(R.id.weatherListView);
                listWeather.setVisibility(View.VISIBLE);
                TextView forecastTitle = findViewById(R.id.forecastTitle);
                forecastTitle.setVisibility(View.VISIBLE);
                ListView listForecast = findViewById(R.id.forecastListView);
                listForecast.setVisibility(View.VISIBLE);

//                // Hide keyboard after button is pressed. WORKS ONLY ON ANDROID 7.0
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                }



                }
        else if( view.getId() == R.id.citynameText) {


            AutoCompleteTextView locationSearch = findViewById(R.id.citynameText);
            locationSearch.setText("");


            // Call database to retrieve all the cities the user has put in! (RUSSA)
            String[] Cities = new String[]{
                    "Athens,GR", "Thessaloniki,GR",
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Cities);
            locationSearch.setAdapter(adapter);


        }


    }



    private void getWeather(String cityName) {

        ListView listView = findViewById(R.id.weatherListView);

        weatherAdapter = new WeatherAdapter(MainActivity.this , R.layout.weather_list_item, new ArrayList<WeatherEntry>());
        listView.setAdapter(weatherAdapter);

        Log.d(TAG , "Starting the Weather AsyncTask...");

        FetchWeatherTask weatherTask = new FetchWeatherTask(weatherAdapter , cityName);
        weatherTask.execute();



    }


    private void getWeatherForecast(String cityName){

        ListView listView = findViewById(R.id.forecastListView);

        forecastAdapter = new ForecastAdapter(MainActivity.this, R.layout.forecast_list_item, new ArrayList<WeatherEntry>());
        listView.setAdapter(forecastAdapter);

        // GET/POST COMMANDS
        Log.d(TAG , "Starting the Forecast AsyncTask...");

        FetchForecastTask weatherTask = new FetchForecastTask(forecastAdapter , cityName);
        weatherTask.execute();

    }
    /*
    //TODO
    USER LOGIN -- STILL TO FIND OUT ( something something περισσοτερες μελοντικες forecasts αν ειναι μέλος.
    LOCAL DATABASE -- αποθηκευει τις πόλες που εχει βάλει ο χρηστης (επιλογες insert και delete) Μια dropdown list για επιλογη και αναζητηση καιρου, αντι να γράφει ;)
    >1 ACTIVITIES -- CLICK ON FORECAST --> MORE DETAILED VIEW IN A NEW WINDOW
    ASYNC TASKS -- FETCHING DATA FROM API ------ DONE
    INTENTS -- CLICK ON FORECAST --> MORE DETAILED VIEW ( EXPLICIT)
    FRAGMENTS -- click on FORECAST --> DETAILED VIEW == FRAGMENT.
    */




    // Looking ahead


    // Swipe to refresh


}
