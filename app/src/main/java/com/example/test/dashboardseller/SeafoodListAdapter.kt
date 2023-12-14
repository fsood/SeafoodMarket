package com.example.test.dashboardseller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodItem
import com.squareup.picasso.Picasso

class SeafoodListAdapter(private val seafoodList: List<SeafoodItem>) :
    RecyclerView.Adapter<SeafoodListAdapter.SeafoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeafoodViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seafoodseller, parent, false)
        return SeafoodViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SeafoodViewHolder, position: Int) {
        val currentItem = seafoodList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return seafoodList.size
    }

    inner class SeafoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vendorNameTextView: TextView = itemView.findViewById(R.id.textViewVendorName)
        private val fishTypeTextView: TextView = itemView.findViewById(R.id.textViewFishType)
        private val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        private val availabilityTextView: TextView = itemView.findViewById(R.id.textViewAvailability)
        private val seafoodImageView: ImageView = itemView.findViewById(R.id.imageViewSeafood)

        fun bind(seafoodItem: SeafoodItem) {
            vendorNameTextView.text = "Vendor Name: ${seafoodItem.vendorName}"
            fishTypeTextView.text = "Type: ${seafoodItem.fishType}"
            priceTextView.text = "Price/Kg: ${seafoodItem.price.toString()}"
            availabilityTextView.text = "Availability: ${seafoodItem.availability}"

            // Load image using Picasso
            Picasso.get().load(seafoodItem.imageUrl).into(seafoodImageView)
        }
    }
}

