package com.mleiva.soccerclubs.mainModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mleiva.soccerclubs.SoccerClubApplication
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import java.util.concurrent.LinkedBlockingQueue

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.mainModule.model
 * Creted by: Marcelo Leiva on 21-02-2024 at 10:19
 ***/
class MainInteractor {

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

}