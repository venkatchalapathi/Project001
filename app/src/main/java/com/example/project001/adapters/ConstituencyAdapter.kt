package com.example.project001.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.R
import com.example.project001.models.Districts

class ConstituencyAdapter(
    val context: Context, var i: List<String>, val onclick:ConstituencySelectInterface
) : RecyclerView.Adapter<ConstituencyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var cardName = itemView.findViewById<TextView>(R.id.catName)
        var cardLayout = itemView.findViewById<ConstraintLayout>(R.id.cardLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.category_dialog_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cardName.text = i[position]

        holder.cardLayout.setOnClickListener {
            onclick.onConstituencyClick(i[position])
        }
    }

    override fun getItemCount(): Int {
        return i.size
    }
    interface ConstituencySelectInterface {
        fun onConstituencyClick(states:String)
    }
}