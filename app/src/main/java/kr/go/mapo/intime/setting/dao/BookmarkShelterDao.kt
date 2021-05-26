package kr.go.mapo.intime.setting.dao

import androidx.room.*
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.model.Shelter

@Dao
interface BookmarkShelterDao {

    @Query("SELECT * FROM shelter")
    fun getAll(): List<Shelter>

    @Query("SELECT * FROM shelter WHERE lat = :lat")
    fun getShelter(lat: Double): Shelter

    @Update
    fun updateShelter(shelter: Shelter)

    @Insert
    fun insertShelter(shelter: Shelter)

    @Query("DELETE FROM shelter WHERE lat = :shelterLat")
    fun delete(shelterLat: Double)

    @Delete
    fun remove(shelter: Shelter)

    @Query("DELETE FROM shelter")
    fun clearAll()

}