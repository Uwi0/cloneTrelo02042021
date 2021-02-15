package com.kakapo.myproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kakapo.myproject.R
import com.kakapo.myproject.models.SelectedMembers
import kotlinx.android.synthetic.main.item_card_selected_member.view.*

class CardMemberListItemsAdapter(
        private val context: Context,
        private val list: ArrayList<SelectedMembers>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnCLickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(
                R.layout.item_card_selected_member,
                parent,
                false
        )

        return MyViewHolder(layout)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            if (position == list.size -1){
                holder.itemView.iv_add_member.visibility = View.VISIBLE
                holder.itemView.iv_selected_member_image.visibility = View.GONE
            }else{
                holder.itemView.iv_add_member.visibility = View.GONE
                holder.itemView.iv_selected_member_image.visibility = View.VISIBLE

                Glide.with(context)
                        .load(model.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(holder.itemView.iv_selected_member_image)
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick()
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnCLickListener){
        this.onClickListener = onClickListener
    }

    interface OnCLickListener{
        fun onClick()
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}