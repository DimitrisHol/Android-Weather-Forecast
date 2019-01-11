package gr.uom.android.myweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ForecastAdapter extends ArrayAdapter {

    //Here we have a list of WeatherEntries , Weather adapter has just a list of Strings
    private List<WeatherEntry> dataSet;

    private final LayoutInflater inflater;
    private final int layoutResource;


    //Constructor
    public ForecastAdapter(@NonNull Context context, int resource, @NonNull List<WeatherEntry> objects) {
        super(context, resource, objects);

        dataSet = objects;

        inflater = LayoutInflater.from(context);
        layoutResource = resource;
    }


//    public List<WeatherEntry> getDataSet() {
//        return dataSet;
//    }

    public WeatherEntry getDayForecast(int position){
        if (position < dataSet.size()){
            return dataSet.get(position);
        }
        return new WeatherEntry();
    }
    public void setDataSet(List<WeatherEntry> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }


    // Buffer, γεμιζει την πρωτη φορα για να μην χρειαζεται να καλεις για καθε στοιχειο ολα τα δεδομενα
    static class ForecastHolder {

        public TextView time;
        public TextView temperature;
        public TextView description;

        //TODO PUT IMAGE HERE

//        public TextView max;
//        public TextView min;
//        public TextView maxmin;

        public ForecastHolder(View Item){

            time = Item.findViewById(R.id.timeText);
            temperature = Item.findViewById(R.id.temperatureView);
            description = Item.findViewById(R.id.weatherDescView);
//            max = Item.findViewById(R.id.maxView);
//            min = Item.findViewById(R.id.minView);
//            maxmin = Item.findViewById(R.id.maxminView);

        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ForecastHolder forecastHolder;

        //Inflate only the first time
        if (convertView == null){
            convertView = inflater.inflate(layoutResource, parent ,false);
            forecastHolder = new ForecastHolder(convertView);

            convertView.setTag(forecastHolder);
        }

        else{   // Get the information from the ForecastHolder
            forecastHolder = (ForecastHolder) convertView.getTag();
        }

        WeatherEntry forecast = dataSet.get(position);

        forecastHolder.time.setText(forecast.getTime());
        forecastHolder.temperature.setText(forecast.getCurrentTemperature() + "˚C");
        forecastHolder.description.setText(formatDescription(forecast.getWeatherDescription()));

        // Acording to API , min/max are optional, only applicable to large cities and megalopolises

//        forecastHolder.maxmin.setText(forecast.getTempMax() +"˚ / " +forecast.getTempMin() +"˚");
//        forecastHolder.max.setText(forecast.getTempMax() +"˚");
//        forecastHolder.min.setText(forecast.getTempMin() +"˚");



        return convertView;
    }

    String formatDescription(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}

