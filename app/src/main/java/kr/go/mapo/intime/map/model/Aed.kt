package kr.go.mapo.intime.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "aed")
data class Aed(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @SerializedName("wgs84lat") val lat: Double,
    @SerializedName("wgs84lon") val lon: Double,
    @SerializedName("buildaddress") val address: String,
    @SerializedName("buildplace") val addressDetail: String,
    @SerializedName("clerktel") val tel: String,
    @SerializedName("org") val org: String
)
