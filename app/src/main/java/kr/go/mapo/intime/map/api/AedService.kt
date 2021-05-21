package kr.go.mapo.intime.map.api

import kr.go.mapo.intime.map.response.AedDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AedService {

    @GET("/api/aed/json/{wgs84lat}/{wgs84lon}/{meter}")
    fun getAedsByDistance(
        @Path("wgs84lat") lat: Double,
        @Path("wgs84lon") lon: Double,
        @Path("meter") km: Float
    ): Call<AedDto>
}