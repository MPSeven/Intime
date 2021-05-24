package kr.go.mapo.intime.setting.dao

import androidx.room.*
import kr.go.mapo.intime.map.model.Aed

@Dao
interface BookmarkAedDao {

    @Query("SELECT * FROM aed")
    fun getAll(): List<Aed>

    @Query("SELECT * FROM aed WHERE lat = :lat")
    fun getAed(lat: Double): Aed

    @Update
    fun updateAed(aed: Aed)

    @Insert
    fun insertAed(aed: Aed)

    @Query("DELETE FROM aed WHERE lat = :aedLat")
    fun delete(aedLat: Double)

    @Delete
    fun remove(aed: Aed)

    @Query("DELETE FROM aed")
    fun clearAll()

}