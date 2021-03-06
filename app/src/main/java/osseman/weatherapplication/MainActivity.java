package osseman.weatherapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public Button button;
    // Current Weather
    public TextView city, currentTemperature, currentRainProb, currentConditions;
    public ImageView iconMain;
    //Daily Forecast
    public TextView day0, day1, day2, condition0, condition1, condition2, temp0, temp1, temp2;
    public TextView rainProb0, rainProb1, rainProb2;
    public ImageView icon0, icon1, icon2;
    //Hourly Forecast
    public TextView hour0, hour1, hour2, hour3, hour4;
    public TextView hourCondition0, hourCondition1, hourCondition2, hourCondition3, hourCondition4;
    public TextView hourTemp0, hourTemp1, hourTemp2, hourTemp3, hourTemp4;
    public TextView hourRainProb0, hourRainProb1, hourRainProb2, hourRainProb3, hourRainProb4;
    public ImageView hourIcon0, hourIcon1, hourIcon2, hourIcon3, hourIcon4;

    // Location Variables
    public LocationManager locationManager;
    private Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up views
        city = (TextView) findViewById(R.id.city);
        currentTemperature = (TextView) findViewById(R.id.currentTemp);
        currentConditions = (TextView) findViewById(R.id.currentCond);
        currentRainProb = (TextView) findViewById(R.id.currentRainProb);
        iconMain = (ImageView) findViewById(R.id.iconMain);
        day0 = (TextView) findViewById(R.id.day0);
        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        condition0 = (TextView) findViewById(R.id.condition0);
        condition1 = (TextView) findViewById(R.id.condition1);
        condition2 = (TextView) findViewById(R.id.condition2);
        icon0 = (ImageView) findViewById(R.id.icon0);
        icon1 = (ImageView) findViewById(R.id.icon1);
        icon2 = (ImageView) findViewById(R.id.icon2);
        temp0 = (TextView) findViewById(R.id.temp0);
        temp1 = (TextView) findViewById(R.id.temp1);
        temp2 = (TextView) findViewById(R.id.temp2);
        rainProb0 = (TextView) findViewById(R.id.rainProb0);
        rainProb1 = (TextView) findViewById(R.id.rainProb1);
        rainProb2 = (TextView) findViewById(R.id.rainProb2);
        hour0 = (TextView) findViewById(R.id.hour0);
        hour1 = (TextView) findViewById(R.id.hour1);
        hour2 = (TextView) findViewById(R.id.hour2);
        hour3 = (TextView) findViewById(R.id.hour3);
        hour4 = (TextView) findViewById(R.id.hour4);
        hourCondition0 = (TextView) findViewById(R.id.hourCondition0);
        hourCondition1 = (TextView) findViewById(R.id.hourCondition1);
        hourCondition2 = (TextView) findViewById(R.id.hourCondition2);
        hourCondition3 = (TextView) findViewById(R.id.hourCondition3);
        hourCondition4 = (TextView) findViewById(R.id.hourCondition4);
        hourTemp0 = (TextView) findViewById(R.id.hourTemp0);
        hourTemp1 = (TextView) findViewById(R.id.hourTemp1);
        hourTemp2 = (TextView) findViewById(R.id.hourTemp2);
        hourTemp3 = (TextView) findViewById(R.id.hourTemp3);
        hourTemp4 = (TextView) findViewById(R.id.hourTemp4);
        hourRainProb0 = (TextView) findViewById(R.id.hourRainProb0);
        hourRainProb1 = (TextView) findViewById(R.id.hourRainProb1);
        hourRainProb2 = (TextView) findViewById(R.id.hourRainProb2);
        hourRainProb3 = (TextView) findViewById(R.id.hourRainProb3);
        hourRainProb4 = (TextView) findViewById(R.id.hourRainProb4);
        hourIcon0 = (ImageView) findViewById(R.id.hourIcon0);
        hourIcon1 = (ImageView) findViewById(R.id.hourIcon1);
        hourIcon2 = (ImageView) findViewById(R.id.hourIcon2);
        hourIcon3 = (ImageView) findViewById(R.id.hourIcon3);
        hourIcon4 = (ImageView) findViewById(R.id.hourIcon4);

        // Set up location service

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        GpsListener locationListener = new GpsListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            Log.d("Check Permisions", "Stuck in persmisions");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 10, locationListener);



        button = (Button) findViewById(R.id.refreshButton);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            startPipeLine();
        } else {
            createToast("Please enable your GPS");
        }

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    startPipeLine();
                } else {
                    createToast("Please enable your GPS");
                }
            }
        });

    }

    protected void setCurrentWeather(JSONObject currentWeather) throws JSONException {
        Log.d("Json", currentWeather.toString());
        this.currentTemperature.setText(currentWeather.get("temperature").toString() + " C");
        this.currentConditions.setText(currentWeather.get("conditions").toString());
        this.iconMain.setImageResource(getImageId(currentWeather.get("icon").toString()));

    }
    protected void setCityNameFromCoordinates(String city) {
        this.city.setText("Today in: " + city);
    }
    
    protected void setHourlyForecast(JSONArray hourlyForecast) throws JSONException {
        hour0.setText(hourlyForecast.getJSONObject(0).get("time").toString());
        hour1.setText(hourlyForecast.getJSONObject(1).get("time").toString());
        hour2.setText(hourlyForecast.getJSONObject(2).get("time").toString());
        hour3.setText(hourlyForecast.getJSONObject(3).get("time").toString());
        hour4.setText(hourlyForecast.getJSONObject(4).get("time").toString());
        hourCondition0.setText(hourlyForecast.getJSONObject(0).get("condition").toString());
        hourCondition1.setText(hourlyForecast.getJSONObject(1).get("condition").toString());
        hourCondition2.setText(hourlyForecast.getJSONObject(2).get("condition").toString());
        hourCondition3.setText(hourlyForecast.getJSONObject(3).get("condition").toString());
        hourCondition4.setText(hourlyForecast.getJSONObject(4).get("condition").toString());
        hourTemp0.setText(hourlyForecast.getJSONObject(0).get("temperature").toString() + " C");
        hourTemp1.setText(hourlyForecast.getJSONObject(1).get("temperature").toString() + " C");
        hourTemp2.setText(hourlyForecast.getJSONObject(2).get("temperature").toString() + " C");
        hourTemp3.setText(hourlyForecast.getJSONObject(3).get("temperature").toString() + " C");
        hourTemp4.setText(hourlyForecast.getJSONObject(4).get("temperature").toString() + " C");
        hourRainProb0.setText(hourlyForecast.getJSONObject(0).get("rainProb").toString() + "%");
        hourRainProb1.setText(hourlyForecast.getJSONObject(1).get("rainProb").toString() + "%");
        hourRainProb2.setText(hourlyForecast.getJSONObject(2).get("rainProb").toString() + "%");
        hourRainProb3.setText(hourlyForecast.getJSONObject(3).get("rainProb").toString() + "%");
        hourRainProb4.setText(hourlyForecast.getJSONObject(4).get("rainProb").toString() + "%");
        hourIcon0.setImageResource(getImageId(hourlyForecast.getJSONObject(0).get("icon").toString()));
        hourIcon1.setImageResource(getImageId(hourlyForecast.getJSONObject(1).get("icon").toString()));
        hourIcon2.setImageResource(getImageId(hourlyForecast.getJSONObject(2).get("icon").toString()));
        hourIcon3.setImageResource(getImageId(hourlyForecast.getJSONObject(3).get("icon").toString()));
        hourIcon4.setImageResource(getImageId(hourlyForecast.getJSONObject(4).get("icon").toString()));
        Log.d("Forecast", "The Hourly forecast is: " + hourlyForecast.toString());
    }

    protected void setDailyForecast(JSONArray dailyForecast) throws JSONException {
        day0.setText(dailyForecast.getJSONObject(0).get("day").toString());
        day1.setText(dailyForecast.getJSONObject(1).get("day").toString());
        day2.setText(dailyForecast.getJSONObject(2).get("day").toString());
        condition0.setText(dailyForecast.getJSONObject(0).get("condition").toString());
        condition1.setText(dailyForecast.getJSONObject(1).get("condition").toString());
        condition2.setText(dailyForecast.getJSONObject(2).get("condition").toString());
        temp0.setText(dailyForecast.getJSONObject(0).get("temperature").toString() + " C");
        temp1.setText(dailyForecast.getJSONObject(1).get("temperature").toString() + " C");
        temp2.setText(dailyForecast.getJSONObject(2).get("temperature").toString() + " C");
        rainProb0.setText(dailyForecast.getJSONObject(0).get("rainProb").toString() + "%");
        rainProb1.setText(dailyForecast.getJSONObject(1).get("rainProb").toString() + "%");
        rainProb2.setText(dailyForecast.getJSONObject(2).get("rainProb").toString() + "%");
        currentRainProb.setText(dailyForecast.getJSONObject(3).get("currentRainProb").toString() + "%");
        icon0.setImageResource(getImageId(dailyForecast.getJSONObject(0).get("icon").toString()));
        icon1.setImageResource(getImageId(dailyForecast.getJSONObject(1).get("icon").toString()));
        icon2.setImageResource(getImageId(dailyForecast.getJSONObject(2).get("icon").toString()));


        Log.d("Forecast", "The Daily forecast is: " + dailyForecast.toString());
    }

    public int getImageId(String name) {
        Context c = MainActivity.this;
        c.getApplicationContext();
        return c.getResources().getIdentifier(name, "drawable", c.getPackageName());
    }

    protected void startPipeLine() {
        final MainActivity app = this;
        String locationString;
        // Setting Kaist as base mock location
        if (location == null) {
            locationString = "36.368982,127.363029";
        } else {
            locationString = Double.toString(this.location.getLatitude()) +
                    "," +
                    Double.toString(this.location.getLongitude());
        }

        final GetCityFromCoordinates getCityFromCoordinates = new GetCityFromCoordinates(app,
                "geolookup/q/"+ locationString + ".json");
        getCityFromCoordinates.execute();
    }

    public void createToast(String message) {
        Context context = getApplicationContext();
        Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
        t.show();
    }


    public void setLocation(Location location) {
        this.location = location;
    }
}
