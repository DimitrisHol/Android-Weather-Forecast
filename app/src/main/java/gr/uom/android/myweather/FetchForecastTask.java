package gr.uom.android.myweather;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchForecastTask extends AsyncTask<String , Object  , List<WeatherEntry> > {


    private static final String LOG_TAG = "FetchForecastTask";

    private String cityName; // This needs to come from user input
    private ForecastAdapter forecastAdapter;
    private String numOfForecasts;




    public FetchForecastTask(ForecastAdapter forecastAdapter , String cityName) {

        this.forecastAdapter = forecastAdapter;
        this.cityName = cityName;

    }


    @Override
    protected List<WeatherEntry> doInBackground(String... params) {

        //To make the result more accurate just put the city name and country divided by comma.

        String forecastJsonStr = null;  //Final String
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
//        String weatherFormat = "json";    Default is json


        numOfForecasts = "10";
        // End result : "http://api.openweathermap.org/data/2.5/forecast?q=THESSALONIKI&type=like&units=metric&APPID=YOUR_API_KEY_HERE?"

        //Building the URL for the OpenWeather API using URI:
        try {

            final String baseUrl = "http://api.openweathermap.org/data/2.5/forecast?";
            final String queryParameter = "q";
            final String searchParameter = "type";
            final String unitsParameter = "units";
            final String daysParameter = "cnt";
            final String apiKeyParameter = "APPID";

            Uri builtURi = Uri.parse(baseUrl).buildUpon()
                    .appendQueryParameter(queryParameter , cityName)
                    .appendQueryParameter(searchParameter ,"like" )
                    .appendQueryParameter(unitsParameter , "metric")
                    .appendQueryParameter(daysParameter , numOfForecasts)
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
            forecastJsonStr = buffer.toString();

            Log.v(LOG_TAG, "Forecast JSON String: " + forecastJsonStr);

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
            return getWeatherDataFromJson(forecastJsonStr);
        }catch (JSONException e){
            Log.e(LOG_TAG, "Error, could not parse the data from JSON" + e.getMessage());
            return null;
        }
    }



    private List<WeatherEntry> getWeatherDataFromJson(String forecastJsonStr) throws JSONException{

        // This is where we store the final results ( this will get returned).
        List<WeatherEntry> dayForecasts  = new ArrayList<>();

        // OPW = OpenWeatherMap , this is a mapping of my variables , to the variable names from the JSON file.

        final String OWP_LIST = "list";         // list , 3 hour intervals
        final String OWP_TEMPERATURE = "main";  // contains temperature and humidity readings
        final String OWP_WEATHER = "weather";   // list , containts "0" , temperature descriptions
        final String OWP_description = "description";
        final String OWP_THERMOKRASIA = "temp";

        final String OWP_MIN = "temp_min";
        final String OWP_MAX = "temp_max";
        final String OWP_hum = "humidity";

        final String OWP_WIND = "wind";         // contains wind speed and direction
        final String OWP_WINDS = "speed";
        final String OWP_WINDD = "deg";

        final String OWP_date = "dt_txt";



        //TODO clouds

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        if ( forecastJson.getString("cod").equals("200")){

            //Weather array, each item of the list is 3h apart
            JSONArray weatherArray = forecastJson.getJSONArray(OWP_LIST);   // Create an array weatherArray, using the array "list" of the JSON file.

            // Iterate through the array represanting the 3 hour period
            //Take the important info, and put them into an class WeatherEntry object.
            //Put them in the list , and return them

            for (int i = 1; i < weatherArray.length(); i++) {

                JSONObject forecast = weatherArray.getJSONObject(i);

                //Temperatures are a child array called "main"
                JSONObject temperatureObject = forecast.getJSONObject(OWP_TEMPERATURE);
                double temperature = temperatureObject.getDouble(OWP_THERMOKRASIA);
                double min = temperatureObject.getDouble(OWP_MIN);
                double max = temperatureObject.getDouble(OWP_MAX);
                String hum = temperatureObject.getString(OWP_hum);

                // Weather descriptions are ac child list called "weather" that contains array 0

                JSONObject weatherObject = forecast.getJSONArray(OWP_WEATHER).getJSONObject(0);
                String desc = weatherObject.getString(OWP_description);  // This is different main ...

                // Wind readings are in a child array called wind
                JSONObject windObject = forecast.getJSONObject(OWP_WIND);
                String wSpeed = windObject.getString(OWP_WINDS);
                String wDirection = windObject.getString(OWP_WINDD);

                // Date and time is just a key:value

                String dateTxt = forecast.getString(OWP_date);


                //Create the object of WeatherEntry ( which really is just 3 hours)
                WeatherEntry df = new WeatherEntry();

                df.setCurrentTemperature(formatTemperatureReadings(temperature));
                df.setTempMin(formatTemperatureReadings(min));
                df.setTempMax(formatTemperatureReadings(max));

//                df.setTempMin(String.valueOf(min));
//                df.setTempMax(String.valueOf(max));
                df.setHumidity(hum);

                df.setWeatherDescription(desc);
                df.setWindSpeed(wSpeed);
                df.setWindDirection(wDirection);

                df.setDate(dateTxt.substring(0 ,10));
                df.setTime(dateTxt.substring(11,16));


                dayForecasts.add(df);

                Log.v(LOG_TAG , "Object is like this : " + df.toString());
            }


            return dayForecasts;
        }
        else
            Log.e(LOG_TAG , "API REQUEST RETURNED CODE : " + forecastJson.getString("cod"));
            return dayForecasts;
    }

    private String formatTemperatureReadings(double temperature){

        long result = Math.round(temperature);

        return String.valueOf(result);

    }

    @Override
    protected void onPostExecute(List<WeatherEntry> dayForecasts) {
        super.onPostExecute(dayForecasts);

        Log.v(LOG_TAG , "onPostExecute...");

        if (dayForecasts != null) {
            forecastAdapter.setDataSet(dayForecasts);
        }
    }


}
