package com.example.apifetchingexamplefirst

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceMap {
    @GET("airlines")
    fun getFlightData(@Query("limit") limit: String, @Query("shuffle") shuffle: Boolean): Call<List<FlightData>>
}