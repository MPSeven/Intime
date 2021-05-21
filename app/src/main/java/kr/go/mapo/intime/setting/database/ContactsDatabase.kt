package kr.go.mapo.intime.setting.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.go.mapo.intime.model.Contacts
import kr.go.mapo.intime.setting.dao.ContactsDao

@Database(entities = [Contacts::class], version = 1, exportSchema = false)
abstract class ContactsDatabase: RoomDatabase()  {
    abstract fun contactsDao(): ContactsDao

    companion object{
        private var instance: ContactsDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ContactsDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, ContactsDatabase::class.java, "db-contacts").allowMainThreadQueries().build()
            }
            return instance
        }
    }
}