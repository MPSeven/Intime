package kr.go.mapo.intime.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.go.mapo.intime.dao.BookmarkAedDao
import kr.go.mapo.intime.model.Aed

@Database(entities = [Aed::class], version = 1)
abstract class AedDatabase : RoomDatabase() {

    abstract fun bookmarkAedDao(): BookmarkAedDao

}