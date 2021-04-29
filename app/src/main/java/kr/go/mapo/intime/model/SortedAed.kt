package kr.go.mapo.intime.model

import com.google.gson.annotations.SerializedName

data class SortedAed(
    @SerializedName("distance") val distance: Double,
    @SerializedName("aed") val aed: Aed
)
