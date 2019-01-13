package gr.uom.android.myweather;

import android.app.Activity;
import android.content.Intent;
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

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";

    private String cityName;
    private ForecastAdapter forecastAdapter;
    private WeatherAdapter weatherAdapter;

    // Database
    private CityDao cityDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting a refference to the database
        // Database is used to store the last searches of the user.
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "cities-db");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

        // Object that contains a refference to the database.
        cityDao = daoSession.getCityDao();


        // Search Button Function
        Button b = findViewById(R.id.searchButton);
        b.setOnClickListener(this);


        // CityList Function
        AutoCompleteTextView locationSearch = findViewById(R.id.citynameText);
        locationSearch.setThreshold(1);
        locationSearch.setOnClickListener(this);


        // ForecastList onClick Function
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

        // User Pressed the search Button
        if (view.getId() == R.id.searchButton) {

            // Getting the name of the city from user input
            EditText nameText = findViewById(R.id.citynameText);
            String inputcityName = nameText.getText().toString();

            if (inputcityName != null) {

                // Start the 2 Tasks Weather and Forecast
//                getWeather("Thessaloniki,GR");
//                getWeatherForecast("Thessaloniki,GR");

                getWeather(inputcityName , cityDao);
                getWeatherForecast(inputcityName);



                //Graphics
                ListView listWeather = findViewById(R.id.weatherListView);
                listWeather.setVisibility(View.VISIBLE);
                TextView forecastTitle = findViewById(R.id.forecastTitle);
                forecastTitle.setVisibility(View.VISIBLE);
                ListView listForecast = findViewById(R.id.forecastListView);
                listForecast.setVisibility(View.VISIBLE);

                // Hide keyboard after button is pressed. WORKS ONLY ON ANDROID 7.0
                InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

                }



        }

        // User Pressed on the InputText Field
        else if( view.getId() == R.id.citynameText) {


            // Erase the text for the user
            AutoCompleteTextView locationSearch = findViewById(R.id.citynameText);
            locationSearch.setText("");

            //Retrieve citiNames from Database and set them to the dropdownList
            ArrayList<String> Cities = new ArrayList<>();
            List<City> dbCities = cityDao.queryBuilder().list();

            for (City city : dbCities) {
                Cities.add(city.getCityName() + ", " + city.getCountryCode());
            }

            // Set the array from database to be provided to the dropdown List.
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Cities);
            locationSearch.setAdapter(adapter);


        }


    }



    private void getWeather(String cityName , CityDao cityDao) {

        ListView listView = findViewById(R.id.weatherListView);

        weatherAdapter = new WeatherAdapter(MainActivity.this , R.layout.weather_list_item, new ArrayList<WeatherEntry>());
        listView.setAdapter(weatherAdapter);

        Log.d(TAG , "Starting the Weather AsyncTask...");

        FetchWeatherTask weatherTask = new FetchWeatherTask(weatherAdapter , cityName , cityDao);
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
}
