package gr.uom.android.myweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreDetails);

        Intent intent = getIntent();
        WeatherEntry weatherEntry = (WeatherEntry) intent.getSerializableExtra("weatherObject");

        TextView time = findViewById(R.id.timeText);
        TextView temperature = findViewById(R.id.temperatureText);
        TextView description = findViewById(R.id.weatherdescText);
        TextView humidity = findViewById(R.id.humidityPercent);
        TextView windSpeed = findViewById(R.id.windSpeed);


        time.setText(weatherEntry.getTime());
        temperature.setText(weatherEntry.getCurrentTemperature() + "˚C");
        description.setText(formatDescription(weatherEntry.getWeatherDescription()));
        humidity.setText(weatherEntry.getHumidity() + "%");
        windSpeed.setText(weatherEntry.getWindSpeed() + "KM/H " + formatWindDirection(weatherEntry.getWindDirection()));

    }

    String formatDescription(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    String formatWindDirection(String input){
        return input;
    }
}
