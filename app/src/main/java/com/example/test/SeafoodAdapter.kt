package com.example.test

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.dashboardbuyer.MakeOrderActivity
import com.squareup.picasso.Picasso

class SeafoodAdapter(private val seafoodItems: List<SeafoodItem>) :
    RecyclerView.Adapter<SeafoodAdapter.SeafoodViewHolder>() {

    class SeafoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewSeafood)
        val textViewVendorName: TextView = itemView.findViewById(R.id.textViewVendorName)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val fishTypeTextView: TextView = itemView.findViewById(R.id.fishTypeTextView)
        val availabilityTextView: TextView = itemView.findViewById(R.id.availabilityTextView)
        // Initialize other TextViews here
        // ...
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeafoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seafood, parent, false)
        return SeafoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeafoodViewHolder, position: Int) {
        val seafoodItem = seafoodItems[position]

        // Bind data to views
        holder.textViewVendorName.text = "Vendor Name: ${seafoodItem.vendorName}"
        holder.locationTextView.text = "Location: ${seafoodItem.location}"
        holder.priceTextView.text = "Price/Kg: ${seafoodItem.price.toString()}" // Convert Double to String
        holder.fishTypeTextView.text = "Type: ${seafoodItem.fishType}"
        holder.availabilityTextView.text = "Availability: ${seafoodItem.availability}kg"
        // Set other details to respective TextViews
        // ...

        // Load image using a library like Picasso or Glide
        // Example using Picasso:
        Picasso.get().load(seafoodItem.imageUrl).into(holder.imageView)

        holder.itemView.findViewById<View>(R.id.makeOrderButton).setOnClickListener {
            val context = holder.itemView.context
            // Start MakeOrderActivity and pass the selected item's details
            val intent = Intent(context, MakeOrderActivity::class.java)
            intent.putExtra("SELECTED_ITEM", seafoodItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return seafoodItems.size
    }
}
