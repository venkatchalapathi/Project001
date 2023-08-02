package com.example.project001.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.project001.R
import com.example.project001.models.Country
import com.example.project001.models.Districts
import com.example.project001.models.States

class StateAdapter(
    val context: Context, var i: List<States>, val onclick:StateInterface
) : RecyclerView.Adapter<StateAdapter.ViewHolder>() {

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

        holder.cardName.text = i[position].stateName

        holder.cardLayout.setOnClickListener {
            onclick.onStateClick(position,i[position].districts)
        }
    }

    override fun getItemCount(): Int {
        return i.size
    }
    interface StateInterface {
        fun onStateClick(position: Int,states:List<Districts>)
    }
}