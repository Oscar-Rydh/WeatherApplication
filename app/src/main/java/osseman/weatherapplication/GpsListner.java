package osseman.weatherapplication;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by osseman on 2018-03-19.
 */

public class GpsListner implements LocationListener {

    private Location location;

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "Location changed");
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public double getLat() {
        return location.getLatitude();
    }

    public double getLong() {
        return location.getLongitude();
    }

}
