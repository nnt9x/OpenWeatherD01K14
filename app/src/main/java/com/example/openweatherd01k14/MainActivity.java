package com.example.openweatherd01k14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText edtCity;
    private String API_KEY = "";

    private TextView tvTemperature, tvHumidity;
    private ImageView imgWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtCity = findViewById(R.id.edtCity);
        // Bind id
        tvHumidity = findViewById(R.id.tvHumidity);
        tvTemperature = findViewById(R.id.tvTemperature);
        imgWeather = findViewById(R.id.imgWeather);


    }

    private String buildURL(String city) {
        return String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric&lang=vi",
                city, API_KEY);
    }
    private String buildImageUrl(String icon){
        return String.format("https://openweathermap.org/img/wn/%s@4x.png", icon);
    }

    public void searchWeatherCityByName(View view) {
        // Lay du lieu
        String city = edtCity.getText().toString();
        if (city.isEmpty()) {
            edtCity.setError("Hãy nhập thành phố cần tìm");
            return;
        }
        // Gui request len openweather
        String url = buildURL(city);

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Co du lieu
                        try {
                            // Nhiet do
                            double temp = response.getJSONObject("main").getDouble("temp");
                            tvTemperature.setText("Temperature: "+ temp +" *C");

                            // Do am
                            double humidity = response.getJSONObject("main").getDouble("humidity");
                            tvHumidity.setText("Humidity: "+ humidity+" %");

                            // Ap suat
                            double pressure = response.getJSONObject("main").getDouble("pressure");

                            // Icon
                            String icon = response.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String urlImage = buildImageUrl(icon);

                            Glide.with(MainActivity.this).load(urlImage).into(imgWeather);

//
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "API Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}