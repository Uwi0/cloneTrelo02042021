package com.kakapo.myproject.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kakapo.myproject.R
import com.kakapo.myproject.firebase.FireStoreClass
import com.kakapo.myproject.models.Board
import com.kakapo.myproject.utils.Constants
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class CreateBoardActivity : BaseActivity() {

    private var mSelectedImageUri: Uri? = null
    private lateinit var mUsername: String
    private var mBoarImageUrl: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        setupActionBar()
        setupBtnCreateBoard()
        getInformationExtraFormMain()
        setupImageFromStorage()
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_create_board_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_ios_24)
            actionBar.title = resources.getString(R.string.create_board_title)
        }

        toolbar_create_board_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this@CreateBoardActivity)
            }else{
                Toast.makeText(
                        this@CreateBoardActivity,
                        "looks like you just denied the permissions," +
                                " you can change in android setting UwU",
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(
                resultCode == Activity.RESULT_OK &&
                requestCode == Constants.PICK_IMAGE_REQUEST_CODE &&
                data!!.data != null
        ){
            mSelectedImageUri = data.data

            try{
                Glide
                        .with(this@CreateBoardActivity)
                        .load(mSelectedImageUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_board_place_holder)
                        .into(iv_board_image)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun setupImageFromStorage(){
        iv_board_image.setOnClickListener {

            if(ContextCompat.checkSelfPermission(
                            this@CreateBoardActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
            ){

                Constants.showImageChooser(this@CreateBoardActivity)

            }else{
                ActivityCompat.requestPermissions(
                        this@CreateBoardActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE
                )
            }

        }
    }

    private fun getInformationExtraFormMain(){
        if (intent.hasExtra(Constants.NAME)){
            mUsername = intent.getStringExtra(Constants.NAME).toString()
        }
    }

    private fun createBoard(){
        val assignedUserArrayList: ArrayList<String> = ArrayList()
        assignedUserArrayList.add(getCurrentUserId())
        val name = et_board_name.text.toString()

        val board = Board(
                name,
                mBoarImageUrl,
                mUsername,
                assignedUserArrayList
        )

        FireStoreClass().createBoard(this, board)
    }

    private fun uploadBoardImage(){
        showProgressDialog(resources.getString(R.string.please_wait))

        val storageReference: StorageReference = FirebaseStorage
                .getInstance()
                .reference
                .child(
                        "BOARD_IMAGE" +
                                "${System.currentTimeMillis()}" +
                                ".${Constants.getFileExtensions(
                                        this@CreateBoardActivity,
                                        mSelectedImageUri
                                )}"
                )

        storageReference.putFile(mSelectedImageUri!!).addOnSuccessListener { taskSnapshot ->
            Log.i(
                    "Board Image Url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.i("Downloadable Image Uri", uri.toString())
                mBoarImageUrl = uri.toString()

                createBoard()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                    this@CreateBoardActivity,
                    exception.message,
                    Toast.LENGTH_SHORT
            ).show()

            hideProgressDialog()
        }

    }

    private fun setupBtnCreateBoard(){
        btn_create.setOnClickListener {
            if(mSelectedImageUri != null){
                uploadBoardImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))
                createBoard()
            }
        }
    }

    fun boardCreateSuccessFully(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}