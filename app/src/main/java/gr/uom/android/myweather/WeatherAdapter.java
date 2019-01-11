package gr.uom.android.myweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter {


    // Again we are gonna have a list of WeatherEntries
    private List<WeatherEntry> currentWeather;

    private final LayoutInflater inflater;
    private final int layoutResource;

    //Constructor
    public WeatherAdapter(@NonNull Context context, int resource, @NonNull List<WeatherEntry> objects) {
        super(context, resource, objects);

        currentWeather = objects;

        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }

    public List<WeatherEntry> getCurrentWeather() {
        return currentWeather;
    }

    public WeatherEntry getWeatherData(int position) {
        if (position < currentWeather.size()) {
            return currentWeather.get(position);
        }
        return new WeatherEntry();

    }

    public void setCurrentWeather(List<WeatherEntry> currentWeather) {
        this.currentWeather = currentWeather;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return currentWeather.size();
    }

    //Buffer
    static class WeatherHolder{

        public TextView time;
        public TextView temperature;
        public TextView description;
        public TextView humidity;
        public TextView windSpeed;


        public WeatherHolder(View Item){

            time = Item.findViewById(R.id.timeText);
            temperature = Item.findViewById(R.id.temperatureText);
            description = Item.findViewById(R.id.weatherdescText);
            humidity = Item.findViewById(R.id.humidityPercent);
            windSpeed = Item.findViewById(R.id.windSpeed);

        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        WeatherHolder weatherHolder;

        //Inflate only the first time
        if (convertView == null){
            convertView = inflater.inflate(layoutResource , parent ,false);
            weatherHolder = new WeatherHolder(convertView);

            convertView.setTag(weatherHolder);
        }

        else { // Get the data from WeatherHolder

            weatherHolder = (WeatherHolder) convertView.getTag();

        }

        WeatherEntry weather = currentWeather.get(position);

        weatherHolder.time.setText(weather.getTime());
        weatherHolder.temperature.setText(weather.getCurrentTemperature() + "˚C");
        weatherHolder.description.setText(weather.getWeatherDescription());
        weatherHolder.humidity.setText(weather.getHumidity() + "%");
        weatherHolder.windSpeed.setText(weather.getWindSpeed() + "KM/H");





        return convertView;
    }
}
