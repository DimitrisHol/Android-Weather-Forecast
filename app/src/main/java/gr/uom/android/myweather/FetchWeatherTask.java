package gr.uom.android.myweather;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FetchWeatherTask extends AsyncTask<String , Object  , List<WeatherEntry> > {


    private static final String LOG_TAG = "FetchWeatherTask";

    private String cityName; // This needs to come from user input
    private WeatherAdapter weatherAdapter;


    public FetchWeatherTask(WeatherAdapter weatherAdapter, String cityName) {

        this.weatherAdapter = weatherAdapter;
        this.cityName = cityName;

    }


    @Override
    protected List<WeatherEntry> doInBackground(String... params) {

        //To make the result more accurate just put the city name and country divided by comma.

        String weatherJsonStr = null;  //Final String
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //End result : "http://api.openweathermap.org/data/2.5/weather?q=THESSALONIKI,GR&type=like&units=metric&APPID=YOUR_API_KEY_HERE"
        //Building the URL for the OpenWeather API using URI:
        try {

            final String baseUrl = "http://api.openweathermap.org/data/2.5/weather?";
            final String queryParameter = "q";
            final String searchParameter = "type";
            final String unitsParameter = "units";
            final String apiKeyParameter = "APPID";

            Uri builtURi = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(queryParameter , cityName)
                    .appendQueryParameter(searchParameter ,"like" )
                    .appendQueryParameter(unitsParameter , "metric")
                    .appendQueryParameter(apiKeyParameter , BuildConfig.ApiKey)

                    .build();
            URL url = new URL(builtURi.toString());

            Log.v(LOG_TAG, "Built URI : " + builtURi.toString());


            // Sending the request to the server

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the response

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null){
                //Your princess is in another castle , nothing came through
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Debugging
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0){
                return null;
            }

            //Final string of data
            weatherJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast JSON String: " + weatherJsonStr);

        }
        catch (IOException e){
            Log.e(LOG_TAG, "Error in getting the data from API, check return code", e);
            // If you can't get the data, no point in parsing.
            return null;
        }

        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(LOG_TAG, "Error closing stream", e);
            }
        }

        //Parse the JSON format to string/class objects , and return the final list of items!
        try {
            return getWeatherDataFromJson(weatherJsonStr);
        }catch (JSONException e){
            Log.e(LOG_TAG, "Error, could not parse the data from JSON" + e.getMessage());
            return null;
        }
    }



    private List<WeatherEntry> getWeatherDataFromJson(String forecastJsonStr) throws JSONException{


        // OPW = OpenWeatherMap , this is a mapping of my variables , to the variable names from the JSON file.

        final String OWP_WEATHER = "weather";   // 1 item list , containts weahter descriptions
        final String OWP_description = "main";
        final String OWP_TEMPERATURE = "main";  // contains temperature and humidity readings
        final String OWP_THERMOKRASIA = "temp";
        final String OWP_HUMIDITY = "humidity";

        final String OWP_WIND = "wind";         // contains wind speed
        final String OWP_WINDSPEED = "speed";


        JSONObject weatherJSON = new JSONObject(forecastJsonStr);

        if ( weatherJSON.getString("cod").equals("200")){


            List<WeatherEntry> currentWeather  = new ArrayList<>();
            WeatherEntry dayWeather = new WeatherEntry();

            JSONObject weatherDecs = weatherJSON.getJSONArray(OWP_WEATHER).getJSONObject(0);
            String desc = weatherDecs.getString(OWP_description);

            dayWeather.setWeatherDescription(desc);   //0

            JSONObject readingsObj = weatherJSON.getJSONObject(OWP_TEMPERATURE);
            double temperature = readingsObj.getDouble(OWP_THERMOKRASIA);
            String humidity = readingsObj.getString(OWP_HUMIDITY);

            dayWeather.setCurrentTemperature(formatTemperatureReadings(temperature)); //1
            dayWeather.setHumidity(humidity); //2

            JSONObject windReadings = weatherJSON.getJSONObject(OWP_WIND);
            String windSpeed =windReadings.getString(OWP_WINDSPEED);

            dayWeather.setWindSpeed(windSpeed); //3

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String time = formatter.format(date);

            dayWeather.setTime(time);


            Log.v(LOG_TAG , "Object is like this : " + dayWeather.toString());

            currentWeather.add(dayWeather);

            return currentWeather;
        }
        else
            Log.e(LOG_TAG , "API REQUEST RETURNED CODE : " + weatherJSON.getString("cod"));
            return new ArrayList<>();
    }

    private String formatTemperatureReadings(double temperature){
        long result = Math.round(temperature);
        return String.valueOf(result);

    }

    @Override
    protected void onPostExecute(List<WeatherEntry> dayWeather) {
        super.onPostExecute(dayWeather);
        Log.v(LOG_TAG , "onPostExecute...");
        weatherAdapter.setCurrentWeather(dayWeather);
    }


}
