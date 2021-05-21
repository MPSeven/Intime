package kr.go.mapo.intime.response

import com.google.gson.annotations.SerializedName
import kr.go.mapo.intime.model.SortedAed

data class AedDto(
    @SerializedName("count") val result: Int,
    @SerializedName("sortedAeds") val aeds: List<SortedAed>
)
