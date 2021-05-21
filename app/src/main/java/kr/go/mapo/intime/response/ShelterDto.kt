package kr.go.mapo.intime.response

import com.google.gson.annotations.SerializedName
import kr.go.mapo.intime.model.SortedAed
import kr.go.mapo.intime.model.SortedShelter

data class ShelterDto(
    @SerializedName("count") val result: Int,
    @SerializedName("sortedShelters") val shelters: List<SortedShelter>
)
