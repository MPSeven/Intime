package kr.go.mapo.intime.model

import com.google.gson.annotations.SerializedName

data class AedDto(
    @SerializedName("count") val result: Int,
    @SerializedName("sortedAeds") val aeds: List<SortedAed>
)
