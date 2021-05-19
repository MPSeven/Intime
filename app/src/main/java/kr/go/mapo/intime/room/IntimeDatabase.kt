package kr.go.mapo.intime.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.go.mapo.intime.dao.ContactsDao
import kr.go.mapo.intime.model.Contacts

@Database(entities = [Contacts::class], version = 1, exportSchema = false)
abstract class IntimeDatabase: RoomDatabase()  {
    abstract fun contactsDao(): ContactsDao

    companion object{
        private var instance: IntimeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): IntimeDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, IntimeDatabase::class.java, "db-contacts").allowMainThreadQueries().build()
            }
            return instance
        }
    }
}