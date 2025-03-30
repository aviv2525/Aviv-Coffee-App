package com.example.avivcoffee

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter(private var itemList: List<MenuItem>) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    private var isImageExpanded = false

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

        holder.itemImage.setOnClickListener {
            val layoutParams = holder.itemImage.layoutParams
            if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                layoutParams.width = 90.dpToPx()
                layoutParams.height = 90.dpToPx()
                holder.itemImage.scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                holder.itemImage.scaleType = ImageView.ScaleType.FIT_CENTER
            }
            holder.itemImage.layoutParams = layoutParams
        }

    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateList(newList: List<MenuItem>) {
        itemList = newList
        notifyDataSetChanged()
    }
}