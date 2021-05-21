package kr.go.mapo.intime.map.api

import kr.go.mapo.intime.map.response.AedDto
import kr.go.mapo.intime.map.response.ShelterDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ShelterService {

    @GET("/api/shelter/json/{wgs84lat}/{wgs84lon}/{meter}")
    fun getSheltersByDistance(
        @Path("wgs84lat") lat: Double,
        @Path("wgs84lon") lon: Double,
        @Path("meter") km: Float
    ): Call<ShelterDto>
}