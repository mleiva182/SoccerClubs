package com.mleiva.soccerclubs.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import com.mleiva.soccerclubs.mainModule.model.MainInteractor

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.mainModule.viewModel
 * Creted by: Marcelo Leiva on 21-02-2024 at 10:18
 ***/
class MainViewModel: ViewModel() {

    private var clubsList: MutableList<SoccerClubEntity>
    private var interactor: MainInteractor

    init {
        clubsList = mutableListOf()
        interactor = MainInteractor()
    }

    private val clubs: MutableLiveData<List<SoccerClubEntity>> by lazy {
        MutableLiveData<List<SoccerClubEntity>>()/*.also {  //descomentar con corrutinas
            loadClubs()
        }*/
    }

    /**
     * Agregado para cargar y refrescar datos cuando hay cambios
     */
    private val soccerClubs = interactor.soccerClubs
    fun getSoccerClubs(): LiveData<MutableList<SoccerClubEntity>>{
        return soccerClubs//.also {
            //    loadClubs()
        //}
    }
    fun getClubs(): LiveData<List<SoccerClubEntity>> {
        return clubs.also {
            loadClubs()
        }
    }

    private fun loadClubs(){
        interactor.getClubs {
            clubs.value = it
            clubsList = it
        }
    }

    fun deleteClub(soccerClubEntity: SoccerClubEntity){
        interactor.deleteClub(soccerClubEntity) {
            val index = clubsList.indexOf(soccerClubEntity)
            if (index != -1) {
                clubsList.removeAt(index)
                clubs.value = clubsList
            }
        }
    }

    fun updateClub(soccerClubEntity: SoccerClubEntity){

        soccerClubEntity.isFavorite = !soccerClubEntity.isFavorite

        interactor.updateClub(soccerClubEntity) {
            val index = clubsList.indexOf(soccerClubEntity)
            if (index != -1) {
                clubsList.set(index, soccerClubEntity)
                clubs.value = clubsList
            }
        }
    }

}