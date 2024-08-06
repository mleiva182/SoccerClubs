package com.mleiva.soccerclubs.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mleiva.soccerclubs.SoccerClubApplication
import com.mleiva.soccerclubs.data.entities.SoccerClubEntity
import java.util.concurrent.LinkedBlockingQueue

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.data.repository
 * Creted by: Marcelo Leiva on 06-08-2024 at 16:35
 ***/
class SoccerClubsRepository {

    fun getClubs(callback: (MutableList<SoccerClubEntity>) -> Unit){//unit no devuelve nada de forma externa
        val queue = LinkedBlockingQueue<MutableList<SoccerClubEntity>>()
        Thread{
            val clubsList = SoccerClubApplication.database.soccerClubDao().getAllClubs()
            queue.add(clubsList)
        }.start()

        callback(queue.take())
    }

    /**
     * Agregado para cargar y refrescar datos cuando hay cambios
     */
    val soccerClubs: LiveData<MutableList<SoccerClubEntity>> = liveData {
        val soccerClubsLiveData = SoccerClubApplication.database.soccerClubDao().getAll()
        emitSource(soccerClubsLiveData.map {soccerClubs ->
            soccerClubs.sortedBy { it.name }.toMutableList()
        })
    }

    fun deleteClub(soccerClubEntity: SoccerClubEntity, callback: (SoccerClubEntity) -> Unit){
        val queue = LinkedBlockingQueue<SoccerClubEntity>()
        Thread {
            SoccerClubApplication.database.soccerClubDao().deleteClub(soccerClubEntity)
            queue.add(soccerClubEntity)
        }.start()
        callback(queue.take())
    }

    fun updateClub(soccerClubEntity: SoccerClubEntity, callback: (SoccerClubEntity) -> Unit){
        val queue = LinkedBlockingQueue<SoccerClubEntity>()
        Thread{
            SoccerClubApplication.database.soccerClubDao().updateClub(soccerClubEntity)
            queue.add(soccerClubEntity)
        }.start()
        callback(queue.take())
    }

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