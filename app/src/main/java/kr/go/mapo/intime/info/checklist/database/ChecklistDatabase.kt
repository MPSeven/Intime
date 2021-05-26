package kr.go.mapo.intime.info.checklist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.go.mapo.intime.info.checklist.dao.ChecklistDao
import kr.go.mapo.intime.info.checklist.model.Checklist


@Database(entities = [Checklist::class], version = 1, exportSchema = false)
abstract class ChecklistDatabase: RoomDatabase()  {
    abstract fun checklistDao(): ChecklistDao

    companion object{
        private var instance: ChecklistDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ChecklistDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext, ChecklistDatabase::class.java, "db-checklist")
                    .allowMainThreadQueries().build()
            }
            return instance
        }
    }
}