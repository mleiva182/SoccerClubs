package com.mleiva.soccerclubs.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mleiva.soccerclubs.data.entities.SoccerClubEntity

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.common.database
 * Creted by: Marcelo Leiva on 20-02-2024 at 14:26
 ***/
@Dao
interface SoccerClubDao {
    @Query("SELECT * FROM SoccerClubEntity")
    fun getAllClubs() : MutableList<SoccerClubEntity>

    @Query("SELECT * FROM SoccerClubEntity")
    fun getAll() : LiveData<MutableList<SoccerClubEntity>>

    @Query("SELECT * FROM SoccerClubEntity where id = :id")
    fun getClubById(id: Long): SoccerClubEntity

    @Insert
    fun addClub(soccerClubEntity: SoccerClubEntity): Long

    @Update
    fun updateClub(soccerClubEntity: SoccerClubEntity)

    @Delete
    fun deleteClub(soccerClubEntity: SoccerClubEntity)
}