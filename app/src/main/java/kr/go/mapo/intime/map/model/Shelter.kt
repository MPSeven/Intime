package kr.go.mapo.intime.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "shelter")
data class Shelter(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lon: Double,
    @SerializedName("address") val address: String,
    @SerializedName("shelterCategory") val shelterCategory: String,
    @SerializedName("capacityArea") val capacityArea: String,
    @SerializedName("placeName") val placeName: String,
)
