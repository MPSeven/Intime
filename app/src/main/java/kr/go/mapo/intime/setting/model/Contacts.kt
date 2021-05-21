package kr.go.mapo.intime.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contacts(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="phoneNumber")
    val phoneNumber: String,

    @ColumnInfo(name="chk", defaultValue = "false")
    val chk: Boolean
)

data class SelectCon(

    @ColumnInfo(name="phoneNumber")
    val phoneNumber: String

)