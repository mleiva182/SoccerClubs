package com.mleiva.soccerclubs.editModule.model

import com.mleiva.soccerclubs.SoccerClubApplication
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import java.util.concurrent.LinkedBlockingQueue

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.editModule.model
 * Creted by: Marcelo Leiva on 21-02-2024 at 20:00
 ***/
class EditSoccerClubInteractor {

    fun saveSoccerClub(soccerClubEntity: SoccerClubEntity, callback: (Long) -> Unit){
        val queue = LinkedBlockingQueue<Long>()
        Thread {
            val newId = SoccerClubApplication.database.soccerClubDao().addClub(soccerClubEntity)
            SoccerClubApplication.database.soccerClubDao().deleteClub(soccerClubEntity)
            queue.add(newId)
        }.start()
        callback(queue.take())
    }

    fun updateSoccerClub(soccerClubEntity: SoccerClubEntity, callback: (SoccerClubEntity) -> Unit){
        val queue = LinkedBlockingQueue<SoccerClubEntity>()
        Thread {
            SoccerClubApplication.database.soccerClubDao().updateClub(soccerClubEntity)
            queue.add(soccerClubEntity)
        }.start()
        callback(queue.take())
    }

}