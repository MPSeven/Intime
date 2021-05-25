package kr.go.mapo.intime.info.checklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist")
data class Checklist(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name="contents")
    val contents: String,

    @ColumnInfo(name="description")
    val description: String,

    @ColumnInfo(name="section")
    val section: Int,

    @ColumnInfo(name="chk")
    val chk: Boolean

)
