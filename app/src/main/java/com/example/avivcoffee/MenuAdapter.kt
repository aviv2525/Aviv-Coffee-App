package com.example.avivcoffee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private var itemList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.itemImage.setImageResource(currentItem.imageResId)
        holder.itemName.text = currentItem.name
        holder.itemPrice.text = currentItem.price
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(newList: List<MenuItem>) {
        itemList = newList
        notifyDataSetChanged()
    }
}
