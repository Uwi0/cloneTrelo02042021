package com.kakapo.myproject.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakapo.myproject.R
import com.kakapo.myproject.adapter.MemberListItemsAdapter
import com.kakapo.myproject.models.User
import kotlinx.android.synthetic.main.activity_sign_in.view.*
import kotlinx.android.synthetic.main.dialog_list.view.*
import kotlinx.android.synthetic.main.item_task.view.*

abstract class MembersListDialog(
        context: Context,
        private var list: ArrayList<User>,
        private val title: String = ""
) : Dialog(context) {

    private var adapter: MemberListItemsAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View){
        view.tv_task_list_title.text = title

        if(list.size > 0){

            view.rv_list_dialog.layoutManager = LinearLayoutManager(context)
            adapter = MemberListItemsAdapter(context, list)
            view.rv_list_dialog.adapter = adapter

            adapter!!.setOnclickListener(object : MemberListItemsAdapter.OnclickListener{
                override fun onClick(position: Int, user: User, action: String) {
                    dismiss()
                    onItemSelected(user, action)
                }

            })
        }
    }

    protected abstract fun onItemSelected(user: User, action: String)

}