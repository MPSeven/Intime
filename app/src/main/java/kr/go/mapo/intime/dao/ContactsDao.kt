package kr.go.mapo.intime.dao

import androidx.room.*
import kr.go.mapo.intime.model.Contacts

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts order by id desc")
    fun getCon(): List<Contacts>

    @Insert
    fun insertCon(vararg contacts: Contacts)

    @Delete
    fun deleteCon(contacts: Contacts)

    @Update
    fun updateCon(Contacts: Contacts)
}