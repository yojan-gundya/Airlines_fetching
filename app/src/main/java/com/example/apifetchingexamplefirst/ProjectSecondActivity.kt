package com.example.apifetchingexamplefirst

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apifetchingexamplefirst.databinding.ActivityProjectSecondBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProjectSecondActivity : AppCompatActivity() {
    private lateinit var limit: TextView
    private var result: Boolean = false
    private lateinit var rvMain: RecyclerView
    private lateinit var myAdapter: FlightDataAdapter
    private lateinit var mProgressDialog: Dialog
    private lateinit var binding: ActivityProjectSecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        limit = binding.enterFlightsLimit
        rvMain = binding.rvflightdata
        rvMain.layoutManager = LinearLayoutManager(this)

        myAdapter = FlightDataAdapter(baseContext, emptyList()) // Initialization  an empty list
        rvMain.adapter = myAdapter


        val switch: Switch = findViewById(R.id.switch1)
        switch.setOnCheckedChangeListener { _, isChecked ->
            result = isChecked
            println("Switch is turned $result")
            Log.i("switch", "switched on")
        }

        val searchButton = binding.button
        searchButton.setOnClickListener {
            getDataFromAPI()
            showprogressDialog("fetching")
            hideKeyboard()
        }

        binding.enterFlightsLimit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                showprogressDialog("fetching")
                getDataFromAPI()
                hideKeyboard()
            }
            true
        }
        setUpActionBar()

    }

    private fun getDataFromAPI() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://us-central1-farefirst-test.cloudfunctions.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceSecond::class.java)

        val limitnumber = limit.text.toString()
        val shuffle = result

        if (limitnumber.isNotEmpty()) {
            val fetchedData = retrofit.getFlightData(limitnumber, shuffle)
            fetchedData.enqueue(object : Callback<List<FlightData>> {
                override fun onResponse(
                    call: Call<List<FlightData>>,
                    response: Response<List<FlightData>>
                ) {
                    if (response.isSuccessful) {
                        hideProgressDialog()
                        val flightDataList = response.body()
                        val flightDataMapList: List<Map<String, Any>>? =
                            flightDataList?.map { flightData ->
                                mapOf(
                                    "name" to flightData.name as Any,
                                    "code" to flightData.code as Any,
                                    "dailyFlights" to flightData.dailyFlights as Any,
                                    "imageUrl" to flightData.imageUrl as Any
                                )
                            }
                        Log.i("Response", flightDataList.toString())
                        myAdapter = FlightDataAdapter(baseContext, flightDataMapList ?: emptyList())
                        rvMain.adapter = myAdapter


                    } else {
                        Log.e("API Error", "Response not successful: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<FlightData>>, t: Throwable) {
                    Log.e("API Error", "Network request failed", t)
                }
            })
        }
    }

    fun showprogressDialog(text: String) {
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.progressdialog)
        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        if (::mProgressDialog.isInitialized && mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun setUpActionBar() {
        val toolbarMain = binding.apifetchtoolbar
        setSupportActionBar(toolbarMain)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }
        toolbarMain.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}