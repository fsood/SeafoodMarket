package com.example.test.dashboardseller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.squareup.picasso.Picasso

class OrderManagementAdapter(private val orders: List<OrderManagement>) : RecyclerView.Adapter<OrderManagementAdapter.OrderViewHolder>() {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind order data to views
        fun bind(order: OrderManagement) {

            // Bind order data to views in the item layout
            val totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
            totalPriceTextView.text = "Total Price: ${order.totalPrice}"

            val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
            quantityTextView.text = "Quantity: ${order.kgQuantity}kg"

            val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
            typeTextView.text = "Type: ${order.fishType}"

            val deliveryInfoTextView: TextView = itemView.findViewById(R.id.deliveryInfoTextView)
             val deliveryInfo = "Name: ${order.deliveryName}\nLocation: ${order.deliveryLocation}\nPhone: ${order.deliveryPhone}"
             deliveryInfoTextView.text = deliveryInfo

            val paymentStatusTextView: TextView = itemView.findViewById(R.id.paymentStatusTextView)
            paymentStatusTextView.text = "Payment Status: Pending"

            val itemImageView: ImageView = itemView.findViewById(R.id.itemimageorder)
            order.imageUrl?.let { imageUrl ->
                Picasso.get().load(imageUrl).into(itemImageView)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
