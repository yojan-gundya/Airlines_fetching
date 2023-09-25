package com.example.apifetchingexamplefirst

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("airline")
    suspend fun getFlightData(@Query("q") code: String): FlightData
}

