package gr.uom.android.myweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";


    public WeatherFragment() {


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_weather, container, false);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

}
