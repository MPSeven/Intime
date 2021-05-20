package kr.go.mapo.intime.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kr.go.mapo.intime.model.Aed

@Dao
interface BookmarkAedDao {

    @Query("SELECT * FROM aed")
    fun getAll(): List<Aed>

    @Insert
    fun insertAed(aed: Aed)

    @Query("DELETE FROM aed WHERE lat = :aedLat")
    fun delete(aedLat: Double)

    @Query("DELETE FROM aed")
    fun clearAll()

}