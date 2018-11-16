package my.com.demo.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView tvLocation, tvTemperature, tvHumidity, tvWindSpeed,
            tvCloudiness;
    private Button btnRefresh;
    private ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        tvLocation = (TextView) findViewById(R.id.location);
        tvTemperature = (TextView) findViewById(R.id.temperature);
        tvHumidity = (TextView) findViewById(R.id.humidity);
        tvWindSpeed = (TextView) findViewById(R.id.wind_speed);
        tvCloudiness = (TextView) findViewById(R.id.cloudiness);
        btnRefresh = (Button) findViewById(R.id.button_refresh);
        ivIcon = (ImageView) findViewById(R.id.icon);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private class WeatherDataRetrival extends AsyncTask<Void, Void, String> {

        private static final String WEATHER_SOURCE =
"http://api.openweathermap.org/data/2.5/weather?APPID=82445b6c96b99bc3ffb78a4c0e17fca5&mode=json&id=1735161";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void...arg0) {

            NetworkInfo networkInfo = ((ConnectivityManager) MainActivity.this
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // network-connected

                URL url = null;
                try {
                    url = new URL(WEATHER_SOURCE);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("Get");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.connect();


                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    if (bufferedReader != null) {
                        String readline;
                        StringBuffer stringBuffer = new StringBuffer();
                        while ((readline=bufferedReader.readLine()) != null) {
                            stringBuffer.append(readline);
                        }
                        return stringBuffer.toString();
                    }
                }
                } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            } else {
// no connection…
            }


            return null;
        }
        @Override
        protected void onPostExecute(String result) {


            if (result!=null) {
                final JSONObject weatherJSON;
                try {
                    weatherJSON = new JSONObject(result);

                tvLocation.setText(weatherJSON.getString("name") + "," +
                        weatherJSON.getJSONObject("sys").getString("country"));
                tvWindSpeed.setText(String.valueOf(weatherJSON.getJSONObject("wind").getDouble("speed")) + " mps");
                tvCloudiness.setText(String.valueOf(weatherJSON.getJSONObject("clouds").
                        getInt("all")) + "%");
                final JSONObject mainJSON = weatherJSON.getJSONObject("main");
                tvTemperature.setText(String.valueOf(mainJSON.getDouble("temp")));
                tvHumidity.setText(String.valueOf(mainJSON.getInt("humidity")) + "%");


                    final JSONArray weatherJSONArray = weatherJSON.getJSONArray("weather");
                    if (weatherJSONArray.length()>0) {
                        int code = weatherJSONArray.getJSONObject(0).getInt("id");
                        ivIcon.setImageResource(getIcon(code));
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            }

            super.onPostExecute(result);
        }

        private int getIcon(int code) {

            String Weather_Thunderstorm= "200,201,202,210,211,212,221,230,231,232";
            String Weather_Drizzle= "300,301,302,310,311,312,313,314,321";
            String Weather_Rain = "500,501,502,503,504,511,520,521,522,531";
            String Weather_Snow = "600,601,602,611,612,615,616,620,621,622";
            String Weather_Clear_Sky ="800";
            String Weather_Few_Clouds ="801";
            String Weather_Scattered_Clouds ="802";
            String Weather_Broken_and_Overcast_Clouds="803, 804";
            String Weather_Fog= "701,711,721,731,741,751,761,762";
            String Weather_Tornado= "781,900";
            String Weather_Windy= "905";
            String Weather_Hail= "906";

            if(Weather_Thunderstorm.contains(String.valueOf(code)))
                return R.drawable.ic_thunderstorm_large;
            else if(Weather_Drizzle.contains(String.valueOf(code)))
                return R.drawable.ic_drizzle_large;
            else if(Weather_Rain.contains(String.valueOf(code)))
                return R.drawable.ic_rain_large;
            else if(Weather_Snow.contains(String.valueOf(code)))
                return R.drawable.ic_snow_large;
            else if(Weather_Clear_Sky.contains(String.valueOf(code)))
                return R.drawable.ic_day_clear_large;
            else if(Weather_Few_Clouds.contains(String.valueOf(code)))
                return R.drawable.ic_day_few_clouds_large;
            else if(Weather_Scattered_Clouds.contains(String.valueOf(code)))
                return R.drawable.ic_scattered_clouds_large;
            else if(Weather_Broken_and_Overcast_Clouds.contains(String.valueOf(code)))
                return R.drawable.ic_broken_clouds_large;
            else if(Weather_Fog.contains(String.valueOf(code)))
                return R.drawable.ic_fog_large;
            else if(Weather_Tornado.contains(String.valueOf(code)))
                return R.drawable.ic_tornado_large;
            else if(Weather_Windy.contains(String.valueOf(code)))
                return R.drawable.ic_windy_large;
            else if(Weather_Hail.contains(String.valueOf(code)))
                return R.drawable.ic_hail_large;
            else
                return -1;


        }


    }
}
