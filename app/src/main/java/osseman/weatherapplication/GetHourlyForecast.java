package osseman.weatherapplication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetHourlyForecast extends AsyncTask<Void, Void, String> {

    private String baseUrl = "http://api.wunderground.com/api/75efe12cd4f4d763/";
    private JSONObject response;
    private MainActivity activity;
    private String endpoint;

    public GetHourlyForecast(MainActivity activity, String endpoint) {
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
            JSONArray hourlyForecast = apiData.getJSONArray("hourly_forecast");
            JSONArray finalForecast = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject currentForecast = hourlyForecast.getJSONObject(i);
                String time = currentForecast.getJSONObject("FCTTIME").get("hour").toString();
                String temperature = currentForecast.getJSONObject("temp").get("metric").toString();
                String condition = currentForecast.get("condition").toString();
                String rainProb = currentForecast.get("pop").toString();
                JSONObject filteredForecast = new JSONObject();
                filteredForecast.put("time", time);
                filteredForecast.put("temperature", temperature);
                filteredForecast.put("condition", condition);
                filteredForecast.put("rainProb", rainProb);
                finalForecast.put(filteredForecast);
            }

            activity.setHourlyForecast(finalForecast);
        } catch (JSONException e) {
            activity.createToast("Could not find an hourly forecast");
            e.printStackTrace();
        }
    }
}