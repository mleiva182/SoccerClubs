package com.mleiva.soccerclubs.mainModule.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mleiva.soccerclubs.R
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import com.mleiva.soccerclubs.databinding.ItemClubBinding

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.mainModule.adapter
 * Creted by: Marcelo Leiva on 21-02-2024 at 10:32
 ***/
class SoccerClubAdapter(private var clubs: MutableList<SoccerClubEntity>, private var listener: OnClickListener) :
    RecyclerView.Adapter<SoccerClubAdapter.ViewHolder>() {

    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_club, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val club = clubs.get(position)

        with(holder){
            setListener(club)

            binding.tvName.text = club.name
            binding.cbFavorite.isChecked = club.isFavorite

            Glide.with(mContext)
                .load(club.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int = clubs.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemClubBinding.bind(view)

        fun setListener(soccerClubEntity: SoccerClubEntity){
            with(binding.root) {
                setOnClickListener { listener.onClick(soccerClubEntity) }
                setOnLongClickListener {
                    listener.onDeleteClub(soccerClubEntity)
                    true
                }
            }

            binding.cbFavorite.setOnClickListener{
                listener.onFavoriteClub(soccerClubEntity)
            }
        }
    }

    fun setClubs(clubs: List<SoccerClubEntity>) {
        this.clubs = clubs as MutableList<SoccerClubEntity>
        notifyDataSetChanged()
    }

    fun add(soccerClubEntity: SoccerClubEntity) {

        if (soccerClubEntity.id != 0L) {
            if (!clubs.contains(soccerClubEntity)) {
                clubs.add(soccerClubEntity)
                notifyItemInserted(clubs.size-1)
            }else{
                update(soccerClubEntity)
            }
        }
    }

    private fun update(soccerClubEntity: SoccerClubEntity) {
        val index = clubs.indexOf(soccerClubEntity)
        if (index != -1){
            clubs.set(index, soccerClubEntity)
            notifyItemChanged(index)
        }
    }

}