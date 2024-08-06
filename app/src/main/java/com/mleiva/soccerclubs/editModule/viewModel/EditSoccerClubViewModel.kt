package com.mleiva.soccerclubs.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mleiva.soccerclubs.data.entities.SoccerClubEntity
import com.mleiva.soccerclubs.data.repository.SoccerClubsRepository

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.editModule.viewModel
 * Creted by: Marcelo Leiva on 21-02-2024 at 20:02
 ***/
class EditSoccerClubViewModel(
    private val repository: SoccerClubsRepository
): ViewModel() {

    private val soccerClubSelected = MutableLiveData<SoccerClubEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    class EditViewModelFactory(private val repository: SoccerClubsRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EditSoccerClubViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EditSoccerClubViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    init {

    }

    fun setSoccerClubSelected(soccerClubEntity: SoccerClubEntity){
        soccerClubSelected.value = soccerClubEntity
    }

    fun getSoccerClubSelected(): LiveData<SoccerClubEntity> {
        return soccerClubSelected
    }

    fun setShowFab(boolean: Boolean){
        showFab.value = boolean
    }

    fun getShowFab(): LiveData<Boolean> {
        return showFab
    }

    fun setResult(any: Any){
        result.value = any
    }

    fun getResult(): LiveData<Any> {
        return result
    }

    fun saveSoccerClub(soccerClubEntity: SoccerClubEntity){
        repository.saveSoccerClub(soccerClubEntity) { newId ->
            result.value = newId
        }
    }

    fun updateSoccerClub(soccerClubEntity: SoccerClubEntity){
        repository.updateSoccerClub(soccerClubEntity) { soccerClubUpdate ->
            result.value = soccerClubUpdate
        }
    }

}