package gr.uom.android.myweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";


    private WeatherEntry weatherEntry;

    public WeatherFragment() {

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_weather, container, false);


        if (weatherEntry == null){

            Log.d(TAG, "Weather entry is null");

            // ΤΟ ΑΠΟΛΥΤΟ ΜΕΡΕΜΕΤΙ  -- ΔΗΜΗΤΡΗΣ Ο ΜΕΡΕΜΕΤΗΣ

            TextView humidityText = rootView.findViewById(R.id.humiditytextView);
            humidityText.setText("");

            TextView windText = rootView.findViewById(R.id.windtextView);
            windText.setText("");

        }
        else
        {
            Log.d(TAG, weatherEntry.toString());

            TextView time = rootView.findViewById(R.id.timeText);
            TextView temperature = rootView.findViewById(R.id.temperatureText);
            TextView description = rootView.findViewById(R.id.weatherdescText);
            TextView humidity = rootView.findViewById(R.id.humidityPercent);
            TextView windSpeed = rootView.findViewById(R.id.windSpeed);



            time.setText(weatherEntry.getTime());
            temperature.setText(weatherEntry.getCurrentTemperature() + "˚C");
            description.setText(formatDescription(weatherEntry.getWeatherDescription()));
            humidity.setText(weatherEntry.getHumidity() + "%");
            windSpeed.setText(weatherEntry.getWindSpeed() + "KM/H ");

            //+ formatWindDirection(weatherEntry.getWindDirection())

        }

        return rootView;

    }


    @Override
    public void onStart() {
        super.onStart();
    }


    String formatDescription(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    String formatWindDirection(String input){
        return input;
    }

    public void setWeatherEntry(WeatherEntry weatherEntry) {
        this.weatherEntry = weatherEntry;
    }
}
