package kr.go.mapo.intime.map.response

import com.google.gson.annotations.SerializedName
import kr.go.mapo.intime.map.model.SortedShelter

data class ShelterDto(
    @SerializedName("count") val result: Int,
    @SerializedName("sortedShelters") val shelters: List<SortedShelter>
)
