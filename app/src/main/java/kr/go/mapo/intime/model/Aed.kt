package kr.go.mapo.intime.model

import com.google.gson.annotations.SerializedName

data class Aed(
    @SerializedName("wgs84lat") val lat: Double,
    @SerializedName("wgs84lon") val lon: Double,
    @SerializedName("buildaddress") val address: String,
    @SerializedName("buildplace") val addressDetail: String,
    @SerializedName("clerktel") val tel: String
)
