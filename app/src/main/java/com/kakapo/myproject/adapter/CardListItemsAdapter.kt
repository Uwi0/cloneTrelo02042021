package com.kakapo.myproject.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakapo.myproject.R
import com.kakapo.myproject.activity.TaskListActivity
import com.kakapo.myproject.models.Card
import com.kakapo.myproject.models.SelectedMembers
import kotlinx.android.synthetic.main.item_card.view.*

class CardListItemsAdapter(
        private val context: Context,
        private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener: OnCLickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
        )

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is ViewHolder){

            if (model.labelColor.isNotEmpty()) {
                holder.itemView.view_label_color.visibility = View.VISIBLE
                holder.itemView.view_label_color.setBackgroundColor(Color.parseColor(model.labelColor))
            } else {
                holder.itemView.view_label_color.visibility = View.GONE
            }

            holder.itemView.tv_card_name.text = model.name

            if((context as TaskListActivity).mAssignedMemberDetailList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for (i in context.mAssignedMemberDetailList.indices){
                    for (j in model.assignedTo){
                        if(context.mAssignedMemberDetailList[i].id == j){
                            val selectedMembers = SelectedMembers(
                                    context.mAssignedMemberDetailList[i].id,
                                    context.mAssignedMemberDetailList[i].image
                            )

                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }

                if (selectedMembersList.size > 0){
                    if (
                            selectedMembersList.size == 1 &&
                            selectedMembersList[0].id == model.createBy
                    ){
                        holder.itemView.rv_card_selected_members_list.visibility = View.GONE
                    }else{
                        holder.itemView.rv_card_selected_members_list.visibility = View.VISIBLE

                        holder.itemView.rv_card_selected_members_list.layoutManager =
                                GridLayoutManager(context, 4)
                        val adapter = CardMemberListItemsAdapter(context, selectedMembersList, false)

                        holder.itemView.rv_card_selected_members_list.adapter = adapter
                        adapter.setOnClickListener(object : CardMemberListItemsAdapter.OnCLickListener{
                            override fun onClick() {
                                if (onClickListener != null){
                                    onClickListener!!.onClick(position)
                                }
                            }

                        })
                    }
                }else{
                    holder.itemView.rv_card_selected_members_list.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position)
                }
            }

        }
    }

    fun setOnClickListener(onClickListener: OnCLickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnCLickListener{
        fun onClick(cardPosition: Int)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}