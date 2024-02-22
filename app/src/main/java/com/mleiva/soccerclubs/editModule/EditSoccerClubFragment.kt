package com.mleiva.soccerclubs.editModule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.mleiva.soccerclubs.R
import com.mleiva.soccerclubs.common.entities.SoccerClubEntity
import com.mleiva.soccerclubs.databinding.FragmentEditSoccerClubBinding
import com.mleiva.soccerclubs.editModule.viewModel.EditSoccerClubViewModel
import com.mleiva.soccerclubs.mainModule.MainActivity

/***
 * Project: SoccerClubs
 * From: com.mleiva.soccerclubs.editModule
 * Creted by: Marcelo Leiva on 21-02-2024 at 19:58
 ***/
class EditSoccerClubFragment : Fragment(){

    private lateinit var mBinding: FragmentEditSoccerClubBinding
    private lateinit var mEditSoccerClubViewModel: EditSoccerClubViewModel

    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private var mSoccerClubEntity: SoccerClubEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mEditSoccerClubViewModel = ViewModelProvider(requireActivity()).get(EditSoccerClubViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {
        mBinding = FragmentEditSoccerClubBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        setupTextFields()
    }

    private fun setupViewModel() {

        mEditSoccerClubViewModel.getSoccerClubSelected().observe(viewLifecycleOwner,{
            mSoccerClubEntity = it
            if (it.id != 0L){
                mIsEditMode = true
                setUiSoccerClub(it)
            } else {
                mIsEditMode = false
            }

            setupActionBar()
        })

        mEditSoccerClubViewModel.getResult().observe(viewLifecycleOwner,{result ->
            hideKeyboard()

            when(result){
                is Long ->{

                    mSoccerClubEntity!!.id = result
                    mEditSoccerClubViewModel.setSoccerClubSelected(mSoccerClubEntity!!)

                    Toast.makeText(mActivity,
                        R.string.edit_soccer_club_message_save_success, Toast.LENGTH_SHORT).show()

                    /*val resultIntent = Intent()
                    resultIntent.putExtra("key", "value")  // Puedes poner cualquier dato que desees pasar de vuelta

                    requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                    requireActivity().supportFragmentManager.popBackStack()*/
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                is SoccerClubEntity ->{
                    Snackbar.make(mBinding.root,
                        R.string.edit_soccer_club_message_update_success,
                        Snackbar.LENGTH_SHORT).show()
                }
            }

        })

    }

    private fun setupActionBar() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mActivity?.supportActionBar?.title = if (mIsEditMode) getString(R.string.edit_soccer_club_title_edit)
        else getString(R.string.edit_soccer_club_title_add)

        setHasOptionsMenu(true)
    }

    private fun setupTextFields() {
        with(mBinding) {
            etName.addTextChangedListener { validateFields(tilName) }
            etPhone.addTextChangedListener { validateFields(tilPhone) }
            etPhotoUrl.addTextChangedListener {
                validateFields(tilPhotoUrl)
                loadImage(it.toString().trim())
            }
        }
    }

    private fun loadImage(url: String){
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.imgPhoto)
    }

    private fun setUiSoccerClub(soccerClubEntity: SoccerClubEntity) {
        with(mBinding){
            etName.text = soccerClubEntity.name.editable()
            etPhone.text = soccerClubEntity.phone.editable()
            etWebsite.text = soccerClubEntity.website.editable()
            etPhotoUrl.text = soccerClubEntity.photoUrl.editable()
        }
    }

    private fun String.editable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(R.string.dialog_exit_title)
                    .setMessage(R.string.dialog_exit_message)
                    .setPositiveButton(R.string.dialog_exit_ok){ _, _->
                        if (isEnabled) {
                            isEnabled = false
                            /*val resultIntent = Intent()
                            resultIntent.putExtra("key", "value")  // Puedes poner cualquier dato que desees pasar de vuelta

                            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                            requireActivity().supportFragmentManager.popBackStack()*/
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    }
                    .setNegativeButton(R.string.dialog_delete_cancel, null)
                    .show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                /*val resultIntent = Intent()
                resultIntent.putExtra("key", "value")  // Puedes poner cualquier dato que desees pasar de vuelta

                requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                requireActivity().supportFragmentManager.popBackStack()*/
                requireActivity().onBackPressedDispatcher.onBackPressed()
                true
            }
            R.id.action_save -> {
                if (mSoccerClubEntity != null &&
                    validateFields(mBinding.tilPhotoUrl, mBinding.tilPhone, mBinding.tilName)){
                    with(mSoccerClubEntity!!) {
                        name = mBinding.etName.text.toString().trim()
                        phone = mBinding.etPhone.text.toString().trim()
                        website = mBinding.etWebsite.text.toString().trim()
                        photoUrl = mBinding.etPhotoUrl.text.toString().trim()
                    }

                    if (mIsEditMode) mEditSoccerClubViewModel.updateSoccerClub(mSoccerClubEntity!!)
                    else mEditSoccerClubViewModel.saveSoccerClub(mSoccerClubEntity!!)

                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun validateFields(vararg textFields: TextInputLayout): Boolean{
        var isValid = true

        for (textField in textFields){
            if (textField.editText?.text.toString().trim().isEmpty()){
                textField.error = getString(R.string.helper_required)
                isValid = false
            } else textField.error = null
        }

        if (!isValid) Snackbar.make(mBinding.root,
            R.string.edit_soccer_club_message_valid,
            Snackbar.LENGTH_SHORT).show()

        return isValid
    }

    private fun hideKeyboard(){
        val imm = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        // FIXME: ViewModel
        mEditSoccerClubViewModel.setShowFab(true)
        mEditSoccerClubViewModel.setResult(Any())
        //mActivity?.hideFab(true)

        setHasOptionsMenu(false)
        super.onDestroy()
    }

}