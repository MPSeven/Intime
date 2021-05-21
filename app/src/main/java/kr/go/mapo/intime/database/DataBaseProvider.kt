package kr.go.mapo.intime.database

import android.content.Context
import androidx.room.Room

object DataBaseProvider {

    private const val DB_NAME = "intime_repository_app.db"

    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        AedDatabase::class.java, DB_NAME
    ).build()

}