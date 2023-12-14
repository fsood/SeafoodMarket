package com.example.test.dashboardbuyer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.squareup.picasso.Picasso

class OrderAdapter(
    private val orders: List<OrderStatus>,
    private val cancelListener: (OrderStatus) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {



    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        init {
            val cancelButton: Button = itemView.findViewById(R.id.cancelButton)
            cancelButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val order = orders[position]
                    cancelListener.invoke(order)
                }
            }

            val payorder: Button = itemView.findViewById(R.id.payOrderButton)
            payorder.setOnClickListener {
                val intent = Intent(itemView.context, MpesaPaybill::class.java)
                itemView.context.startActivity(intent)
            }
        }


        // Bind order data to views
        fun bind(order: OrderStatus) {
            // Bind order data to views in the item layout
            val totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
            totalPriceTextView.text = "Total Price: ${order.totalPrice}"

            val quantityTextView: TextView = itemView.findViewById(R.id.quantityTextView)
            quantityTextView.text = "Quantity: ${order.kgQuantity}kg"

            val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
            typeTextView.text = "Type: ${order.fishType}"

            val paymentStatusTextView: TextView = itemView.findViewById(R.id.paymentStatusTextView)
            paymentStatusTextView.text = "Payment Status: Pending"

            val sellersdeliveryInfoTextView = itemView.findViewById<TextView>(R.id.sellersdeliveryInfoTextView)
            val sellerspaymentStatusTextView = itemView.findViewById<TextView>(R.id.sellerspaymentStatusTextView)
            val numberinfo = itemView.findViewById<TextView>(R.id.sellerphonenumberinfotextview)
            val seafoodImageView = itemView.findViewById<ImageView>(R.id.itemimage)

            sellersdeliveryInfoTextView.text = "Delivery Information: ${order.deliveryInfo}"
            sellerspaymentStatusTextView.text = "Payment Method: ${order.payment}"
            numberinfo.text = "Contact Seller: ${order.phoneNumber}"

            if (!order.imageUrl.isNullOrEmpty()) {
                Picasso.get().load(order.imageUrl).into(seafoodImageView)
            } else {
                // Handle the case where the image URL is empty
                // For example, set a placeholder image or hide the ImageView
                seafoodImageView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_orderbuyer, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}
