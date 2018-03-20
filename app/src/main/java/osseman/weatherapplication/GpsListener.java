package osseman.weatherapplication;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by osseman on 2018-03-19.
 */

public class GpsListener implements LocationListener {

    private final MainActivity activity;

    public GpsListener(MainActivity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("New Location", Double.toString(location.getLatitude()) +
                "  " + Double.toString(location.getLongitude()));
        setLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        activity.createToast("GPS enabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        activity.createToast("Please enable your GPS");
    }

    public void setLocation(Location location) {
        activity.setLocation(location);
    }

}
