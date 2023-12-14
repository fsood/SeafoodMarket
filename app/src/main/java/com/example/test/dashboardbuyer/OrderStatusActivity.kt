package com.example.test.dashboardbuyer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private lateinit var ordersList: MutableList<OrderStatus>
    private lateinit var ordersQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        val backButton = findViewById<ImageButton>(R.id.backB)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerViewOrders)
        ordersList = mutableListOf()
        adapter = OrderAdapter(ordersList) { order ->
            cancelOrder(order)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()

        if (currentUser != null) {
            ordersQuery = database.reference.child("orders").child("orders")
                .orderByChild("buyerId")
                .equalTo(currentUser)

            retrieveUserOrders()
        }
    }

    private fun retrieveUserOrders() {
        ordersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ordersList.clear()
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(OrderStatus::class.java)
                    order?.let {
                        fetchItemDetails(it.generatedItemId, it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchItemDetails(generatedItemId: String, order: OrderStatus) {
        val database = FirebaseDatabase.getInstance().reference
        val itemRef = database.child("seafood_items").child(generatedItemId)

        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val item = dataSnapshot.getValue(SeafoodItem::class.java)
                    item?.let {
                        // Access fetched details
                        order.imageUrl = it.imageUrl
                        order.deliveryInfo = it.deliveryInfo // Delivery information
                        order.phoneNumber = it.phoneNumber // Phone number
                        order.payment = it.payment // Payment method

                        ordersList.add(order)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun cancelOrder(order: OrderStatus) {
        val database = FirebaseDatabase.getInstance().reference

        val itemRef = database.child("seafood_items").child(order.generatedItemId)
        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val seafoodItem = dataSnapshot.getValue(SeafoodItem::class.java)
                    seafoodItem?.let {
                        /* it.availability += order.kgQuantity
                         itemRef.setValue(it)*/
                        val currentAvailability = it.availability.toDoubleOrNull() ?: 0.0
                        val orderQuantity = order.kgQuantity.toDoubleOrNull() ?: 0.0

                        it.availability = (currentAvailability + orderQuantity).toString()

                        itemRef.setValue(it)

                        val orderRef = FirebaseDatabase.getInstance().reference
                            .child("orders")
                            .child("orders")
                            .child(order.orderId)

                        orderRef.removeValue().addOnSuccessListener {
                            ordersList.remove(order)
                            adapter.notifyDataSetChanged()

                            Toast.makeText(
                                applicationContext,
                                "Order cancelled successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            // Handle failure
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}