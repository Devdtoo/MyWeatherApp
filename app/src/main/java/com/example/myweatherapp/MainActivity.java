package com.example.myweatherapp;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    Button myButton;
    EditText city;
    TextView resultClimate;
    TextView resultLat;
    TextView resultLon;
    TextView temp_tv;

    //  https://api.openweathermap.org/data/2.5/weather?q=lucknow&appid=15bb59511e15f1086dbac85cf0989b9a
    String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
    String API = "&appid=15bb59511e15f1086dbac85cf0989b9a";
    String myURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress);
        myButton = findViewById(R.id.button);
        city = findViewById(R.id.getCity);
        resultClimate = findViewById(R.id.resultC);
        resultLat = findViewById(R.id.resultLat);
        resultLon = findViewById(R.id.resultLon);
        temp_tv = findViewById(R.id.temperature);


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                progressBar.setVisibility(View.VISIBLE);

                Log.i("Tap", "I'm Tapped");
                if (city.getText().toString() == null) {
                    Log.i("Invalid","No City Found!!!");
                }
                else {
                    myURL = baseURL + city.getText().toString() + API;
                    Log.i("URL","Final URL: "+myURL);
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
//                                Toast.makeText(MainActivity.this, "JSON: " + response, Toast.LENGTH_LONG).show();
//                                Log.i("JSON", "JSON:" + response);


                                try {
                                    String weatherInfo = response.getString("weather");
//                                    Log.i("Weather","Expand: "+ weatherInfo);

                                    JSONArray jsonArray = new JSONArray(weatherInfo);

                                    for (int i = 0; i < jsonArray.length(); i++)  {
                                        JSONObject parObj = jsonArray.getJSONObject(i);
                                        String myClimate = "Climate: " + parObj.getString("main");
                                        resultClimate.setText(myClimate);


//                                        Toast.makeText(MainActivity.this,
//                                                "ID: "+parObj.getString("id")+"\n"
//                                                +
//                                                "Main: "+parObj.getString("main")+"\n"
//                                                +
//                                                "Description: "+parObj.getString("description"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    String coordinate = response.getString("coord");

                                    Log.i("coord", "CORD: "+ coordinate);
                                    JSONObject co = new JSONObject(coordinate);
                                    String lat = "Latitude: " + co.getString("lat");
                                    String lon = "Longitude: " + co.getString("lon");
                                    resultLat.setText(lat);
                                    resultLon.setText(lon);

                                    String parentTemp = response.getString("main");
                                    JSONObject tempObj = new JSONObject(parentTemp);
                                    String tempFromJSON = tempObj.getString("temp") ;
                                    float tempInF = Float.parseFloat(tempFromJSON);

                                    String tempInC ="Temperature: "+ ( (tempInF-32) * 5 ) / 9;
                                    String temperature = tempInC +"C";

                                    temp_tv.setText(temperature);


                                    Log.i("Longitude","Coordinates Are: "+ lon);
                                    Log.i("Latitude","Coordinates Are: "+ lat);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "WEATHER FETCHED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Something Went Wrong!!!", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }
                );

                MySingleton.getInstance(MainActivity.this).addToRequestQue(jsonObjectRequest);
            }
        });

    }
}
