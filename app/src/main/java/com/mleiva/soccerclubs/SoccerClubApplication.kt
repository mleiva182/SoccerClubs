package com.mleiva.soccerclubs

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mleiva.soccerclubs.common.database.SoccerClubDatabase

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs
 * Creted by: Marcelo Leiva on 20-02-2024 at 14:21
 ***/
class SoccerClubApplication : Application() {

    companion object{
        lateinit var database: SoccerClubDatabase
    }
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this,
            SoccerClubDatabase::class.java,
            "SoccerClubDatabase")
            .build()
    }

}