package kr.go.mapo.intime.setting.dao

import androidx.room.*
import kr.go.mapo.intime.map.model.Aed

@Dao
interface BookmarkAedDao {

    @Query("SELECT * FROM aed")
    fun getAll(): List<Aed>

    @Update
    fun updateAed(aed: Aed)

    @Insert
    fun insertAed(aed: Aed)

    @Query("DELETE FROM aed WHERE lat = :aedLat")
    fun delete(aedLat: Double)

    @Query("DELETE FROM aed")
    fun clearAll()

}