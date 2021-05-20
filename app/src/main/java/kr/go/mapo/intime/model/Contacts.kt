package kr.go.mapo.intime.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contacts(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val phoneNumber: String
)
