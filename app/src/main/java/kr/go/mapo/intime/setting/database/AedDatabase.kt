package kr.go.mapo.intime.setting.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.go.mapo.intime.map.model.Aed
import kr.go.mapo.intime.map.model.Shelter
import kr.go.mapo.intime.setting.dao.BookmarkAedDao
import kr.go.mapo.intime.setting.dao.BookmarkShelterDao

@Database(entities = [Aed::class, Shelter::class], version = 1)
abstract class AedDatabase : RoomDatabase() {

    abstract fun bookmarkAedDao(): BookmarkAedDao

    abstract fun bookmarkShelterDao(): BookmarkShelterDao

}