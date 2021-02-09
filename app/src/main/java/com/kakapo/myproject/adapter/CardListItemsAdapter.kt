package com.kakapo.myproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kakapo.myproject.R
import com.kakapo.myproject.models.Card
import kotlinx.android.synthetic.main.item_card.view.*

class CardListItemsAdapter(
        private val context: Context,
        private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


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

            holder.itemView.tv_card_name.text = model.name

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface SetOnCLickListener{
        fun onClick(position: Int, card: Card)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}