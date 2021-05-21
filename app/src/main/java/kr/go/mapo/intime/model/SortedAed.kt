package kr.go.mapo.intime.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class SortedAed(
    @SerializedName("distance") val distance: Double,
    @SerializedName("aed") val aed: Aed
)
