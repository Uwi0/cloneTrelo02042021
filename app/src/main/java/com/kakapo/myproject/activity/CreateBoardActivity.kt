package com.kakapo.myproject.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.kakapo.myproject.R
import com.kakapo.myproject.utils.Constants
import kotlinx.android.synthetic.main.activity_create_board.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.io.IOException

class CreateBoardActivity : AppCompatActivity() {

    private var mSelectedImageUri: Uri? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)

        setupActionBar()
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
}