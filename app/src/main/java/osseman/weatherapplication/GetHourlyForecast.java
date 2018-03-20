package osseman.weatherapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by osseman on 2018-03-19.
 */

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
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;

        try {
            URL url = new URL (baseUrl + this.endpoint);

            Log.i("url", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            response = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
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
            e.printStackTrace();
        }
    }
}