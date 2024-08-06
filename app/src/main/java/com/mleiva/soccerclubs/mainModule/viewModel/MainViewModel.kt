package com.mleiva.soccerclubs.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mleiva.soccerclubs.data.entities.SoccerClubEntity
import com.mleiva.soccerclubs.data.repository.SoccerClubsRepository

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.mainModule.viewModel
 * Creted by: Marcelo Leiva on 21-02-2024 at 10:18
 ***/
class MainViewModel(
    private val repository: SoccerClubsRepository
): ViewModel() {

    class MainViewModelFactory(private val repository: SoccerClubsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private var clubsList: MutableList<SoccerClubEntity>

    init {
        clubsList = mutableListOf()
    }

    private val clubs: MutableLiveData<List<SoccerClubEntity>> by lazy {
        MutableLiveData<List<SoccerClubEntity>>()/*.also {  //descomentar con corrutinas
            loadClubs()
        }*/
    }

    /**
     * Agregado para cargar y refrescar datos cuando hay cambios
     */
    private val soccerClubs = repository.soccerClubs
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
        repository.getClubs {
            clubs.value = it
            clubsList = it
        }
    }

    fun deleteClub(soccerClubEntity: SoccerClubEntity){
        repository.deleteClub(soccerClubEntity) {
            val index = clubsList.indexOf(soccerClubEntity)
            if (index != -1) {
                clubsList.removeAt(index)
                clubs.value = clubsList
            }
        }
    }

    fun updateClub(soccerClubEntity: SoccerClubEntity){

        soccerClubEntity.isFavorite = !soccerClubEntity.isFavorite

        repository.updateClub(soccerClubEntity) {
            val index = clubsList.indexOf(soccerClubEntity)
            if (index != -1) {
                clubsList.set(index, soccerClubEntity)
                clubs.value = clubsList
            }
        }
    }

}