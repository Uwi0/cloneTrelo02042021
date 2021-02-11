package com.kakapo.myproject.activity

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.kakapo.myproject.R
import com.kakapo.myproject.dialog.LabelColorListDialog
import com.kakapo.myproject.firebase.FireStoreClass
import com.kakapo.myproject.models.Board
import com.kakapo.myproject.models.Card
import com.kakapo.myproject.models.Task
import com.kakapo.myproject.utils.Constants
import kotlinx.android.synthetic.main.activity_card_detail.*

class CardDetailActivity : BaseActivity() {

    private lateinit var mBoarDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private var mSelectedColor = ""

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
        setupSelectedLabelColorClickListener()
        setupColorWhenCreated()
        setButtonUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(
                        mBoarDetails.taskList[mTaskListPosition].cards[mCardPosition].name
                )

                return true
            }
        }

        return super.onOptionsItemSelected(item)
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

    private fun setButtonUpdate(){
        btn_update_card_details.setOnClickListener{

            val nameDetail = et_name_card_details.text.toString()

            if(nameDetail.isNotEmpty()){
                updateCardDetails()
            }else{
            Toast.makeText(
                    this@CardDetailActivity,
                    "Please enter card name",
                    Toast.LENGTH_SHORT
            ).show()
            }
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

    private fun updateCardDetails(){
        val card = Card(
                et_name_card_details.text.toString(),
                mBoarDetails.taskList[mTaskListPosition].cards[mCardPosition].createBy,
                mBoarDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
                mSelectedColor
        )

        mBoarDetails.taskList[mTaskListPosition].cards[mCardPosition] = card

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this@CardDetailActivity, mBoarDetails)
    }

    private fun deleteCard(){
        val cardList: ArrayList<Card> = mBoarDetails.taskList[mTaskListPosition].cards

        cardList.removeAt(mCardPosition)

        val taskList: ArrayList<Task> = mBoarDetails.taskList
        taskList.removeAt(taskList.size -1)

        taskList[mTaskListPosition].cards = cardList
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this@CardDetailActivity, mBoarDetails)

    }

    private fun alertDialogForDeleteCard(cardName: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.alert))
        builder.setMessage(
                resources.getString(R.string.confirmation_message_to_delete_card, cardName)
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog, _ ->
            dialog.dismiss()
            deleteCard()
        }
        builder.setNegativeButton(resources.getString(R.string.no)){dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun colorList() : ArrayList<String>{
        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")

        return colorsList
    }

    private fun setColor(){
        tv_select_label_color.text = ""
        tv_select_label_color.setBackgroundColor(Color.parseColor(mSelectedColor))
    }

    private fun labelColorListDialog(){
        val colorList: ArrayList<String> = colorList()
        val listDialog = object : LabelColorListDialog(
                this@CardDetailActivity,
                colorList,
                resources.getString(R.string.str_select_label_color),
                mSelectedColor
        ){
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }

        }

        listDialog.show()
    }

    private fun setupSelectedLabelColorClickListener(){
        tv_select_label_color.setOnClickListener {
            labelColorListDialog()
        }
    }

    private fun setupColorWhenCreated(){
        mSelectedColor = mBoarDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor
        if (mSelectedColor.isNotEmpty()) {
            setColor()
        }
    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }
}