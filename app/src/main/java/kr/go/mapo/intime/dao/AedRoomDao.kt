package kr.go.mapo.intime.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.go.mapo.intime.model.AedRoom

@Dao
interface AedRoomDao {

    @Query("SELECT * FROM AedRoom")
    fun getAll(): List<AedRoom>

    @Insert
    fun insertHAedRoom(aedRoom: AedRoom)

    @Delete
    fun delete(aedRoom: AedRoom)

}