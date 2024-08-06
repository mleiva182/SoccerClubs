package com.mleiva.soccerclubs.mainModule

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mleiva.soccerclubs.R
import com.mleiva.soccerclubs.data.entities.SoccerClubEntity
import com.mleiva.soccerclubs.data.repository.SoccerClubsRepository
import com.mleiva.soccerclubs.databinding.ActivityMainBinding
import com.mleiva.soccerclubs.editModule.EditSoccerClubFragment
import com.mleiva.soccerclubs.editModule.viewModel.EditSoccerClubViewModel
import com.mleiva.soccerclubs.mainModule.adapter.OnClickListener
import com.mleiva.soccerclubs.mainModule.adapter.SoccerClubAdapter
import com.mleiva.soccerclubs.mainModule.viewModel.MainViewModel

class MainActivity : AppCompatActivity(), OnClickListener{

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: SoccerClubAdapter
    private lateinit var mGridLayout: GridLayoutManager
    //MVVM
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mEditSoccerClubViewModel: EditSoccerClubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener { launchEditFragment() }

        setupViewModel()
        setupRecyclerView()

    }

    override fun onStart() {
        super.onStart()
        Log.d("Main", "onStart")
    }
    override fun onResume() {
        super.onResume()
        Log.d("Main", "onResume")

        if (mMainViewModel != null)  {
            mMainViewModel.getClubs().observe(this) { clubs ->
                mAdapter.setClubs(clubs)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d("Main", "onPause")
    }


    private fun setupViewModel() {
        val repository = SoccerClubsRepository()
        val mainViewModelFactory = MainViewModel.MainViewModelFactory(repository)
        mMainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        mMainViewModel.getSoccerClubs().observe(this) { clubs ->
            mAdapter.setClubs(clubs)
        }

        val editViewModelFactory = EditSoccerClubViewModel.EditViewModelFactory(repository)
        mEditSoccerClubViewModel = ViewModelProvider(this, editViewModelFactory).get(EditSoccerClubViewModel::class.java)
        mEditSoccerClubViewModel.getShowFab().observe(this, {isVisible ->
            if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
        })

        mEditSoccerClubViewModel.getSoccerClubSelected().observe(this, {soccerClubEntity ->
            mAdapter.add(soccerClubEntity)
        })
    }

    private fun launchEditFragment(soccerClubEntity: SoccerClubEntity = SoccerClubEntity()) {

        mEditSoccerClubViewModel.setShowFab(false)
        mEditSoccerClubViewModel.setSoccerClubSelected(soccerClubEntity)

        val fragment = EditSoccerClubFragment()
        val resultKey = "your_result_key"
        val requestCode = 1  // Puedes usar cualquier número único para identificar la solicitud

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        //fragmentTransaction.replace(R.id.containerMain, fragment)
        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun setupRecyclerView() {
        mAdapter = SoccerClubAdapter(mutableListOf(), this)
        mGridLayout = GridLayoutManager(this, resources.getInteger(R.integer.main_columns))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    override fun onClick(soccerClubEntity: SoccerClubEntity) {
        launchEditFragment(soccerClubEntity)
    }

    override fun onFavoriteClub(soccerClubEntity: SoccerClubEntity) {
        mMainViewModel.updateClub(soccerClubEntity)
    }

    override fun onDeleteClub(soccerClubEntity: SoccerClubEntity) {
        val items = resources.getStringArray(R.array.array_options_item)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_options_title)
            .setItems(items) { _, i ->
                when(i){
                    0 -> confirmDelete(soccerClubEntity)

                    1 -> dial(soccerClubEntity.phone)

                    2 -> goToWebsite(soccerClubEntity.website)
                }
            }
            .show()
    }

    private fun confirmDelete(soccerClubEntity: SoccerClubEntity){
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_title)
            .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                //delete
                mMainViewModel.deleteClub(soccerClubEntity)
            }
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }

    private fun dial(phone: String){
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }

        startIntent(callIntent)
    }

    private fun goToWebsite(website: String){
        if (website.isEmpty()){
            Toast.makeText(this, R.string.main_error_no_website, Toast.LENGTH_LONG).show()
        } else {
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }

            startIntent(websiteIntent)
        }
    }

    private fun startIntent(intent: Intent){
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this, R.string.main_error_no_resolve, Toast.LENGTH_LONG).show()
    }

}