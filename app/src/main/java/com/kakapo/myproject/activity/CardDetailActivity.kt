package com.kakapo.myproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.kakapo.myproject.R
import com.kakapo.myproject.models.Board
import com.kakapo.myproject.utils.Constants
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : AppCompatActivity() {

    private lateinit var mBoarDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        getIntentData()
        setupActionBar()

        et_name_card_details.setText(mBoarDetails
                .taskList[mTaskListPosition]
                .cards[mCardPosition]
                .name
        )

        et_name_card_details.setSelection(et_name_card_details.text.toString().length)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_card_details_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_ios_24)
            actionBar.title = mBoarDetails
                    .taskList[mTaskListPosition]
                    .cards[mCardPosition]
                    .name
        }

        toolbar_card_details_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun getIntentData(){
        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoarDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }
        if(intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION, -1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION, -1)
        }
    }
}