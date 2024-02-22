package com.mleiva.soccerclubs.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.common.entities
 * Creted by: Marcelo Leiva on 20-02-2024 at 14:28
 ***/
@Entity(tableName = "SoccerClubEntity")
data class SoccerClubEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var name: String,
    var phone: String = "",
    var website: String = "",
    var photoUrl: String = "",
    var isFavorite: Boolean = false
){

    constructor() : this(name = "", phone = "", photoUrl = "")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoccerClubEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
