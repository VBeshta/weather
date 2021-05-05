package com.app.weather

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity(){
    private var user_field: EditText? = null
    private var getData_button: Button? = null
    private var temp_info: TextView? = null
    private var description_info_view: TextView? = null
    private var address: TextView? = null

    var cityToSave: String = ""

    var pref : SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = getSharedPreferences("TABLE", MODE_PRIVATE)
        user_field = findViewById(R.id.city_field)
        getData_button = findViewById(R.id.main_btn)
        temp_info = findViewById(R.id.temp_info)
        address = findViewById(R.id.address)
        description_info_view = findViewById(R.id.description_info)
        description_info_view?.text = " "
        readData()
    }

    fun onClickGetCity(view: View) {
        if(  (user_field?.text?.toString() == "" || user_field?.text?.toString() == "" && cityToSave == "")) {
            showToast()
        }
        else {
            val cityName: String = user_field?.text.toString()
            val apiKey: String = "56d1fb3ba11cf7b64fc0b434ca952fa4"
            val url: String =
                "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$apiKey&units=metric&lang=ru"
            doAsync {
                setData(url, cityName)
            }
        }
    }


    private fun readData(){
        address?.text = pref?.getString("city_country", "")
        cityToSave = pref?.getString("cityName", "").toString()
        temp_info?.text = pref?.getString("temp", "").toString()
        description_info_view?.text = pref?.getString("description", "").toString()
    }


    private fun setData(url: String, city:String){
        val response = URL(url).readText()
        val weather = JSONObject(response).getJSONArray("weather")
        val description = weather.getJSONObject(0).getString("description")
        val main = JSONObject(response).getJSONObject("main")
        val temp = main.getString("temp") + "°C"
        val sys = JSONObject(response).getJSONObject("sys")
        val country = sys.getString("country")
        address?.text = "$country,$city"
        temp_info?.text = temp
        description_info_view?.text = description.toString()
    }

    fun showToast(){
        Toast.makeText(this, "Enter the city name", Toast.LENGTH_LONG).show()
    }

}