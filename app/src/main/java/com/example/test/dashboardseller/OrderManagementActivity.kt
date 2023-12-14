package com.example.test.dashboardseller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.SeafoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OrderManagementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderManagementAdapter
    private lateinit var ordersList: MutableList<OrderManagement>
    private lateinit var ordersQuery: Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_management)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        ordersList = mutableListOf()
        adapter = OrderManagementAdapter(ordersList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val database = FirebaseDatabase.getInstance()

        if (currentUser != null) {
            ordersQuery = database.reference.child("orders").child("orders")
                .orderByChild("sellerId")
                .equalTo(currentUser)

            retrieveUserOrders()
        }
    }

    private fun retrieveUserOrders() {
        ordersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                ordersList.clear()
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(OrderManagement::class.java)
                    order?.let {
                        fetchSeafoodItemDetails(it.generatedItemId, it)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun fetchSeafoodItemDetails(generatedItemId: String, order: OrderManagement) {
        val database = FirebaseDatabase.getInstance().reference
        val itemRef = database.child("seafood_items").child(generatedItemId)

        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val seafoodItem = dataSnapshot.getValue(SeafoodItem::class.java)
                    seafoodItem?.let {
                        order.imageUrl = it.imageUrl
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
}
