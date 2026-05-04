package com.pourab.weathermate

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    var city : String = "London"
    var api : String = "7d5216b6848c90b471e85524f9d94ed9"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        weathertask().execute()

        findViewById<com.google.android.material.button.MaterialButton>(R.id.Changetext_city).setOnClickListener{
            val input = findViewById<EditText>(R.id.edittext_city).text.toString().trim()
            if (input.isNotEmpty()) {
                city = input
                weathertask().execute()
            }
        }

    }

    inner class weathertask(): AsyncTask<String, Void, String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<RelativeLayout>(R.id.UI_holder).visibility = View.GONE
            findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
        }
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                    .readText(Charsets.UTF_8)
            }catch (e: Exception){
                response = null
            }
            return response

            }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val temp = main.getString("temp") + "°C"
                val humidity = main.getString("humidity") + "%"
                val windspeed = wind.getString("speed") + "km/h"
                val mainMain = weather.getString("main")
                val icon = weather.getString("icon")

                findViewById<TextView>(R.id.city).text = city
                findViewById<TextView>(R.id.temperature).text = temp
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.Wind).text = windspeed
                findViewById<TextView>(R.id.text_Conditon).text = mainMain

                when(icon){
                    "01d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.sun__1_)
                    "01n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.moon)
                    "02d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.sun_cloud)
                    "02n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.moon_cloud)
                    "03d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.cloud)
                    "03n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.cloud)
                    "04d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.cloud)
                    "04n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.cloud)
                    "09d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.humidity)
                    "09n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.humidity)
                    "10d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.humidity)
                    "10n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.humidity)
                    "11d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.storm)
                    "11n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.storm)
                    "13d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.snow)
                    "13n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.snow)
                    "50d" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.mist)
                    "50n" -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.mist)
                    else -> findViewById<ImageView>(R.id.img_condition).setImageResource(R.drawable.sun__1_)
                }
                findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.UI_holder).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                findViewById<RelativeLayout>(R.id.UI_holder).visibility = View.GONE
            }

        }
        }


}