package com.kakapo.myproject.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakapo.myproject.R
import com.kakapo.myproject.adapter.LabelColorListAdapter
import kotlinx.android.synthetic.main.dialog_list.view.*

abstract class LabelColorListDialog(
        context: Context,
        private var list: ArrayList<String>,
        private val title: String = "",
        private val mSelectedColor: String = ""
) : Dialog(context){

    private var adapter: LabelColorListAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list, null)

        setContentView(view)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setupRecyclerView(view)
    }

    private fun setupRecyclerView(view: View){
        view.tv_title_dialog_chose_color.text = title
        view.rv_list_color_choser.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListAdapter(context, list, mSelectedColor)
        view.rv_list_color_choser.adapter = adapter

        adapter!!.onItemClickListener = object : LabelColorListAdapter.OnItemClickListener{
            override fun onCLick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }

        }
    }

    protected abstract fun onItemSelected(color: String)
}