package com.example.apifetchingexamplefirst

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import androidx.lifecycle.lifecycleScope
import com.example.apifetchingexamplefirst.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var airlineCodeInputLayout: TextInputLayout
    private lateinit var flightNameTextView: TextView
    private lateinit var flightCodeTextView: TextView
    private lateinit var airlineimage: ImageView
    private lateinit var dailyFlightsCountTextView: TextView
    private lateinit var apiService: ApiService
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialization
        airlineCodeInputLayout = binding.textInputLayout
        flightNameTextView = binding.flightname
        flightCodeTextView = binding.flightcode
        dailyFlightsCountTextView = binding.dailyflightscount
        airlineimage = binding.imageView


        // Initialization of  Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-farefirst-test.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        val searchButton =binding.buttonsearch
        searchButton.setOnClickListener {
            getDataFromAPI()
            hideKeyboard()
        }
        binding.etBoardName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getDataFromAPI()
                hideKeyboard()
            }
            true
        }
        binding.buttonNextProject.setOnClickListener {
            var intent =Intent(this,ProjectSecondActivity::class.java)
            startActivity(intent)
        }
    }
//    private fun getDataFromAPI() {
//        val airlineCode = airlineCodeInputLayout.editText?.text.toString()
//
//        if (airlineCode.isNotEmpty()) {
//            lifecycleScope.launch(Dispatchers.Main) {
//                try {
//                    val flightData = apiService.getFlightData(airlineCode)
//
//
//                    val gson = Gson() // You need to include Gson library in your project
//                    val flightMap = gson.fromJson(flightData, object : TypeToken<HashMap<String, Any>>() {}.type)
//
//
//                    flightNameTextView.text = flightMap["name"]?.toString() ?: "No Name"
//                    flightCodeTextView.text = flightMap["code"]?.toString() ?: "No Code"
//                    dailyFlightsCountTextView.text = flightMap["dailyFlights"]?.toString() ?: "No count"
//
//                    Glide.with(this@MainActivity)
//                        .load(flightMap["imageUrl"]?.toString())
//                        .into(airlineimage)
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }


//    private fun getDataFromAPI() {
//        val airlineCode = airlineCodeInputLayout.editText?.text.toString()
//
//        if (airlineCode.isNotEmpty()) {
//            lifecycleScope.launch(Dispatchers.Main) {
//                try {
//                    val flightData = apiService.getFlightData(airlineCode)
//
//                    // Conversion of json response to map
//                    val flightMap: Map<String, Any?> = mapOf(
//                        "name" to flightData.name,
//                        "code" to flightData.code,
//                        "dailyFlights" to flightData.dailyFlights,
//                        "imageUrl" to flightData.imageUrl
//                    )
//
//                    // Populating the text views using the flightMap
//                    flightNameTextView.text = flightMap["name"]?.toString() ?: "No Name"
//                    flightCodeTextView.text = flightMap["code"]?.toString() ?: "No Code"
//                    dailyFlightsCountTextView.text = flightMap["dailyFlights"]?.toString() ?: "No count"
//
//
//                    Glide.with(this@MainActivity)
//                        .load(flightMap["imageUrl"])
//                        .into(airlineimage)
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
    private fun getDataFromAPI() {
        val airlineCode = airlineCodeInputLayout.editText?.text.toString()

        if (airlineCode.isNotEmpty()) {
            lifecycleScope.launch(Dispatchers.Main) {
                try {
                    val flightData = apiService.getFlightData(airlineCode)
                    // Populating the text views
                    flightNameTextView.text = flightData.name?:"No Name"
                    flightCodeTextView.text = flightData.code?:"No Code"
                    dailyFlightsCountTextView.text = (flightData.dailyFlights?:"No count").toString()

                    // Using Glide to load image into ImageView
                    Glide.with(this@MainActivity)
                        .load(flightData.imageUrl)
                        .into(airlineimage)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}