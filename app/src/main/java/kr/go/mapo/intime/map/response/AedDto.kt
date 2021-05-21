package kr.go.mapo.intime.map.response

import com.google.gson.annotations.SerializedName
import kr.go.mapo.intime.map.model.SortedAed

data class AedDto(
    @SerializedName("count") val result: Int,
    @SerializedName("sortedAeds") val aeds: List<SortedAed>
)
