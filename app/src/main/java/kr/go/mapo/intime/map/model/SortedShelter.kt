package kr.go.mapo.intime.map.model

import com.google.gson.annotations.SerializedName

data class SortedShelter(
    @SerializedName("distance") val distance: Double,
    @SerializedName("shelter") val shelter: Shelter
)
