package com.mleiva.soccerclubs.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import com.mleiva.soccerclubs.editModule.model.EditSoccerClubInteractor

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.editModule.viewModel
 * Creted by: Marcelo Leiva on 21-02-2024 at 20:02
 ***/
class EditSoccerClubViewModel: ViewModel() {

    private val soccerClubSelected = MutableLiveData<SoccerClubEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val interactor: EditSoccerClubInteractor

    init {
        interactor = EditSoccerClubInteractor()
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
        interactor.saveSoccerClub(soccerClubEntity) { newId ->
            result.value = newId
        }
    }

    fun updateSoccerClub(soccerClubEntity: SoccerClubEntity){
        interactor.updateSoccerClub(soccerClubEntity) { soccerClubUpdate ->
            result.value = soccerClubUpdate
        }
    }

}