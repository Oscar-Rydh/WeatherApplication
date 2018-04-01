package osseman.weatherapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


public class GetCurrentCityWeather extends AsyncTask<Void, Void, String> {

    private MainActivity activity;
    private String endpoint;

    public GetCurrentCityWeather(MainActivity activity, String endpoint) {
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
            String temperature = apiData.getJSONObject("current_observation").getString("temp_c");
            String conditions = apiData.getJSONObject("current_observation").getString("weather");
            String icon = apiData.getJSONObject("current_observation").getString("icon");
            JSONObject filteredData = new JSONObject();
            filteredData.put("temperature", temperature);
            filteredData.put("conditions", conditions);
            filteredData.put("icon", icon);
            activity.setCurrentWeather(filteredData);
        } catch (JSONException e) {
            activity.createToast("Could not find any current weather conditions");
            e.printStackTrace();
        }
    }
}