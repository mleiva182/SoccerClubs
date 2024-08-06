package com.mleiva.soccerclubs.mainModule.adapter

import com.mleiva.soccerclubs.data.entities.SoccerClubEntity

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.mainModule.adapter
 * Creted by: Marcelo Leiva on 21-02-2024 at 10:29
 ***/
interface OnClickListener {
 fun onClick(soccerClubEntity: SoccerClubEntity)
 fun onFavoriteClub(soccerClubEntity: SoccerClubEntity)
 fun onDeleteClub(soccerClubEntity: SoccerClubEntity)
}