package kr.go.mapo.intime.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AedRoom(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name = "org") val org: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "addressDetail") val addressDetail: String?,
    @ColumnInfo(name = "tel") val tel: String?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lon") val lon: Double?
)
