package osseman.weatherapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class GetCityFromCoordinates extends AsyncTask<Void, Void, String> {


    private MainActivity activity;
    private String endpoint;

    public GetCityFromCoordinates(MainActivity activity, String endpoint) {
        super();
        this.activity = activity;
        this.endpoint = endpoint;
    }

    @Override
    protected String doInBackground(Void... endpoint) {
        return BaseApiCaller.makeApiCall(this.endpoint);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null) {
            activity.createToast("Could not connect to API");
            return;
        }
        try {
            JSONObject apiData = new JSONObject(s);
            Log.i("Response from get city", apiData.toString());
            String city = apiData.getJSONObject("location").get("city").toString();
            String cityCode = apiData.getJSONObject("location").get("l").toString();
            activity.setCityNameFromCoordinates(city);

            // Fire the rest of the requests as we now know the city
            final GetCurrentCityWeather getCurrentCityWeather = new GetCurrentCityWeather(activity, "conditions" + cityCode + ".json");
            final GetHourlyForecast getHourlyForecast = new GetHourlyForecast(activity, "hourly" + cityCode + ".json");
            final GetDailyForecast getDailyForecast = new GetDailyForecast(activity, "forecast" + cityCode + ".json");
            getCurrentCityWeather.execute();
            getHourlyForecast.execute();
            getDailyForecast.execute();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("Toast", "About To create a toast");
            activity.createToast("Could not find a location for the given coordinates.");
        }
    }
}