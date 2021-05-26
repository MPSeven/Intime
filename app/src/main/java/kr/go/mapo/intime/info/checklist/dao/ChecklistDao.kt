package kr.go.mapo.intime.info.checklist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kr.go.mapo.intime.info.checklist.model.Checklist

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklist order by id desc")
    fun getList(): List<Checklist>

    @Insert
    fun insert(vararg checklist: Checklist)

    @Query("UPDATE checklist SET chk = 'true' WHERE id = :id")
    fun updateTrue(id: Int)

    @Query("UPDATE checklist SET chk = 'false' WHERE id = :id")
    fun updateFalse(id: Int)

}