package osseman.weatherapplication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetDailyForecast extends AsyncTask<Void, Void, String> {

    private MainActivity activity;
    private String endpoint;

    public GetDailyForecast(MainActivity activity, String endpoint) {
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
            JSONArray dailyForecast = apiData.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday");
            JSONArray finalForecast = new JSONArray();
            for (int i = 1; i < 4; i++) {
                JSONObject currentForecast = dailyForecast.getJSONObject(i);
                String day = currentForecast.getJSONObject("date").get("weekday").toString();
                String temperature = currentForecast.getJSONObject("high").get("celsius").toString();
                String condition = currentForecast.get("conditions").toString();
                String rainProb = currentForecast.get("pop").toString();
                String icon = currentForecast.get("icon").toString();
                JSONObject filteredForecast = new JSONObject();
                filteredForecast.put("day", day);
                filteredForecast.put("temperature", temperature);
                filteredForecast.put("condition", condition);
                filteredForecast.put("rainProb", rainProb);
                filteredForecast.put("icon", icon);
                finalForecast.put(filteredForecast);
            }
            JSONObject currentRainProb = new JSONObject();
            currentRainProb.put("currentRainProb", dailyForecast.getJSONObject(0).get("pop"));
            finalForecast.put(currentRainProb);
            activity.setDailyForecast(finalForecast);
        } catch (JSONException e) {
            activity.createToast("Could not find a daily forecast");
            e.printStackTrace();
        }
    }
}