package kr.go.mapo.intime

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.go.mapo.intime.dao.AedRoomDao
import kr.go.mapo.intime.model.AedRoom

@Database(entities = [AedRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun aedRoomDao(): AedRoomDao
}