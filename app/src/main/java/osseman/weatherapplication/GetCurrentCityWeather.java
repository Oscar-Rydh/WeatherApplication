package osseman.weatherapplication;

import android.os.AsyncTask;
import android.util.Log;

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

public class GetCurrentCityWeather extends AsyncTask<Void, Void, String> {

    private String baseUrl = "http://api.wunderground.com/api/75efe12cd4f4d763/";
    private JSONObject response;
    private MainActivity activity;
    private String endpoint;

    public GetCurrentCityWeather(MainActivity activity, String endpoint) {
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
            String temperature = apiData.getJSONObject("current_observation").getString("temp_c");
            String conditions = apiData.getJSONObject("current_observation").getString("weather");
            JSONObject filteredData = new JSONObject();
            filteredData.put("temperature", temperature);
            filteredData.put("conditions", conditions);
            activity.setCurrentWeather(filteredData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}