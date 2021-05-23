package kr.go.mapo.intime.setting.dao

import androidx.room.*
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.model.SelectCon

@Dao
interface ContactsDao {

    @Query("SELECT * FROM contacts order by id desc")
    fun getCon(): List<Contacts>

    @Insert
    fun insertCon(vararg contacts: Contacts)

    @Delete
    fun deleteCon(contacts: Contacts)

    @Update
    fun updateCon(contacts: Contacts)

    @Query("SELECT phoneNumber FROM contacts WHERE chk = :check")
    fun selectSms(check: Boolean): SelectCon

    @Query("SELECT count(id) FROM contacts WHERE chk = :check")
    fun countSms(check: Boolean): Int

    @Query("SELECT count(id) FROM contacts")
    fun countAll(): Int

}