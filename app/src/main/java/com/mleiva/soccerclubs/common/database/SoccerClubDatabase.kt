package com.mleiva.soccerclubs.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.common.database
 * Creted by: Marcelo Leiva on 20-02-2024 at 14:25
 ***/
@Database(entities = arrayOf(SoccerClubEntity::class), version = 1)
abstract class SoccerClubDatabase : RoomDatabase() {
    abstract fun soccerClubDao() : SoccerClubDao
}